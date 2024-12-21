package recarga.pay.recargatransactions.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void sendMessage(String topic, String key, T value, Class<T> type) {
        try {
            var recordValue = objectMapper.writeValueAsString(value);
            var record = new ProducerRecord<>(topic, null, key, recordValue);
            record.headers().add("EventType", type.getSimpleName().getBytes());

            kafkaTemplate.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
