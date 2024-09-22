package com.doctorhoai.orderservice.query;


import com.doctorhoai.orderservice.core.data.OrderEntity;
import com.doctorhoai.orderservice.core.data.OrderRepository;
import com.doctorhoai.orderservice.core.event.OrderApprovedEvent;
import com.doctorhoai.orderservice.core.event.OrderCreatedEvent;
import com.doctorhoai.orderservice.core.event.OrderRejectedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    private final OrderRepository ordersRepository;

    public OrderEventsHandler(OrderRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);

        ordersRepository.save(orderEntity);
    }


    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());

        if(orderEntity == null) {
            // TODO: Do something about it
            return;
        }

        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());

        ordersRepository.save(orderEntity);

    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
        orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
}
