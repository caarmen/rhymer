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

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CmuDictionary {

    private CmuDictionary() {
    }

    /**
     * Build a rhymer based on the CMU dictionary files.
     * @param symbolsFile the location of the CMU file mapping phone symbols to phone types.
     * @param wordsFile the location of the CMU file mapping English words to symbols.
     * @throws IOException
     */
    public static Rhymer loadRhymer(File symbolsFile, File wordsFile) throws IOException {
        Rhymer rhymer = new Rhymer();
        Map<String, PhoneType> symbolsMap = CmuDictionaryReader.readPhones(symbolsFile);
        Map<String, String[]> wordsMap = CmuDictionaryReader.readWords(wordsFile);
        rhymer.buildIndex(symbolsMap, wordsMap);
        return rhymer;
    }
}

