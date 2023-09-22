package top.jbyf.backupExchange.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.jbyf.backupExchange.config.BackupConfig;

@Component
@Slf4j
public class BackupConsumer {
    @RabbitListener(
            queues = BackupConfig.BACKUP_QUEUE
    )
    public void receiveBackup(Message message, Channel channel) throws Exception{
        log.info("备份交换机获取到信息：{}",new String(message.getBody()));
    }
}
