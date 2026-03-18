package com.mendel.service_transaction.integration.infraestructure.entrypoint.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ReaderUtil {

    private ReaderUtil() {
    }

    public static String readResource(String path) {
        try (InputStream inputStream = ReaderUtil.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading resource: " + path, e);
        }
    }
}