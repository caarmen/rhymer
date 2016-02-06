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
import java.util.Map;

class SyllableParser {
    private final Map<String, PhoneType> symbolPhoneMap;

    public SyllableParser(Map<String, PhoneType> symbolPhoneMap) {
        this.symbolPhoneMap = symbolPhoneMap;
    }

    /**
     * Combine symbols into syllables, for use in rhyming matching.
     * The symbols with variations are reduced to the root symbol. For
     * example: AE1 and AE2, are reduced to the root symbol AE
     *
     * Example: For the word "KITCAT", the symbols are:  K IH1 T K AE2 T
     *
     * The syllables are KIT and CAT, but we're only interested in
     * IT and AT for rhyming, so this will return "IHT", "AET".
     *
     * @return an array of rhyming syllables for the given list of phone symbols.
     */
    public String[] extractRhymingSyllables(String[] symbols) {
        List<String> syllables = new ArrayList<>();

        String currentSyllable = "";
        for (String symbol : symbols) {
            String symbolRoot = symbol.replaceAll("[0-9]$", "");
            PhoneType phoneType = symbolPhoneMap.get(symbolRoot);
            if (phoneType == PhoneType.VOWEL) {
                if (!currentSyllable.isEmpty()) {
                    syllables.add(currentSyllable);
                    currentSyllable = "";
                }
            } else if (currentSyllable.isEmpty()) {
                continue;
            }
            currentSyllable = currentSyllable + symbolRoot;
        }
        if (!currentSyllable.isEmpty()) syllables.add(currentSyllable);
        return syllables.toArray(new String[syllables.size()]);
    }
}
