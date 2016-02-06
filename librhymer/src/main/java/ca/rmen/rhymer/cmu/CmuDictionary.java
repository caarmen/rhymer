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
import ca.rmen.rhymer.Rhymer;
import ca.rmen.rhymer.WordVariant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CmuDictionary {

    private static final String VERSION = "cmudict-0.7b";
    private static final String ROOT_FOLDER = "/dictionary_files/";
    private static final String WORDS_FILE = ROOT_FOLDER + VERSION;
    private static final String PHONES_FILE = ROOT_FOLDER + VERSION + ".phones";
    private CmuDictionary() {
    }

    /**
     * Build a rhymer based on the CMU dictionary files.
     * @throws IOException
     */
    public static Rhymer loadRhymer() throws IOException {
        InputStream phonesFile = CmuDictionary.class.getResourceAsStream(PHONES_FILE);
        InputStream wordsFile = CmuDictionary.class.getResourceAsStream(WORDS_FILE);
        Rhymer rhymer = new Rhymer();
        Map<String, PhoneType> symbolsMap = CmuDictionaryReader.readPhones(phonesFile);
        Map<String, List<WordVariant>> wordsMap = CmuDictionaryReader.readWords(wordsFile);
        rhymer.buildIndex(symbolsMap, wordsMap);
        return rhymer;
    }
}

