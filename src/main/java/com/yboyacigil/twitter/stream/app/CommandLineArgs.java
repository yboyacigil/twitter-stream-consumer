package com.yboyacigil.twitter.stream.app;

import java.util.List;

public class CommandLineArgs {

    private String consumerKey;
    private String consumerSecret;
    private String authToken;
    private String authSecret;
    private List<String> trackTerms;
    private ProducerType producerType;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public List<String> getTrackTerms() {
        return trackTerms;
    }

    public void setTrackTerms(List<String> trackTerms) {
        this.trackTerms = trackTerms;
    }

    public ProducerType getProducerType() {
        return producerType;
    }

    public void setProducerType(ProducerType producerType) {
        this.producerType = producerType;
    }

    public static enum ProducerType {
        STDOUT, KAFKA
    }
}
