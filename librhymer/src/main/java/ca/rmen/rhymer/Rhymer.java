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

package ca.rmen.rhymer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rhymer {
    private final Map<String, String[]> words = new HashMap<>();
    private final Map<String, List<String>> lastSyllableMap = new HashMap<>();
    private final Map<String, List<String>> lastTwoSyllablesMap = new HashMap<>();
    private final Map<String, List<String>> lastThreeSyllablesMap = new HashMap<>();
    private SyllableParser syllableParser;

    public Rhymer() {
    }

    /**
     * @return an array of three elements: 1) an array of one-syllable matches,
     * 2) an array of two-syllable matches, and 3) an array of three-syllable matches.
     */
    public String[][] getRhymingWords(String word) {
        String[] oneSyllableMatches = new String[0];
        String[] twoSyllableMatches = new String[0];
        String[] threeSyllableMatches = new String[0];
        String[] symbols = words.get(word);

        // The word doesn't exist in our dictionary
        if (symbols == null) return new String[][]{oneSyllableMatches, twoSyllableMatches, threeSyllableMatches};

        String[] syllables = syllableParser.extractRhymingSyllables(symbols);

        String lastSyllable = concatenateLastSyllables(syllables, 1);
        List<String> matches = lastSyllableMap.get(lastSyllable);
        Collections.sort(matches);
        oneSyllableMatches = matches.toArray(new String[matches.size()]);
        if (syllables.length >= 2) {
            String lastTwoSyllables = concatenateLastSyllables(syllables, 2);
            matches = lastTwoSyllablesMap.get(lastTwoSyllables);
            Collections.sort(matches);
            twoSyllableMatches = matches.toArray(new String[matches.size()]);
        }

        if (syllables.length >= 3) {
            String lastThreeSyllables = concatenateLastSyllables(syllables, 3);
            matches = lastThreeSyllablesMap.get(lastThreeSyllables);
            Collections.sort(matches);
            threeSyllableMatches = matches.toArray(new String[matches.size()]);
        }
        return new String[][]{oneSyllableMatches, twoSyllableMatches, threeSyllableMatches};
    }

    /**
     * @param symbolMap a map of phone symbols to phone types.
     * @param words     a map of words to the list of symbols for each word
     */
    public void buildIndex(Map<String, PhoneType> symbolMap, Map<String, String[]> words) {
        this.words.clear();
        this.words.putAll(words);
        lastSyllableMap.clear();
        lastTwoSyllablesMap.clear();
        lastThreeSyllablesMap.clear();

        syllableParser = new SyllableParser(symbolMap);
        for (String word : words.keySet()) {
            String[] symbols = words.get(word);
            String[] syllables = syllableParser.extractRhymingSyllables(symbols);
            if (syllables.length >= 3) {
                String lastThreeSyllables = concatenateLastSyllables(syllables, 3);
                List<String> lastThreeSyllableWords = get(lastThreeSyllablesMap, lastThreeSyllables);
                lastThreeSyllableWords.add(word);
            }
            if (syllables.length >= 2) {
                String lastTwoSyllables = concatenateLastSyllables(syllables, 2);
                List<String> lastTwoSyllableWords = get(lastTwoSyllablesMap, lastTwoSyllables);
                lastTwoSyllableWords.add(word);
            }
            String lastSyllable = syllables[syllables.length - 1];
            List<String> lastSyllableWords = get(lastSyllableMap, lastSyllable);
            lastSyllableWords.add(word);
        }
    }

    /**
     * @return a string concatenating the last n syllables in the array
     */
    private static String concatenateLastSyllables(String[] syllables, int n) {
        if (syllables.length < n)
            throw new IllegalArgumentException(String.format("Cannot extract %d syllables from list of %d syllables", n, syllables.length));

        StringBuilder builder = new StringBuilder();
        for (int i = syllables.length - n; i < syllables.length; i++) {
            builder.append(syllables[i]);
        }
        return builder.toString();
    }

    private static List<String> get(Map<String, List<String>> map, String key) {
        List<String> result = map.get(key);
        if (result == null) {
            result = new ArrayList<>();
            map.put(key, result);
        }
        return result;
    }

}
