package com.datawizards.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class BankTransactionsProducer {
    private static final String TOPIC = "bank-transactions";

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "transactions-producer");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonSerializer");
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, "3");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        KafkaProducer<String, JsonNode> producer = new KafkaProducer<>(config);
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionsGenerator generator = new TransactionsGenerator();

        for(int i=0;i<10000;++i) {
            try {
                BankTransaction tr = generator.generate();
                JsonNode jsonNode = objectMapper.valueToTree(tr);
                ProducerRecord<String, JsonNode> record = new ProducerRecord<>(TOPIC, tr.getName(), jsonNode);
                producer.send(record).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
