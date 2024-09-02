package ma.inpt.esj.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.KafkaException;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KafkaUtils {
    public static boolean isKafkaServerAvailable() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "3000"); // Timeout after 3 seconds
        config.put(AdminClientConfig.RETRIES_CONFIG, "1"); // Only try once

        try (AdminClient adminClient = AdminClient.create(config)) {
            adminClient.describeCluster().nodes().get(3, TimeUnit.SECONDS); // Timeout after 3 seconds
            return true;
        } catch (KafkaException | InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Kafka server is not available: " + e.getMessage());
            return false;
        }
    }
}
