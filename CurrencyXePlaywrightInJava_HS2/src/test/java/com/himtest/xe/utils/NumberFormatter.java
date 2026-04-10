package com.himtest.xe.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberFormatter {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("=\\s*(\\d+(?:\\.\\d{4})?)");
    private static final DecimalFormat FOUR_DP = new DecimalFormat("0.0000");
    private static final DecimalFormat TWO_DP = new DecimalFormat("0.00");

    private NumberFormatter() {
    }

    public static String extractAndFormatNumber(String text) {
        String cleaned = text.replace(",", "");
        Matcher matcher = NUMBER_PATTERN.matcher(cleaned);
        if (!matcher.find()) {
            return null;
        }
        //return TWO_DP.format(Double.parseDouble(matcher.group(1)));
        return FOUR_DP.format(Double.parseDouble(matcher.group(1)));
    }

    public static String multiplyRateWithAmount(String rate, double amount) {
        if (rate == null || rate.isBlank()) {
            throw new IllegalArgumentException("rate is empty or null");
        }
        return TWO_DP.format(Double.parseDouble(rate) * amount);
    }
}