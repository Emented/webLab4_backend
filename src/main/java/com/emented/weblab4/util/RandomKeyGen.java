package com.emented.weblab4.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomKeyGen {
    private final static char[] DICTIONARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890".toCharArray();

    public String generateKey(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(DICTIONARY[ThreadLocalRandom.current().nextInt(DICTIONARY.length)]);
        }
        return sb.toString();
    }
}
