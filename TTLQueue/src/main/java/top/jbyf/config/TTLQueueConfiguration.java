package top.jbyf.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL队列 配置文件类代码
 */
@Configuration
public class TTLQueueConfiguration {
    // 普通交换机名称
    public static final String X_EXCHANGE = "X";
    // 死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    // 死信队列名称
    public static final String DEAD_LETTER_QUEUE = "QD";
    public static final String Y_DEAD_LETTER_ROUTING_KEY = "YD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // 设置过期时间10秒
    @Bean("queueA")
    public Queue queueA(){
        Map<String, Object> map = new HashMap<>(3);
        // 设置死信交换机
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死信routing-key
        map.put("x-dead-letter-routing-key",Y_DEAD_LETTER_ROUTING_KEY);
        // 设置超时时间
        map.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A)
                .withArguments(map)
                .build();
    }


    // 设置过期时间10秒
    @Bean("queueB")
    public Queue queueB(){
        Map<String, Object> map = new HashMap<>(3);
        // 设置死信交换机
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死信routing-key
        map.put("x-dead-letter-routing-key",Y_DEAD_LETTER_ROUTING_KEY);
        // 设置超时时间
        map.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B)
                .withArguments(map)
                .build();
    }


    // 设置过期时间10秒
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
                .build();
    }

    // 绑定普通交换机和普通队列A
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    // 绑定普通交换机和普通队列B
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExechange){
        return BindingBuilder
                .bind(queueB)
                .to(xExechange)
                .with("XB");
    }

    // 绑定死信交换机和死信队列
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder
                .bind(queueD)
                .to(yExchange)
                .with("YD");
    }
}
