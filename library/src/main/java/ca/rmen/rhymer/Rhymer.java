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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Rhymer {

    /**
     * @param word the word for which we want to find rhymes.
     * @return a list of RhymeResults.  Most words will have one RhymeResult.  Words with multiple possible
     * pronunciations (word variants) will have one RhymeResult per variant.
     */
    public List<RhymeResult> getRhymingWords(String word) {
        return getRhymingWords(word, -1);
    }

    /**
     * @param word the word for which we want to find rhymes.
     * @param maxResults return at most this many results per rhyme type. For no limit, pass -1.
     * @return a list of RhymeResults.  Most words will have one RhymeResult.  Words with multiple possible
     * pronunciations (word variants) will have one RhymeResult per variant.
     */
    public List<RhymeResult> getRhymingWords(String word, int maxResults) {
        List<RhymeResult> results = new ArrayList<>();
        String lookupWord = word.toLowerCase(Locale.US);

        // The word doesn't exist in our dictionary
        List<WordVariant> wordVariants = getWordVariants(lookupWord);
        if (wordVariants == null) return results;

        // One RhymeResult per word variant (pronunciation)
        for (WordVariant wordVariant : wordVariants) {

            SortedSet<String> matches0 = getWordsWithLastStressSyllable(wordVariant.lastStressRhymingSyllables);
            SortedSet<String> matches1 = getWordsWithLastSyllable(wordVariant.lastRhymingSyllable);
            SortedSet<String> matches2 = new TreeSet<>();
            SortedSet<String> matches3 = new TreeSet<>();

            if (wordVariant.lastTwoRhymingSyllables != null) {
                matches2 = getWordsWithLastTwoSyllables(wordVariant.lastTwoRhymingSyllables);
                matches1.removeAll(matches2);
            }
            if (wordVariant.lastThreeRhymingSyllables != null) {
                matches3 = getWordsWithLastThreeSyllables(wordVariant.lastThreeRhymingSyllables);
                matches1.removeAll(matches3);
                matches2.removeAll(matches3);
            }

            matches0.remove(word);
            matches1.remove(word);
            matches2.remove(word);
            matches3.remove(word);

            matches1.removeAll(matches0);
            matches2.removeAll(matches0);
            matches3.removeAll(matches0);

            if (!matches0.isEmpty()
                    || !matches1.isEmpty()
                    || !matches2.isEmpty()
                    || !matches3.isEmpty()) {
                RhymeResult result = new RhymeResult(wordVariant.variantNumber,
                        toArray(matches0, maxResults),
                        toArray(matches1, maxResults),
                        toArray(matches2, maxResults),
                        toArray(matches3, maxResults));
                results.add(result);
            }
        }
        return results;
    }

    private static String[] toArray(Set<String> set, int limit) {
        // Some words, like "puppy", match way too many words.... any word
        // ending with an "ee" sound (IY0 phone).
        // TODO maybe there is a better way to solve this problem.

        if (limit < 0) return set.toArray(new String[set.size()]);
        String[] result = new String[Math.min(set.size(), limit)];
        int index = 0;
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext() && index < limit; index++) {
            result[index] = iterator.next();
        }
        return result;
    }

    protected abstract List<WordVariant> getWordVariants(String word);
    protected abstract SortedSet<String> getWordsWithLastStressSyllable(String lastStressSyllable);
    protected abstract SortedSet<String> getWordsWithLastSyllable(String lastSyllable);
    protected abstract SortedSet<String> getWordsWithLastTwoSyllables(String lastTwoSyllables);
    protected abstract SortedSet<String> getWordsWithLastThreeSyllables(String lastThreeSyllables);


}
