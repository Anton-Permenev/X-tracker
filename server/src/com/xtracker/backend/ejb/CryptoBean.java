package com.xtracker.backend.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Singleton(name = "crypto")
@Startup
public class CryptoBean {

    private SecureRandom randomGenerator;

    @PostConstruct
    public void init() {
        try {
            setRandomGenerator(SecureRandom.getInstance("SHA1PRNG"));
            System.out.println(randomGenerator == null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecureRandom getRandomGenerator() {
        return randomGenerator;
    }

    public void setRandomGenerator(SecureRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
    }
}
