package com.yboyacigil.twitter.stream.app;

import java.util.concurrent.BlockingQueue;

public class TwitterStreamStdoutProducer {

    private BlockingQueue<String> queue;

    public TwitterStreamStdoutProducer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        while(true) {
            String message = null;
            try {
                message = queue.take();
                System.out.printf("%s", message);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
