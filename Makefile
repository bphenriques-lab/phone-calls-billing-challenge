.PHONY: all clean lint package test

all: clean test package

# Cleans the project.
clean:
	sbt clean

# Runs Scala linter.
lint:
	sbt scalastyle

# Generates a distributable ZIP file under target/universal/*.zip
package:
	sbt universal:packageBin

# Unit tests.
test: clean
	sbt test
