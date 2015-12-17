package com.yboyacigil.twitter.stream.app;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class TwitterStreamKafkaProducer {

    private String topic;
    private BlockingQueue<String> queue;

    private Producer<String, String> producer;

    public TwitterStreamKafkaProducer(String topic, Properties producerProperties, BlockingQueue<String> queue) {
        this.topic = topic;
        this.queue = queue;

        ProducerConfig producerConfig = new ProducerConfig(producerProperties);
        producer = new Producer<String, String>(producerConfig);
    }

    public void run() {
        while(true) {
            try {
                KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, queue.take());
                producer.send(msg);
            } catch (InterruptedException e) {
                producer.close();
                break;
            }
        }
    }
}
