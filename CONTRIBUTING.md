# Contributing Guide

This project is developed in Scala and [scala-sbt](https://www.scala-sbt.org/) as the build tool.

### Requirements

* Sbt 1.2.x.
* Scala 2.12.8.

### Installing

To install the project, unzip the folder `billing-0.1.0-SNAPSHOT.zip` at a folder of your selection. If you not have such
file you need to create [one](#packaging).

In the `README.md` you will find instructions on how to use the application.

### Compiling

The following command compiles the project:

```
$ make compile
```

### Run the linter

The following command runs the scala linter (see [scalastyle](http://www.scalastyle.org/)):

```
$ make lint
```

### Run Unit Tests

The following command runs the unit tests:

```
$ make test
```

### Packaging

The following command will generate a zip file in the `target/universal` folder that should be used to distribute this
application.

```
$ make package
```

Please follow the [installation guide](#installing) with instructions on how deploy the application.

