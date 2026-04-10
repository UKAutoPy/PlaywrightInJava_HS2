package com.himtest.xe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CsvDataLoader {
    private CsvDataLoader() {
    }

    public static List<Map<String, String>> load(String resourcePath) {
        InputStream inputStream = CsvDataLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV resource not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<Map<String, String>> records = new ArrayList<>();
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isBlank()) {
                throw new IllegalStateException("CSV file is empty: " + resourcePath);
            }

            String[] headers = headerLine.split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }
                records.add(record);
            }
            return records;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read CSV resource: " + resourcePath, exception);
        }
    }
}