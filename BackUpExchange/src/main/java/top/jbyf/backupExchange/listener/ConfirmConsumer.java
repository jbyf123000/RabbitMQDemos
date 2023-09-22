package top.jbyf.backupExchange.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.jbyf.backupExchange.config.ConfirmConfig;

@Component
@Slf4j
public class ConfirmConsumer {
    @RabbitListener(
            queues = ConfirmConfig.CONFIRM_QUEUE_NAME
    )
    public void receiverConfirmMessage(Message message, Channel channel) throws Exception{
        log.info("队列接收到消息：{}",new String(message.getBody(),"UTF-8"));
    }
}
