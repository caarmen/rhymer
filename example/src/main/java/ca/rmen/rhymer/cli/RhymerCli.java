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

package ca.rmen.rhymer.cli;

import ca.rmen.rhymer.Rhymer;
import ca.rmen.rhymer.cmu.CmuDictionary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class RhymerCli {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String word = args[0];
        Rhymer rhymer = CmuDictionary.loadRhymer();
        String[][] rhymingWords = rhymer.getRhymingWords(word.toUpperCase());

        System.out.println("One-syllable matches:");
        System.out.println(Arrays.toString(rhymingWords[0]));
        System.out.println("Two-syllable matches:");
        System.out.println(Arrays.toString(rhymingWords[1]));
        System.out.println("Three-syllable matches:");
        System.out.println(Arrays.toString(rhymingWords[2]));
    }
}
