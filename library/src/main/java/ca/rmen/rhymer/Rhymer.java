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
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Rhymer {
    private static final int THRESHOLD_TOO_MANY_RHYMES = 500;

    /**
     * @return a list of RhymeResults.  Most words will have one RhymeResult.  Words with multiple possible
     * pronunciations (word variants) will have one RhymeResult per variant.
     */
    public List<RhymeResult> getRhymingWords(String word) {
        List<RhymeResult> results = new ArrayList<>();
        String lookupWord = word.toLowerCase(Locale.US);

        // The word doesn't exist in our dictionary
        List<WordVariant> wordVariants = getWordVariants(lookupWord);
        if (wordVariants == null) return results;

        // One RhymeResult per word variant (pronunciation)
        for (WordVariant wordVariant : wordVariants) {

            Set<String> matches1 = getWordsWithLastSyllable(wordVariant.lastRhymingSyllable);
            Set<String> matches2 = new TreeSet<>();
            Set<String> matches3 = new TreeSet<>();

            if (wordVariant.lastTwoRhymingSyllables != null) {
                matches2 = getWordsWithLastTwoSyllables(wordVariant.lastTwoRhymingSyllables);
                matches1.removeAll(matches2);
            }
            if (wordVariant.lastThreeRhymingSyllables != null) {
                matches3 = getWordsWithLastThreeSyllables(wordVariant.lastThreeRhymingSyllables);
                matches1.removeAll(matches3);
                matches2.removeAll(matches3);
            }

            matches1.remove(word);
            matches2.remove(word);
            matches3.remove(word);

            // Some words, like "puppy", match way too many words.... any word
            // ending with an "ee" sound (IY0 phone).  If we end up in this situation,
            // completely ignore all the one-syllable matches, and only return
            // 2 and 3 syllable matches.
            // TODO maybe there is a better way to solve this problem.
            if (matches1.size() > THRESHOLD_TOO_MANY_RHYMES && matches2.size() > 0) {
                matches1.clear();
            }

            RhymeResult result = new RhymeResult(wordVariant.variantNumber,
                    matches1.toArray(new String[matches1.size()]),
                    matches2.toArray(new String[matches2.size()]),
                    matches3.toArray(new String[matches3.size()]));
            results.add(result);
        }
        return results;
    }

    protected abstract List<WordVariant> getWordVariants(String word);
    protected abstract SortedSet<String> getWordsWithLastSyllable(String lastSyllable);
    protected abstract SortedSet<String> getWordsWithLastTwoSyllables(String lastTwoSyllables);
    protected abstract SortedSet<String> getWordsWithLastThreeSyllables(String lastThreeSyllables);


}
