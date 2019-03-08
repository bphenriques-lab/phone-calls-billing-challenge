.PHONY: all clean compile lint test package

all: clean compile lint test package

# Cleans the project.
clean:
	sbt clean

# Compiles the project
compile:
	sbt compile

# Runs Scala linter.
lint:
	sbt scalastyle

# Unit tests.
test:
	sbt test

# Generates a distributable ZIP file under target/universal/*.zip
package: clean
	sbt universal:packageBin

