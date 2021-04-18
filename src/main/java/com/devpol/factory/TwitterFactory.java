package com.devpol.factory;

import io.micronaut.context.annotation.Factory;
import twitter4j.Twitter;

import javax.inject.Singleton;

@Factory
public class TwitterFactory {

    @Singleton
    public Twitter twitter() {
        return twitter4j.TwitterFactory.getSingleton();
    }

}
