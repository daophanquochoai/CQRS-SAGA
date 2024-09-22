package com.doctorhoai.orderservice.core.event;

import com.doctorhoai.orderservice.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    String orderId;
    OrderStatus orderStatus = OrderStatus.APPROVED;
}
