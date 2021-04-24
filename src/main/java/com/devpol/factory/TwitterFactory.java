package com.devpol.factory;

import io.micronaut.context.annotation.Factory;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.inject.Singleton;

@Factory
public class TwitterFactory {

    @Singleton
    public Twitter twitter() {
        return twitter4j.TwitterFactory.getSingleton();
    }

    @Singleton
    public TwitterStream twitterStreamFactory() {
        return twitter4j.TwitterStreamFactory.getSingleton();
    }

}
