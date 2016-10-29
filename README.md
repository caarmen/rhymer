Rhymer
======

This library provides a rhyming dictionary based on the the Carnegie Mellon Pronouncing Dictionary.

To test out the library on the command-line:

```
    ./gradlew clean cliJar
    java -jar example/build/libs/example-all-1.1.2.jar <word to rhyme>
```

The library is available on jcenter. To include it in your project:

maven:

```
<dependency>
  <groupId>ca.rmen</groupId>
  <artifactId>rhymer</artifactId>
  <version>1.1.2</version>
</dependency>
```

gradle:

```
compile 'ca.rmen:rhymer:1.1.2'
```


