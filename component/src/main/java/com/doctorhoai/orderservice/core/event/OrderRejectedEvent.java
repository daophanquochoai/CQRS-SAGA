package com.doctorhoai.orderservice.core.event;

import com.doctorhoai.orderservice.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    String orderId;
    String reason;
    OrderStatus orderStatus = OrderStatus.REJECTED;
}
