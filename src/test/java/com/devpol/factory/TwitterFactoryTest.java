package com.devpol.factory;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.inject.Inject;

@MicronautTest
public class TwitterFactoryTest {

    @Inject
    private Twitter twitter;

    @Inject
    private TwitterStream twitterStream;

    @Test
    public void verify() {
        Assertions.assertEquals(twitter, TwitterFactory.getSingleton());
        Assertions.assertEquals(twitterStream, TwitterStreamFactory.getSingleton());
    }
}
