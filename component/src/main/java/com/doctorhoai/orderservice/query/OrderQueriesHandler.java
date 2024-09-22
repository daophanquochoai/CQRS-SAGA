package com.doctorhoai.orderservice.query;

import com.doctorhoai.orderservice.core.data.OrderEntity;
import com.doctorhoai.orderservice.core.data.OrderRepository;
import com.doctorhoai.orderservice.core.model.OrderSummary;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderQueriesHandler {
    OrderRepository ordersRepository;

    public OrderQueriesHandler(OrderRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(),
                orderEntity.getOrderStatus(), "");
    }
}
