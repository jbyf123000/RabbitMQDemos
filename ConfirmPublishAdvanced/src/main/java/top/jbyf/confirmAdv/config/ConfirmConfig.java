package top.jbyf.confirmAdv.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class ConfirmConfig {

    // 交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    // 队列
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    // routing key
    public static final String CONFIRM_ROUTING_KEY = "key1";

    @Bean
    public DirectExchange confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmQueueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                               @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder
                .bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }

}
