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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Rhymer {
    private static final int THRESHOLD_TOO_MANY_RHYMES = 500;

    private final Map<String, List<WordVariant>> words = new HashMap<>();
    private final Map<String, SortedSet<String>> lastSyllableMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastTwoSyllablesMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastThreeSyllablesMap = new HashMap<>();
    private SyllableParser syllableParser;

    public Rhymer() {
    }

    /**
     * @return a list of RhymeResults.  Most words will have one RhymeResult.  Words with multiple possible
     * pronunciations (word variants) will have one RhymeResult per variant.
     */
    public List<RhymeResult> getRhymingWords(String word) {
        List<RhymeResult> results = new ArrayList<>();
        String lookupWord = word.toLowerCase(Locale.US);

        // The word doesn't exist in our dictionary
        List<WordVariant> wordVariants = words.get(lookupWord);
        if (wordVariants == null) return results;

        // One RhymeResult per word variant (pronunciation)
        for (WordVariant wordVariant : wordVariants) {

            String[] syllables = syllableParser.extractRhymingSyllables(wordVariant.symbols);

            Set<String> matches1 = lookupWords(lookupWord, syllables, 1, lastSyllableMap);
            Set<String> matches2 = new TreeSet<>();
            Set<String> matches3 = new TreeSet<>();

            if (syllables.length >= 2) {
                matches2 = lookupWords(lookupWord, syllables, 2, lastTwoSyllablesMap);
                matches1.removeAll(matches2);
            }
            if (syllables.length >= 3) {
                matches3 = lookupWords(lookupWord, syllables, 3, lastThreeSyllablesMap);
                matches1.removeAll(matches3);
                matches2.removeAll(matches3);
            }

            // Some words, like "puppy", match way too many words.... any word
            // ending with an "ee" sound (IY0 phone).  If we end up in this situation,
            // completely ignore all the one-syllable matches, and only return
            // 2 and 3 syllable matches.
            // TODO maybe there is a better way to solve this problem.
            if (matches1.size() > THRESHOLD_TOO_MANY_RHYMES && matches2.size() > 0) {
                matches1.clear();
            }

            RhymeResult result = new RhymeResult(word + "(" + wordVariant.variantNumber + ")",
                    matches1.toArray(new String[matches1.size()]),
                    matches2.toArray(new String[matches2.size()]),
                    matches3.toArray(new String[matches3.size()]));
            results.add(result);
        }
        return results;
    }

    /**
     * @param word              the word for which we want to find rhyming words.
     * @param syllables         the syllables of the word already parsed (we parse them before calling this method for performance).
     * @param numberOfSyllables specifies the number of syllables to use when looking for rhyming words.
     * @param syllablesMap      map of syllables to words: the key contains syllables of numberOfSyllables syllables.
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
     * @param words     a map of words to the list of word variants for each word
     */
    public void buildIndex(Map<String, PhoneType> symbolMap, Map<String, List<WordVariant>> words) {
        this.words.clear();
        this.words.putAll(words);
        lastSyllableMap.clear();
        lastTwoSyllablesMap.clear();
        lastThreeSyllablesMap.clear();

        syllableParser = new SyllableParser(symbolMap);
        for (String word : words.keySet()) {
            List<WordVariant> wordVariants = words.get(word);
            for(WordVariant wordVariant : wordVariants) {
                String[] syllables = syllableParser.extractRhymingSyllables(wordVariant.symbols);
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
