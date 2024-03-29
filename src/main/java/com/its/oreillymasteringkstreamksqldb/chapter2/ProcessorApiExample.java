package com.its.oreillymasteringkstreamksqldb.chapter2;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

class ProcessorApiExample {

    public static void main(String[] args) {
        System.out.println("Entering ProcessorApiExample : main");
        // the builder is used to construct the topology
        Topology topology = new Topology();

        topology.addSource("UserSource", "users");
        topology.addProcessor("SayHello", SayHelloProcessor::new, "UserSource");

        // set the required properties for running Kafka Streams
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev2");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // build the topology and start streaming!
        KafkaStreams streams = new KafkaStreams(topology, config);
        System.out.println("Kafka streams instantiated with configured topology");
        streams.start();
        System.out.println("Kafka streams started");

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime
            .getRuntime()
            .addShutdownHook(new Thread(streams::close));
    }
}

