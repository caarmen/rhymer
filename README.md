Rhymer
======

This library provides a rhyming dictionary based on the the Carnegie Mellon Pronouncing Dictionary.

To test out the library on the command-line:

```
    ./gradlew clean cliJar
    java -jar example/build/libs/example-all-1.0-SNAPSHOT.jar <word to rhyme>
```

If you want to build the library to include the library in your project:
*Note*, the library is not yet available on jcenter.  In the meantime, add this repository to your build files:
`"https://dl.bintray.com/caarmen/maven/"`

For gradle:

```
    maven {
        url "https://dl.bintray.com/caarmen/maven/"
    }
```

Then add the dependency:

maven:

```
<dependency>
  <groupId>ca.rmen</groupId>
  <artifactId>rhymer</artifactId>
  <version>1.0.0</version>
</dependency>
```

gradle:

```
compile 'ca.rmen:rhymer:1.0.0'
```


