package com.its.oreillymasteringkstreamksqldb.chapter2;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Slf4j
public class DslExample {
    public static void main(String [] args) {
        System.out.println("Entering DslExample : main");
        StreamsBuilder builder = new StreamsBuilder();

        KStream <Void, String> stream = builder.stream("users");

        stream
            .foreach((key, value) -> {
                System.out.println(" DSL - Hello : " + value);
            });

        // you can also print using the `print` operator
        // stream.print(Printed.<String, String>toSysOut().withLabel("source"));

        // set the required properties for running Kafka Streams
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev1");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // build the topology and start streaming
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        System.out.println("Kafka streams instantiated");
        streams.start();
        System.out.println("Kafka streams started");

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime
            .getRuntime()
            .addShutdownHook(new Thread(streams::close));
        System.out.println("Leaving DslExample : main");
    }
}
