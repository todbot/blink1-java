# Makefile for Blink(1) library for Java -- 
#  
#
#


PROCESSINGZIPNAME = blink1-java-processing.zip

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
CLASSPATH=$(JARS):src/main/java

endif

JAVA_ARGS=-cp $(CLASSPATH)
JAVAC_ARGS=$(JAVA_ARGS) -source $(JAVA_VER) -target $(JAVA_VER)

EXAMPLE=Example1

all: help

help:
	@echo "This Makefile has no default rule. Use one of the following:"
	@echo "make javac ..... to compile the Java"
	@echo "make jar ....... to build the jar"
	@echo "make example.... to build the examples"
	@echo "make processing. to build the processing library"
	@echo "make clean ..... to clean all built files"
	@echo "make javadoc ... to make the javadoc"

javac:
	javac $(JAVAC_ARGS) src/main/java/com/thingm/blink1/*java	

# the main one
jar: javac
	jar -cfm lib/blink1.jar packaging/Manifest.txt -C src/main/java com/thingm/blink1

examples: jar
	javac $(JAVAC_ARGS) examples/*.java

run-example:
	java -cp $(JARS):lib/blink1.jar:examples $(EXAMPLE)


msg: 
	@echo "building for OS=$(OS)"


processing: processinglib
processinglib: jar
	rm -f $(PROCESSINGZIPNAME)
	rm -rf processing
	mkdir -p processing/blink1/library
	mkdir -p processing/blink1/examples
	cp packaging/library.properties processing/blink1
	cp -r lib/* processing/blink1/library
	cp -r examples-processing/* processing/blink1/examples
	zip --exclude \*application.\* --exclude \*~ --exclude .DS_Store --exclude \*zip -r $(PROCESSINGZIPNAME) blink1
	@echo
	@echo "now unzip $(PROCESSINGZIPNAME) into ~/Documents/Processing3/libraries"
#	@echo "or maybe ln -s \`pwd\`/ ~/Documents/Processing/libraries/blink1"


javadoc:
#	cd doc && javadoc -sourcepath .. thingm.blink1 && cd ..
#	mkdir -p ../docs/javadoc
	cd ./docs/javadoc && javadoc -sourcepath ../../java thingm.blink1 && cd ../../java

clean:
	-rm lib/blink1.jar
	-rm src/main/java/com/thingm/blink1/*.class
	-rm examples/*.class

