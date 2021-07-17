package com.its.oreillymasteringkstreamksqldb.chapter2;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class SayHelloProcessor implements Processor<Void, String, Void, String> {
    @Override
    public void init(ProcessorContext<Void, String> context) {
        System.out.println("Entering and leaving SayHelloProcessor : init");
    }

    @Override
    public void process(Record<Void, String> record) {
        System.out.println("Entering SayHelloProcessor : process");
        System.out.println("(Processor API) Hello, " + record.value());
        System.out.println("Leaving SayHelloProcessor : process");
    }

    @Override
    public void close() {
        System.out.println("Entering and leaving SayHelloProcessor : close");
    }
}
