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


import ca.rmen.rhymer.cmu.TestCmuDictionaryReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class TestSyllableParser {

    /**
     * Make sure each word in the dictionary has at least one symbol and at least one syllable.
     */
    @Test
    public void testAllSyllables() throws IOException {
        Map<String, PhoneType> phones = TestCmuDictionaryReader.readPhones();
        Map<String, String[]> words = TestCmuDictionaryReader.readWords();
        Assert.assertNotNull(words);
        SyllableParser syllableParser = new SyllableParser(phones);
        for (String word : words.keySet()) {
            String[] symbols = words.get(word);
            Assert.assertNotNull(symbols);
            Assert.assertTrue("word " + word + " has no symbols", symbols.length >= 1);
            String[] syllables = syllableParser.extractRhymingSyllables(symbols);
            Assert.assertNotNull(syllables);
            Assert.assertTrue("word " + word + " has no syllables", syllables.length >= 1);
        }
    }

    /**
     * Test extraction of syllables for a few sequences of symbols.
     */
    @Test
    public void testSyllables() throws IOException {
        Map<String, PhoneType> symbolMap = TestCmuDictionaryReader.readPhones();
        SyllableParser syllableParser = new SyllableParser(symbolMap);
        // ASAT: AE1 Z AE0 T
        testSyllableExtraction(syllableParser, new String[]{"AE1", "Z", "AE0", "T"}, new String[]{"AEZ", "AET"});
        // CAT:  K AE1 T
        testSyllableExtraction(syllableParser, new String[]{"K", "AE1", "T"}, new String[]{"AET"});
        // KITCAT:  K IH1 T K AE2 T
        testSyllableExtraction(syllableParser, new String[]{"K", "IH1", "T", "AE2", "T"}, new String[]{"IHT", "AET"});
        // GRAPH  G R AE1 F
        testSyllableExtraction(syllableParser, new String[]{"G", "R", "AE1", "F"}, new String[]{"AEF"});
        // RECUPERATE  R IH0 K UW1 P ER0 EY2 T
        testSyllableExtraction(syllableParser, new String[]{"R", "IH0", "K", "UW1", "P", "ER0", "EY2", "T"}, new String[]{"IHK", "UWP", "ER", "EYT"});
        // REDECORATE  R IY0 D EH1 K ER0 EY2 T
        testSyllableExtraction(syllableParser, new String[]{"R", "IH0", "D", "EH1", "K", "ER0", "EY2", "T"}, new String[]{"IHD", "EHK", "ER", "EYT"});
    }

    private void testSyllableExtraction(SyllableParser syllableParser, String[] symbols, String[] expectedSyllables) {
        String[] actualSyllables = syllableParser.extractRhymingSyllables(symbols);
        Assert.assertNotNull(actualSyllables);
        Assert.assertArrayEquals("The symbols " + Arrays.toString(symbols) + " were split into syllables " + Arrays.toString(actualSyllables)
                + " instead of " + Arrays.toString(expectedSyllables), expectedSyllables, actualSyllables);
    }

}
