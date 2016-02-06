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


import ca.rmen.rhymer.cmu.CmuDictionary;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestRhymer {

    @Test
    public void testRhymes() throws IOException {
        Rhymer rhymer = CmuDictionary.loadRhymer();
        testShouldRhyme(rhymer, "recuperate", "redecorate", 2);
        testShouldRhyme(rhymer, "rhyme", "paradigm", 1);
        testShouldRhyme(rhymer, "low", "hello", 1);
    }

    @Test
    public void testNotRhymes() throws IOException {
        Rhymer rhymer = CmuDictionary.loadRhymer();
        testShouldntRhyme(rhymer, "puppy", "happy");
    }

    private void testShouldRhyme(Rhymer rhymer, String word1, String word2, int numberOfSyllables) {
        List<RhymeResult> results = rhymer.getRhymingWords(word1);
        Assert.assertTrue(results.size() == 1);
        RhymeResult result = results.get(0);
        String[] rhymes = numberOfSyllables == 1? result.oneSyllableRhymes : numberOfSyllables == 2 ? result.twoSyllableRhymes : result.threeSyllableRhymes;
        List<String> rhymingWords = Arrays.asList(rhymes);
        Assert.assertTrue(word1 + " should rhyme with " + word2, rhymingWords.contains(word2));
    }

    private void testShouldntRhyme(Rhymer rhymer, String word1, String word2) {
        List<RhymeResult> results = rhymer.getRhymingWords(word1);
        for (RhymeResult result : results) {
            List<String> rhymingWords = Arrays.asList(result.oneSyllableRhymes);
            Assert.assertFalse(word1 + " shouldn't match with " + word2, rhymingWords.contains(word2));
            rhymingWords = Arrays.asList(result.twoSyllableRhymes);
            Assert.assertFalse(word1 + " shouldn't match with " + word2, rhymingWords.contains(word2));
            rhymingWords = Arrays.asList(result.threeSyllableRhymes);
            Assert.assertFalse(word1 + " shouldn't match with " + word2, rhymingWords.contains(word2));
        }
    }
}
