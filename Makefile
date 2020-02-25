# Makefile for Blink(1) library for Java -- 
#  
#
#


LIBZIPNAME = blink1-java-processing-lib.zip

# try to do some autodetecting
UNAME := $(shell uname -s)

ifeq "$(UNAME)" "Darwin"
	OS=macosx
endif

ifeq "$(OS)" "Windows_NT"
	OS=windows
endif

ifeq "$(UNAME)" "Linux"
	OS=linux
endif


# pick low-level implemenation style

#JAVA_VER=`java -version 2>&1 | grep version | cut -c 15-17`
JAVA_VER=1.8

#################  Mac OS X  ##################################################
ifeq "$(OS)" "macosx"

JARS=./lib/jna-5.5.0.jar:./lib/hid4java.jar
CLASSPATH=$(JARS):examples:src/main/java

endif

#################  Windows  ##################################################
ifeq "$(OS)" "windows"

#JARS=.\lib\purejavahidapi.jar;.\lib\jna-5.5.0.jar
CLASSPATH+="$(JARS);.\lib\jna-platform-5.5.0.jar;src/main/java"

endif

#################  Linux  ###################################################
ifeq "$(OS)" "linux"

#JARS=./lib/jna-5.5.0.jar:./lib/purejavahidapi.jar
CLASSPATH=$(JARS):examples:src/main/java

endif

JAVA_ARGS=-cp $(CLASSPATH)
JAVAC_ARGS=$(JAVA_ARGS) -source $(JAVA_VER) -target $(JAVA_VER)

all: help

help:
	@echo "This Makefile has no default rule. Use one of the following:"
	@echo "make javac ..... to compile the Java"
	@echo "make jar ....... to build the jar"
	@echo "make processing. to build the processing library"
	@echo "make clean ..... to clean all built files"
	@echo "make javadoc ... to make the javadoc"

# the main one
jar: javac
	jar -cfm blink1.jar packaging/Manifest.txt -C src/main/java com/thingm/blink1

javac:
	javac $(JAVAC_ARGS) src/main/java/com/thingm/blink1/*java	

example:
	javac $(JAVAC_ARGS) examples/Example1.java

run-example0: examples
	java -cp blink1.jar:$(JARS):examples Example0

run-example1: examples
	java -cp blink1.jar:$(JARS):examples Example1

run-example2: examples
	java -cp blink1.jar:$(JARS):examples Example2

msg: 
	@echo "building for OS=$(OS)"


processing: processinglib
processinglib: jar
	rm -f $(LIBZIPNAME)
	rm -rf blink1/library
	rm -rf blink1/examples
	mkdir -p blink1/library
	mkdir -p blink1/examples
	cp packaging/processing-export.txt blink1/library/export.txt
	cp packageing/library.properties blink1
	cp -r build/* blink1/library
	rm -rf blink1/library/html
	cp -r processing/* blink1/examples
	zip --exclude \*application.\* --exclude \*~ --exclude .DS_Store --exclude \*zip -r $(LIBZIPNAME) blink1
	cp $(LIBZIPNAME) build
	@echo
	@echo "now unzip $(LIBZIPNAME) into ~/Documents/Processing/libraries"
#	@echo "or maybe ln -s \`pwd\`/ ~/Documents/Processing/libraries/blink1"


javadoc:
#	cd doc && javadoc -sourcepath .. thingm.blink1 && cd ..
#	mkdir -p ../docs/javadoc
	cd ./docs/javadoc && javadoc -sourcepath ../../java thingm.blink1 && cd ../../java

clean:
	-rm blink1.jar
	-rm src/main/java/com/thingm/blink1/*.class
	-rm examples/*.class

