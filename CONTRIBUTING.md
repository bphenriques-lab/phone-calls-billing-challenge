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

### Publish

This tool is not yet available on Nexus.

### Development Guide

The following sections briefly describe how to contribute to the project.

#### Configuration file

This project uses [Lightbend Config](https://github.com/lightbend/config) due to its flexibility and how easy it is to
read. For this purpose, I suggest reading [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md).

#### Adding more strategies to read call records

To create additional ways to read call records , extend the `trait CallRecordsFileSupplier` and implement the method:

```
def from(file: File): Try[Seq[CallRecord]]
```

**Note**: Currently, there is no support on changing the strategy through the configuration file.

#### Adding more billing methods

There are two extension points regarding billing:

* **Individual calls**: Use the trait `CallBillGenerator` and implement the following method:

    ```
    def createBill(value: CallRecord): Bill
    ```

* **Set of calls**: Use the trait `SeqCallsBillGenerator` and implement the following method:

    ```
    def createBill(value: Seq[CallRecord]): Bill    
    ```

`Bill` as the name suggests, groups the relevant billing information which is the duration of the call and the amount
that is going to billed.

**Note**: Currently, there is no support to change any of these strategy through the configuration file.

#### Feature Requests and bug reports

TBD.

#### Create merge requests

TBD.
