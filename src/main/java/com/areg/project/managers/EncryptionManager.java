/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Component
public class EncryptionManager {

    private static final short SALT_LENGTH = 16;
    private static final String RNG_ALGORITHM = "SHA1PRNG";
    private static final String ALL_CHARACTERS;
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final short PBE_KEY_LENGTH = 256;
    private static final short PBE_KEY_ITERATIONS = 1024;


    static {
        ALL_CHARACTERS = generateAllCharacters();
    }

    public String encrypt(String password, String salt) {
        if (StringUtils.isAnyBlank(password, salt)) {
            return "";
        }

        final byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }

    public String generateSalt() {
        final var salt = new StringBuilder(SALT_LENGTH);
        for (int i = 0; i < SALT_LENGTH; ++i) {
            final Random random = createSecureRandom();
            salt.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }
        return salt.toString();
    }

    private SecureRandom createSecureRandom() {
        try {
            return SecureRandom.getInstance(RNG_ALGORITHM);
        } catch (NoSuchAlgorithmException nae) {
            return new SecureRandom();
        }
    }

    private byte[] hash(char[] password, byte[] salt) {
        final var spec = new PBEKeySpec(password, salt, PBE_KEY_ITERATIONS, PBE_KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            final var skf = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    private static String generateAllCharacters() {
        final var allCharacters = new StringBuilder();
        appendRange(allCharacters, '!', '-');
        appendRange(allCharacters, '0', '9');
        appendRange(allCharacters, 'A', 'Z');
        appendRange(allCharacters, 'a', 'z');
        return allCharacters.toString();
    }

    private static void appendRange(StringBuilder builder, char start, char end) {
        for (char c = start; c <= end; ++c) {
            builder.append(c);
        }
    }
}
