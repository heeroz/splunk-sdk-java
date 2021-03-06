/*
 * Copyright 2011 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.splunk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value conversion helper functions.
 */
class Value {

    /**
     * Convert the given {@code String} to a boolean value.
     *
     * @param value Value to convert.
     * @return boolean value.
     */
    static boolean toBoolean(String value) {
        if (value == null) return false;
        if (value.equals("0"))
            return false;
        if (value.equals("1"))
            return true;
        if (value.toLowerCase().equals("false"))
            return false;
        if (value.toLowerCase().equals("true"))
            return true;
        String message = String.format("Value error: '%s'", value);
        throw new RuntimeException(message);
    }

    /**
     * Convert the given {@code String} to a numeric byte count.
     *
     * @param value Value to convert.
     * @return Count of bytes.
     */
    static long toByteCount(String value) {
        long multiplier = 1;
        if (value.endsWith("B")) {
            if (value.endsWith("KB"))
                multiplier = 1024;
            else if (value.endsWith("MB"))
                multiplier = 1024*1024;
            else if (value.endsWith("GB"))
                multiplier = 1024*1024*1024;
            else {
                String message = String.format("Value error: '%s'", value);
                throw new RuntimeException(message);
            }
            value = value.substring(0, value.length()-2);
        }
        return Long.parseLong(value) * multiplier;
    }

    private static SimpleDateFormat dateFormat = null;
    private static Pattern datePattern = null;

    /**
     * Convert the given {@code String} to a {@code Date} value.
     *
     * @param value Value to convert.
     * @return Date value.
     */
    static Date toDate(String value) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            dateFormat.setLenient(true);
        }
        if (datePattern == null) {
            String pattern = "(.*)\\.\\d+([\\-+]\\d+):(\\d+)";
            datePattern = Pattern.compile(pattern);
        }
        Date result;
        try {
            // Must first remove the colon (':') from the timezone
            // field, or SimpleDataFormat will not parse correctly.
            // Eg: 2010-01-01T12:00:00+01:00 => 2010-01-01T12:00:00+0100
            Matcher matcher = datePattern.matcher(value);
            value = matcher.replaceAll("$1$2$3");
            result = dateFormat.parse(value);
        }
        catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * Convert the given {@code String}, representing a date from the epoch,
     * to a {@code Date} value.
     *
     * @param value Value to convert.
     * @return Date value.
     */
    static Date toDateFromEpoch(String value) {
        return new Date(Long.parseLong(value)*1000);
    }

    /**
     * Convert the given {@code String} to a {@code float} value.
     *
     * @param value Value to convert.
     * @return {@code float} value.
     */
    static float toFloat(String value) {
        return Float.parseFloat(value);
    }

    /**
     * Convert the given {@code String} to an {@code int} value.
     *
     * @param value Value to convert.
     * @return {@code int} value.
     */
    static int toInteger(String value) {
        return Integer.parseInt(value);
    }

    /**
     * Convert the given {@code String} to a {@code long} value.
     *
     * @param value Value to convert.
     * @return {@code long} value.
     */
    static long toLong(String value) {
        return Long.parseLong(value);
    }
}

