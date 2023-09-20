package top.jbyf.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置延时队列交换机
 */
@Configuration
public class DelayQueueConfig {
    // 交换机
    public static final String DELAY_EXCHANGE_NAME = "delayed.exchange";
    // 队列
    public static final String DELAY_QUEUE_NAME = "delayed.queue";
    // routing key
    public static final String DELAY_ROUTING_KEY = "`delayed.routingKey";

    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object > params = new HashMap<>();
        params.put("x-delayed-type","direct");
        return new CustomExchange(
                DELAY_EXCHANGE_NAME,
                "x-delayed-message",
                true,
                false,
                params
        );

    }
    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.durable(DELAY_QUEUE_NAME).build();
    }

    @Bean
    public Binding delayedQueueBindingdelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAY_ROUTING_KEY)
                .noargs();
    }
}
