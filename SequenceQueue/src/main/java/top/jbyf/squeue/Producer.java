package top.jbyf.squeue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import top.jbyf.squeue.config.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static final String QUEUE_NAME = "sequence_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        Map<String, Object> params = new HashMap<>();
        // 设置最大优先级为10 , 官方允许 0-255
        params.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, params);
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .priority(5)
                .build();

        for (int i = 0; i < 10; i++) {
            String message = "info::" + i;
            if (i == 5) {
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }

        System.out.println("消息发送完成");

    }
}
