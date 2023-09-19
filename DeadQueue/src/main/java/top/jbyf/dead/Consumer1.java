package top.jbyf.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer1 {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String NORMAL_ROUTING_KEY = "normal_key";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String DEAD_ROUTING_KEY = "dead_key";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        Map<String, Object> params = new HashMap<>();
        //指定死信队列交换机
        params.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //指定死信队列routingkey
        params.put("x-dead-letter-routing-key",DEAD_ROUTING_KEY);
        //设定长度限制
        params.put("x-max-length",6);
        //创建死信队列交换机和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //创建队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,params);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        // 绑定交换机
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,NORMAL_ROUTING_KEY);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,DEAD_ROUTING_KEY);

        System.out.println("C1 wait for message.");
        DeliverCallback dCall = (consumerTag,message) -> {
            System.out.println("C1 consumed a message: " + new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(NORMAL_QUEUE,true,dCall,(consumerTag -> {}));


    }
}