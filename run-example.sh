#! /bin/bash

# exec:java causes sigsegv's on mac with jna

java \
-cp blink1-library/target/blink1-library-jar-with-dependencies.jar:./blink1-java-examples/target/blink1-java-examples.jar \
com.thingm.blink1.$1 $2 $3 $4
