/*
 * Copyright (c) 2016 Carmen Alvarez
 *
 * This file is part of Rhymer.
 *
 * Rhymer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Rhymer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Rhymer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.rmen.rhymer.cmu;

import ca.rmen.rhymer.PhoneType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class CmuDictionaryReader {

    private CmuDictionaryReader() {
        // Prevent instantiation of a utility class
    }

    static Map<String, PhoneType> readPhones(InputStream is) throws IOException {
        Map<String, PhoneType> phones = new HashMap<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] tokens = line.split("\t");
                String symbol = tokens[0];
                PhoneType phoneType = PhoneType.valueOf(tokens[1].toUpperCase(Locale.US));
                phones.put(symbol, phoneType);
            }
        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return phones;
    }

    static Map<String, String[]> readWords(InputStream is) throws IOException {
        Map<String, String[]> words = new HashMap<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.isEmpty()) continue;
                if (line.startsWith(";;;")) continue;
                int wordSeparator = line.indexOf("  ");
                String word = line.substring(0, wordSeparator);
                String[] phones = line.substring(wordSeparator+2).split(" ");
                words.put(word, phones);
            }
        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return words;
    }

}
