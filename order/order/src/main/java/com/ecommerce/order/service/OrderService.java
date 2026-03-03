package com.ecommerce.order.service;


import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Autowired
    private StreamBridge streamBridge;
//    private final RabbitTemplate rabbitTemplate;

    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userOptional.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create Order.
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems=cartItems
                .stream()
                .map(item->new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);
        // publish order created event

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount(),
                mapToOrderItemDTOs(savedOrder.getItems()),
                savedOrder.getCreatedAt()
        );

//        rabbitTemplate.convertAndSend
//                ("order.exchange",
//                "order.tracking",
//                event);
        streamBridge.send("createOrder-out-0",event);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private List<OrderItemDTO> mapToOrderItemDTOs(List<OrderItem> items){
        return items.stream()
                .map(x->new OrderItemDTO(
                        x.getId(),
                        x.getProductId(),
                        x.getQuantity(),
                        x.getPrice(),
                        x.getPrice().multiply(new BigDecimal(x.getQuantity()))
                )).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        ))
                        .toList(),
                        order.getCreatedAt()
        );
    }
}
