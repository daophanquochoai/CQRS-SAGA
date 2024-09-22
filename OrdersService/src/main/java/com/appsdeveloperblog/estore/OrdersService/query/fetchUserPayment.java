package com.appsdeveloperblog.estore.OrdersService.query;

import com.doctorhoai.core.commands.User;
import com.doctorhoai.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class fetchUserPayment {
    @QueryHandler
    public User handle( FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery){
        User user = User.builder()
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .firstName("Hoai")
                .lastName("Dao")
                .build();
        return user;
    }
}
