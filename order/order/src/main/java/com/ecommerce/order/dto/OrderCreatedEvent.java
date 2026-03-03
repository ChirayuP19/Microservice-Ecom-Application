package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
    private LocalDateTime created;



}
