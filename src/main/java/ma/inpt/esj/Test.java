package ma.inpt.esj;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Test {
    public static void main(String[] args) {
            // Configuration du consumer
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:29092");
            props.put("group.id", "jeunes-consumer-group");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

            // S'abonner au topic "jeunes"
            consumer.subscribe(Collections.singletonList("jeunes"));

            // Boucle pour lire les messages
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf("Offset: %d, Clé: %s, Valeur: %s%n", record.offset(), record.key(), record.value());
                    }
                }
            } finally {
                consumer.close();
            }
    }
}
