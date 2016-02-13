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

import java.io.Serializable;

/**
 * A result for a search for rhymes on a word.
 * This object holds the list of words which rhyme with one particular variant
 * of a word.
 */
public class RhymeResult implements Serializable {
    /**
     * The variant of the word.  In most cases, this is 0, when there is one pronunciation of the word.  Some words have
     * multiple pronunciations.  For example, the word TUESDAY has three variants: TUESDAY, TUESDAY(1) and TUESDAY(2).
     * This field indicates the specific variant for which we have lists of rhyming words.
     */
    public final int variantNumber;

    /**
     * Words which rhyme with the last syllable of our word variant.
     */
    public final String[] oneSyllableRhymes;

    /**
     * Words which rhyme with the last two syllables of our word variant.
     */
    public final String[] twoSyllableRhymes;

    /**
     * Words which rhyme with the last three syllables of our word variant.
     */
    public final String[] threeSyllableRhymes;

    public RhymeResult(int variantNumber, String[] oneSyllableRhymes, String[] twoSyllableRhymes, String[] threeSyllableRhymes) {
        this.variantNumber = variantNumber;
        this.oneSyllableRhymes = oneSyllableRhymes;
        this.twoSyllableRhymes = twoSyllableRhymes;
        this.threeSyllableRhymes = threeSyllableRhymes;
    }
}
