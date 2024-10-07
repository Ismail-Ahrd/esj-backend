package ma.inpt.esj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.JeuneDto;

@Service
public class KafkaProducerService {

    private final StreamBridge streamBridge;

    @Autowired
    public KafkaProducerService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    /**
     * Sends a Jeune object to the Kafka topic 'jeune-topic'.
     *
     * @param jeune The Jeune object to send.
     */
    public void sendJeune(JeuneDto jeune) {
        boolean sent = streamBridge.send("output", jeune);
        if (sent) {
            System.out.println("Jeune sent to Kafka: " + jeune);
        } else {
            System.err.println("Failed to send Jeune to Kafka: " + jeune);
        }
    }
}