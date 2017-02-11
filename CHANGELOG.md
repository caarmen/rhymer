Change Log
==========

1.2.0  *(2017-02-11)*
--------------------
* Added optional `maxResults` parameter to `Rhymer.getRhymingWords()` to limit the number of results returned. By default, all results are returned, which is new behavior. Previously, "last-syllable" matches were ommitted completely if better rhymes were available (strict or "last-two-syllables").

1.1.2  *(2016-10-29)*
--------------------
* New behavior if there are too many "last-syllable" matches: Now we will omit all "last-syllable" matches if either "strict" or "last-two-syllable" matches are present.  Previously, we omitted all one-syllable matches if "last-two-syllable" matches were present.

1.1.1  *(2016-10-21)*
--------------------
* Filter out empty rhyme results.

1.1.0  *(2016-02-25)*
--------------------
* Added "strict" rhyming matches (words match from the last stressed syllable to the end of the word).

1.0.0  *(2016-02-14)*
--------------------
* First version.
