#!/bin/sh
#
# you must type "make jar" before this script will work
#

BUILD_PATH=build

export PATH=${PATH}:${JAVA_HOME}/bin

if [ -e ${BUILD_PATH}/blink1.jar ]; then
    java -Djava.awt.headless=true -Djava.library.path=${BUILD_PATH} -jar ${BUILD_PATH}/blink1.jar $*
    #java -Djava.awt.headless=true  -jar blink1.jar $*
    #java -d32 -Djava.library.path=libtargets -jar libtargets/blink1.jar $*
else 
    echo "cannot run. make the jar with 'make jar' please"
fi
