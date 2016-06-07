package de.twiechert.linroad.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by tafyun on 07.06.16.
 */
public class SampleConfig extends Properties {

    public SampleConfig() {
        this.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.0.2:9092, 172.17.0.3:9092, 172.17.0.4:9092");
        this.put(ProducerConfig.ACKS_CONFIG, "all");
        this.put(ProducerConfig.RETRIES_CONFIG, 0);
        this.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        this.put(ProducerConfig.LINGER_MS_CONFIG,  1);
        this.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        this.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "de.twiechert.linroad.kafka.core.StringArraySerializer");
        this.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "de.twiechert.linroad.kafka.core.StringArraySerializer");
    }
}
