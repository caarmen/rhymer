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
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class TestCmuDictionaryReader {

    private static final String WORDS_FILE = "src/main/resources/dictionary_files/cmudict-0.7b";
    private static final String PHONES_FILE = "src/main/resources/dictionary_files/cmudict-0.7b.phones";

    public static Map<String, PhoneType> readPhones() throws IOException {
        return CmuDictionaryReader.readPhones(new FileInputStream(PHONES_FILE));
    }

    public static Map<String, String[]> readWords() throws IOException {
        return CmuDictionaryReader.readWords(new FileInputStream(WORDS_FILE));
    }

    /**
     * Test that we load all the phone symbols, and spot check the phone type for a couple of them.
     */
    @Test
    public void testLoadPhones() throws IOException {
        Map<String, PhoneType> phones = readPhones();
        Assert.assertNotNull(phones);
        Assert.assertEquals(39, phones.size());
        PhoneType phoneType = phones.get("IY");
        Assert.assertEquals(PhoneType.VOWEL, phoneType);
        phoneType = phones.get("G");
        Assert.assertEquals(PhoneType.STOP, phoneType);
    }

    /**
     * Test that we correctly load the symbols for some words.
     */
    @Test
    public void testLoadWords() throws IOException {
        Map<String, String[]> words = readWords();
        Assert.assertNotNull(words);
        Assert.assertEquals(133854, words.size());
        testWordSymbols("ZYNDA", new String[]{"Z", "IH1", "N", "D", "AH0"}, words);
        testWordSymbols("ZYMAN", new String[]{"Z", "AY1", "M", "AH0", "N"}, words);
    }

    private void testWordSymbols(String word, String[] expectedSymbols, Map<String, String[]> dict) {
        String[] actualSymbols = dict.get(word);
        Assert.assertNotNull(actualSymbols);
        Assert.assertEquals("Expected and actual symbols list are not the same size for word " + word, expectedSymbols.length, actualSymbols.length);
        Assert.assertArrayEquals("Error parsing word " + word, expectedSymbols, actualSymbols);
    }

}
