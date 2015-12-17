package com.yboyacigil.twitter.stream.app;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class Application {

    @Autowired
    private Environment env;

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LinkedBlockingQueue<String> twitterStreamQueue() {
        int size = env.getProperty("app.queue-size", Integer.class);
        return new LinkedBlockingQueue<>(size);
    }

    @Bean
    public String kafkaTopic() {
        String topic = env.getProperty("app.kafka.topic");
        return topic;
    }

    @Bean
    public Properties kafkaProducerProperties() {
        Properties properties = new Properties();
        properties.put("metadata.broker.list", env.getProperty("app.kafka.broker-list"));
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("client.id", env.getProperty("app.kafka.client-id"));
        return properties;
    }

    @Bean
    public CommandLineArgs commandLineArgs() {
        CommandLineArgs result = new CommandLineArgs();
        result.setConsumerKey(rejectIfInvalid(env, "consumer-key"));
        result.setConsumerSecret(rejectIfInvalid(env, "consumer-secret"));
        result.setAuthToken(rejectIfInvalid(env, "auth-token"));
        result.setAuthSecret(rejectIfInvalid(env, "auth-secret"));
        String trackTerms = rejectIfInvalid(env, "track-terms");
        result.setTrackTerms(Lists.newArrayList(trackTerms.split(",")));
        result.setProducerType(CommandLineArgs.ProducerType.valueOf(rejectIfInvalid(env, "producer-type")));
        return result;
    }

    private String rejectIfInvalid(Environment env, String property) {
        String s = env.getProperty(property);
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("No command line arg: '" + property + "'");
        }
        return s;
    }

}
