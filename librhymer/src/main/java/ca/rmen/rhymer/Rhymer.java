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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Rhymer {
    private static final int THRESHOLD_TOO_MANY_RHYMES = 500;

    private final Map<String, String[]> words = new HashMap<>();
    private final Map<String, Integer> wordVariants = new HashMap<>();
    private final Map<String, SortedSet<String>> lastSyllableMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastTwoSyllablesMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastThreeSyllablesMap = new HashMap<>();
    private SyllableParser syllableParser;

    public Rhymer() {
    }

    /**
     * @return an array of three elements: 1) an array of one-syllable matches,
     * 2) an array of two-syllable matches, and 3) an array of three-syllable matches.
     */
    public String[][] getRhymingWords(String word) {
        word = word.toUpperCase();
        String[] symbols = words.get(word);

        // The word doesn't exist in our dictionary
        if (symbols == null) return new String[][]{new String[0], new String[0], new String[0]};

        String[] syllables = syllableParser.extractRhymingSyllables(symbols);

        Set<String> matches1 = lookupWords(word, syllables, 1, lastSyllableMap);
        Set<String> matches2 = new TreeSet<>();
        Set<String> matches3 = new TreeSet<>();

        if (syllables.length >= 2) {
            matches2 = lookupWords(word, syllables, 2, lastTwoSyllablesMap);
            matches1.removeAll(matches2);
        }
        if (syllables.length >= 3) {
            matches3 = lookupWords(word, syllables, 3, lastThreeSyllablesMap);
            matches1.removeAll(matches3);
            matches2.removeAll(matches3);
        }

        // Some words, like "puppy", match way to many words.... any word
        // ending with an "ee" sound (IY0 phone).  If we end up in this situation,
        // completely ignore all the one-syllable matches, and only return
        // 2 and 3 syllable matches.
        if (matches1.size() > THRESHOLD_TOO_MANY_RHYMES && matches2.size() > 0) {
            matches1.clear();
        }

        return new String[][]{
                matches1.toArray(new String[matches1.size()]),
                matches2.toArray(new String[matches2.size()]),
                matches3.toArray(new String[matches3.size()])
        };
    }

    /**
     *
     * @param word the word for which we want to find rhyming words.
     * @param syllables the syllables of the word already parsed (we parse them before calling this method for performance).
     * @param numberOfSyllables specifies the number of syllables to use when looking for rhyming words.
     * @param syllablesMap map of syllables to words: the key contains syllables of numberOfSyllables syllables.
     * @return a list of words which rhyme with the last numberOfSyllables syllables of the given word
     */
    private static Set<String> lookupWords(String word, String[] syllables, int numberOfSyllables, Map<String, SortedSet<String>> syllablesMap) {
        String lastSyllables = concatenateLastSyllables(syllables, numberOfSyllables);
        Set<String> matches = new TreeSet<>(syllablesMap.get(lastSyllables));
        matches.remove(word);
        return matches;
    }

    /**
     * @param symbolMap a map of phone symbols to phone types.
     * @param words     a map of words to the list of symbols for each word
     */
    public void buildIndex(Map<String, PhoneType> symbolMap, Map<String, String[]> words) {
        this.words.clear();
        this.words.putAll(words);
        wordVariants.clear();
        lastSyllableMap.clear();
        lastTwoSyllablesMap.clear();
        lastThreeSyllablesMap.clear();

        syllableParser = new SyllableParser(symbolMap);
        for (String word : words.keySet()) {
            if(word.matches("\\([0-9]\\)")) {
                int variantPos = word.indexOf('(');
            // Some words have multiple entries in the dictionary, for multiple pronunciations:
            // ex:
            // TUESDAY  T UW1 Z D IY0
            // TUESDAY(1)  T UW1 Z D EY2
            // TUESDAY(2)  T Y UW1 Z D EY2
            // We'll store the number of variants for this word.
                String wordRoot = word.substring(0, variantPos);
                int variantNumber = Integer.valueOf(word.substring(variantPos+1, variantPos+3));
                int variantCount = wordVariants.get(wordRoot);
                if (variantCount < variantNumber) wordVariants.put(wordRoot, variantNumber);
            }
            String[] symbols = words.get(word);
            String[] syllables = syllableParser.extractRhymingSyllables(symbols);
            if (syllables.length >= 3) {
                String lastThreeSyllables = concatenateLastSyllables(syllables, 3);
                Set<String> lastThreeSyllableWords = get(lastThreeSyllablesMap, lastThreeSyllables);
                lastThreeSyllableWords.add(word);
            }
            if (syllables.length >= 2) {
                String lastTwoSyllables = concatenateLastSyllables(syllables, 2);
                Set<String> lastTwoSyllableWords = get(lastTwoSyllablesMap, lastTwoSyllables);
                lastTwoSyllableWords.add(word);
            }
            String lastSyllable = syllables[syllables.length - 1];
            Set<String> lastSyllableWords = get(lastSyllableMap, lastSyllable);
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

    private static Set<String> get(Map<String, SortedSet<String>> map, String key) {
        SortedSet<String> result = map.get(key);
        if (result == null) {
            result = new TreeSet<>();
            map.put(key, result);
        }
        return result;
    }

}
