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


import ca.rmen.rhymer.cmu.CmuDictionary;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class TestRhymer {

    @Test
    public void testRhymesRecuperate() throws IOException, URISyntaxException {
        Rhymer rhymer = CmuDictionary.loadRhymer();
        String[][] rhymes = rhymer.getRhymingWords("recuperate");
        Assert.assertEquals(3, rhymes.length);
        List<String> rhymes2Syllables = Arrays.asList(rhymes[1]);
        Assert.assertTrue(rhymes2Syllables.contains("REDECORATE"));
    }

}
