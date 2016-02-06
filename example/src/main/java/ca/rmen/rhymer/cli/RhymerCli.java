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
import ca.rmen.rhymer.Rhymer.RhymeResult;
import ca.rmen.rhymer.cmu.CmuDictionary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class RhymerCli {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String word = args[0];
        Rhymer rhymer = CmuDictionary.loadRhymer();
        List<RhymeResult> results = rhymer.getRhymingWords(word.toUpperCase());
        for (RhymeResult result : results) {
            System.out.println("Results for " + result.variant + ":");

            System.out.println("  One-syllable matches:");
            System.out.println("    " + Arrays.toString(result.oneSyllableRhymes));
            System.out.println();
            System.out.println("  Two-syllable matches:");
            System.out.println("    " + Arrays.toString(result.twoSyllableRhymes));
            System.out.println();
            System.out.println("  Three-syllable matches:");
            System.out.println("    " + Arrays.toString(result.threeSyllableRhymes));
        }
    }
}
