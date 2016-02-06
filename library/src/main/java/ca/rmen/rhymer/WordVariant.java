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

/**
 * Some words have multiple entries in the dictionary, for multiple pronunciations:
 * ex:
 * TUESDAY  T UW1 Z D IY0
 * TUESDAY(1)  T UW1 Z D EY2
 * TUESDAY(2)  T Y UW1 Z D EY2
 * This class indicates the variant number and the pronunciation of this variant.
 */
public class WordVariant {
    /**
     * The number of the variant.  For example, for TUESDAY, this would be 0.  For TUESDAY(1), this would be 1.
     */
    public final int variantNumber;

    /**
     * The list of phone symbols for this variant's pronunciation.
     */
    public final String[] symbols;

    public WordVariant(int variantNumber, String[] symbols) {
        this.variantNumber = variantNumber;
        this.symbols = symbols;
    }
}
