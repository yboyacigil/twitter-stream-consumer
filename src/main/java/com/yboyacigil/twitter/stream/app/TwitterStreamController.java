package com.yboyacigil.twitter.stream.app;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@Controller
public class TwitterStreamController implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(TwitterStreamController.class);

    @Autowired
    private CommandLineArgs arguments;

    @Autowired
    private String kafkaTopic;

    @Autowired
    private Properties kafkaProducerProperties;

    @Autowired
    private LinkedBlockingQueue<String> twitterStreamQueue;

    public void run(String... args) throws Exception {
        Authentication auth = new OAuth1(
                arguments.getConsumerKey(),
                arguments.getConsumerSecret(),
                arguments.getAuthToken(),
                arguments.getAuthSecret());

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(arguments.getTrackTerms());

        final Client client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(twitterStreamQueue))
                .build();

        client.connect();


        if (CommandLineArgs.ProducerType.STDOUT.equals(arguments.getProducerType())) {
            TwitterStreamStdoutProducer stdout = new TwitterStreamStdoutProducer(twitterStreamQueue);
            stdout.run();
        } else if (CommandLineArgs.ProducerType.KAFKA.equals(arguments.getProducerType())) {
            TwitterStreamKafkaProducer kafka = new TwitterStreamKafkaProducer(kafkaTopic, kafkaProducerProperties, twitterStreamQueue);
            kafka.run();
        } else {
            logger.info("No producer closing");
        }

        client.stop();
    }

}
