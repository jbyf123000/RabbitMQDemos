package top.jbyf.squeue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.squeue.config.RabbitMqUtils;

public class Consumer {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //推送的消息如何进行消费的接口回调
        DeliverCallback callback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody());
            System.out.println("优先级队列消费者获取到消息： " + message);
        };
        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println("broken message");
        };
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         */
        channel.basicConsume(Producer.QUEUE_NAME,true,callback,cancelCallback);
    }
}
