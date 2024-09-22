package com.doctorhoai.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@EnableDiscoveryClient
@SpringBootApplication
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}
	@Bean
	public RouteLocator routeLocator(
			RouteLocatorBuilder routeLocatorBuilder
	){
		return routeLocatorBuilder.routes()
				.route(
						p -> p
								.path("/doctorhoai/product/**")
								.filters(
										f -> f
												.rewritePath("/doctorhoai/product/(?<segment>.*)", "/${segment}")
								)
								.uri("lb://PRODUCTSERVICE")
				)
				.route(
						p -> p
								.path("/doctorhoai/order/**")
								.filters(
										f -> f
												.rewritePath("/doctorhoai/order/(?<segment>.*)", "/${segment}")
								)
								.uri("lb://ORDERSSERVICE")
				)
				.build();
	}
}
