Rhymer
======

This library provides a rhyming dictionary based on the the Carnegie Mellon Pronouncing Dictionary.

To test out the library on the command-line:

```
    ./gradlew clean cliJar
    java -jar example/build/libs/example-all-1.0-SNAPSHOT.jar  librhymer/src/main/resources/cmudict-0.7b.phones librhymer/src/main/resources/cmudict-0.7b <word to rhyme>
```

This brand new library is not yet available on a remote maven repository.

If you want to build the library to include it in your project:

To build the library:

```
./gradlew clean build
```


This will create the file `./librhymer/build/libs/librhymer-<version>.jar` which you can include in your project.

To deploy the jar to your local maven repository:

```
    ./gradlew clean install
```

Then include this maven dependency:

```
    <dependency>
      <groupId>ca.rmen</groupId>
      <artifactId>librhymer</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

Or gradle dependency:

```
    compile 'ca.rmen:librhymer:1.0-SNAPSHOT'
```
