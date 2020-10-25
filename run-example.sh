#! /bin/bash

# exec:java causes sigsegv's on mac with jna
# mvn exec:java -pl blink1-examples -Dexec.mainClass="com.thingm.blink1.$1" -Dexec.cleanupDaemonThreads=false

java \
-cp blink1-library/target/blink1-library-jar-with-dependencies.jar:./blink1-examples/target/blink1-examples.jar \
com.thingm.blink1.$1
