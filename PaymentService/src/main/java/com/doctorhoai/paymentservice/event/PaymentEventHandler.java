package com.doctorhoai.paymentservice.event;

import com.doctorhoai.core.event.PaymentProcessedEvent;
import com.doctorhoai.paymentservice.data.PaymentEntity;
import com.doctorhoai.paymentservice.data.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final PaymentRepository paymentRepository;
    @EventHandler
    public void handle(PaymentProcessedEvent paymentProcessedEvent){
        log.info("Payment : PaymentProcessEvent is called for orderId : " + paymentProcessedEvent.getOrderId());
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(paymentProcessedEvent, paymentEntity);
        paymentRepository.save(paymentEntity);
    }

}
