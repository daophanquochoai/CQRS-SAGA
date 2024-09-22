package com.doctorhoai.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
