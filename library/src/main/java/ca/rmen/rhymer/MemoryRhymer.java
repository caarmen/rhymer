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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This implementation loads all the rhyming data in memory.
 */
public class MemoryRhymer extends Rhymer {
    private final Map<String, List<WordVariant>> words = new HashMap<>();
    private final Map<String, SortedSet<String>> lastSyllableMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastTwoSyllablesMap = new HashMap<>();
    private final Map<String, SortedSet<String>> lastThreeSyllablesMap = new HashMap<>();

    public Set<String> getWords() {
        return words.keySet();
    }

    public List<WordVariant> getWordVariants(String word){
        return words.get(word);
    }

    protected SortedSet<String> getWordsWithLastSyllable(String lastSyllable) {
        return lastSyllableMap.get(lastSyllable);
    }

    protected SortedSet<String> getWordsWithLastTwoSyllables(String lastTwoSyllables) {
        return lastTwoSyllablesMap.get(lastTwoSyllables);
    }

    protected SortedSet<String> getWordsWithLastThreeSyllables(String lastThreeSyllables) {
        return lastThreeSyllablesMap.get(lastThreeSyllables);
    }

    /**
     * @param words     a map of words to the list of word variants for each word
     */
    public void buildIndex(Map<String, List<WordVariant>> words) {
        this.words.clear();
        this.words.putAll(words);
        lastSyllableMap.clear();
        lastTwoSyllablesMap.clear();
        lastThreeSyllablesMap.clear();

        for (String word : words.keySet()) {
            List<WordVariant> wordVariants = words.get(word);
            for(WordVariant wordVariant : wordVariants) {
                if (wordVariant.lastThreeRhymingSyllables != null ) {
                    indexWord(lastThreeSyllablesMap, wordVariant.lastThreeRhymingSyllables, word);
                }
                if (wordVariant.lastTwoRhymingSyllables != null) {
                    indexWord(lastTwoSyllablesMap, wordVariant.lastTwoRhymingSyllables, word);
                }
                indexWord(lastSyllableMap, wordVariant.lastRhymingSyllable, word);
            }
        }
    }

    /**
     * Add a mapping for the given syllable to the given word, to the given map.
     */
    private void indexWord(Map<String, SortedSet<String>> map, String syllable, String word) {
        SortedSet<String> wordsForSyllable = map.get(syllable);
        if (wordsForSyllable == null) {
            wordsForSyllable = new TreeSet<>();
            map.put(syllable, wordsForSyllable);
        }
        wordsForSyllable.add(word);
    }

}
