package top.jbyf.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.jbyf.config.DelayQueueConfig;

import java.util.Date;

/**
 * 消费基于插件的延迟消息
 */
@Component
@Slf4j
public class DelayedQueueConsumer {
    @RabbitListener(
        queues = DelayQueueConfig.DELAY_QUEUE_NAME
    )
    public void receiveDelayQueue(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody(),"UTF-8");
        log.info("当前时间：{},收到延迟队列的消息：{}",new Date().toString(),msg);
    }
}
