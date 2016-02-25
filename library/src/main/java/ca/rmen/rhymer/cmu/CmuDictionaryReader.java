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
import ca.rmen.rhymer.WordVariant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    static Map<String, List<WordVariant>> readWords(SyllableParser syllableParser, InputStream is) throws IOException {
        Map<String, List<WordVariant>> words = new HashMap<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            Pattern pattern = Pattern.compile("(^.*)\\(([0-9])\\).*$");
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.isEmpty()) continue;
                if (line.startsWith(";;;")) continue;
                int wordSeparator = line.indexOf("  ");
                String word = line.substring(0, wordSeparator).toLowerCase(Locale.US);
                int variantNumber = 0;
                Matcher matcher = pattern.matcher(word);
                if(matcher.matches()) {
                    word = matcher.group(1);
                    variantNumber = Integer.valueOf(matcher.group(2));
                }
                String[] phones = line.substring(wordSeparator+2).split(" ");
                String[] syllables = syllableParser.extractRhymingSyllables(phones);
                String lastStressRhymingSyllables = syllableParser.extractStressRhymingSyllables(phones);
                String lastRhymingSyllable = concatenateLastSyllables(syllables, 1);
                String lastTwoRhymingSyllables = concatenateLastSyllables(syllables, 2);
                String lastThreeRhymingSyllables = concatenateLastSyllables(syllables, 3);
                WordVariant wordVariant = new WordVariant(variantNumber, lastStressRhymingSyllables, lastRhymingSyllable, lastTwoRhymingSyllables, lastThreeRhymingSyllables);
                List<WordVariant> wordVariants = words.get(word);
                if(wordVariants == null) {
                    wordVariants = new ArrayList<>();
                    words.put(word, wordVariants);
                }
                wordVariants.add(wordVariant);
            }
        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return words;
    }

    /**
     * @return a string concatenating the last n syllables in the array
     * For example: if we have this word:
     * TELEPHONE:
     *   symbols:  T,EH1,L,AH0,F,OW2,N
     *   syllables: EHL,AHF,OWN
     * And if we want the last 2 syllables, we will return "AHFOWN"
     */
    private static String concatenateLastSyllables(String[] syllables, int n) {
        if (syllables.length < n) return null;

        StringBuilder builder = new StringBuilder();
        for (int i = syllables.length - n; i < syllables.length; i++) {
            builder.append(syllables[i]);
        }
        return builder.toString();
    }

}
