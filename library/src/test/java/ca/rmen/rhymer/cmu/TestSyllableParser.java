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
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestSyllableParser {

    /**
     * Make sure each word in the dictionary has at least least one rhyming syllable.
     */
    @Test
    public void testAllSyllables() throws IOException {
        Map<String, PhoneType> phones = TestCmuDictionaryReader.readPhones();
        Map<String, List<WordVariant>> words = TestCmuDictionaryReader.readWords();
        Assert.assertNotNull(words);
        SyllableParser syllableParser = new SyllableParser(phones);
        for (String word : words.keySet()) {
            List<WordVariant> wordVariants = words.get(word);
            for(WordVariant wordVariant : wordVariants) {
                Assert.assertNotNull(wordVariant.lastRhymingSyllable);
            }
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
        // TELEMEDICINE T EH2 L IH0 M EH1 D IH0 S AH0 N
        testStressSyllableExtraction(syllableParser, new String[]{"T", "EH2", "L", "IH0", "M", "EH1", "D", "IH0", "S", "AH0", "N"}, "EHDIHSAHN");
        // ABSTRACTION  AE0 B S T R AE1 K SH AH0 N
        testStressSyllableExtraction(syllableParser, new String[]{"AE0", "B", "S", "T", "R", "AE1", "K", "SH", "AH0", "N"}, "AEKSHAHN");
        // HI  HH AY1
        testStressSyllableExtraction(syllableParser, new String[]{"HH", "AY1"}, "AY");
        // OH  OW1
        testStressSyllableExtraction(syllableParser, new String[]{"OW1"}, "OW");
    }

    private void testSyllableExtraction(SyllableParser syllableParser, String[] symbols, String[] expectedSyllables) {
        String[] actualSyllables = syllableParser.extractRhymingSyllables(symbols);
        Assert.assertNotNull(actualSyllables);
        Assert.assertArrayEquals("The symbols " + Arrays.toString(symbols) + " were split into syllables " + Arrays.toString(actualSyllables)
                + " instead of " + Arrays.toString(expectedSyllables), expectedSyllables, actualSyllables);
    }

    private void testStressSyllableExtraction(SyllableParser syllableParser, String[] symbols, String expectedSyllables) {
        String actualSyllables = syllableParser.extractStressRhymingSyllables(symbols);
        Assert.assertNotNull(actualSyllables);
        Assert.assertEquals("The symbols " + Arrays.toString(symbols) + " were split into syllables " + actualSyllables
                + " instead of " + expectedSyllables, expectedSyllables, actualSyllables);
    }

}
