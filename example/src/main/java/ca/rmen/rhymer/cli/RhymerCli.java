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

import ca.rmen.rhymer.MemoryRhymer;
import ca.rmen.rhymer.RhymeResult;
import ca.rmen.rhymer.Rhymer;
import ca.rmen.rhymer.WordVariant;
import ca.rmen.rhymer.cmu.CmuDictionary;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RhymerCli {
    private static void usage() {
        System.out.println("Usages:");
        System.out.println("query <word>");
        System.out.println("exportdb </path/to/db/file>");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        if (args.length < 2) {
            usage();
        }
        String command = args[0];
        if ("query".equals(command)) {
            String word = args[args.length - 1];
            runQuery(word);

        } else if ("exportdb".equals(command)) {
            if (args.length != 2) usage();
            exportDb(new File(args[1]));

        } else {
            usage();
        }
    }

    private static void runQuery(String word) throws IOException {
        Rhymer rhymer = CmuDictionary.loadRhymer();
        List<RhymeResult> results = rhymer.getRhymingWords(word);
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

    private static void exportDb(File dbFile) throws IOException, ClassNotFoundException, SQLException {
        dbFile.delete();
        int batch_insert_size = 1000;
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        Statement createTableStatement = connection.createStatement();
        createTableStatement.execute("CREATE TABLE word_variants (word, variant_number, last_syllable, last_two_syllables, last_three_syllables)");
        createTableStatement.close();
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO word_variants (word, variant_number, last_syllable, last_two_syllables, last_three_syllables) VALUES (?, ?, ?, ?, ?)");
        MemoryRhymer rhymer = (MemoryRhymer) CmuDictionary.loadRhymer();
        Set<String> words = rhymer.getWords();
        int wordIndex = 0;
        int row = 0;
        for (String word : words) {
            List<WordVariant> wordVariants = rhymer.getWordVariants(word);
            for (WordVariant wordVariant : wordVariants) {
                int column = 1;
                insertStatement.setString(column++, word);
                insertStatement.setInt(column++, wordVariant.variantNumber);
                insertStatement.setString(column++, wordVariant.lastRhymingSyllable);
                insertStatement.setString(column++, wordVariant.lastTwoRhymingSyllables);
                insertStatement.setString(column++, wordVariant.lastThreeRhymingSyllables);
                insertStatement.addBatch();
                row++;
                if (row % batch_insert_size == 0) {
                    System.out.println("Executing batch...");
                    insertStatement.executeBatch();
                }
            }
            wordIndex++;
            if (wordIndex % 1000 == 0) System.out.println("Wrote word " + wordIndex + " of " + words.size());
        }
        insertStatement.executeBatch();
        insertStatement.close();
    }
}
