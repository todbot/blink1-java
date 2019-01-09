# Makefile for Blink(1) library for Java -- 
#  
# This makefile builds Java and native code for multiple OSes
# To build the multi-platform Processing library, it's assumed that
# this directory is shared between a Mac OS X, Windows, and Linux computers
#
# For base OS dependencies, see the blink1/commandline/Makefile
#
# Assumptions:
#
# Mac OS X is at least 10.6 (Snow Leopard)
#
# Windows is at least Windows XP with MinGW and MinSys installed
#
# Linux is Ubuntu 9 or above, with JDK installed (e.g. "sudo apt-get install openjdk-6-jdk")
#
# Attempts are made to make universal binary (i386 & x86_64) on Mac OS X
#
#


LIBUSBA_DIR = libusb-static
#PROCESSING_LIBDIR = blink1/library

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
# "HIDAPI" type is best for Mac, Windows, Linux Desktop, 
#  but has dependencies on iconv, libusb-1.0, pthread, dl
#
# "HIDDATA" type is best for low-resource Linux, 
#  and the only dependencies it has is libusb-0.1
#

USBLIB_TYPE = HIDAPI
#USBLIB_TYPE = HIDDATA

JAVA_VER=`java -version 2>&1 | grep version | cut -c 15-17`


#################  Mac OS X  ##################################################
ifeq "$(OS)" "macosx"
JAVA_HOME:=/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home
PATH:=$(JAVA_HOME)/bin:$(PATH)

LIBTARGET = libBlink1.jnilib

ifeq "$(USBLIB_TYPE)" "HIDAPI"
CFLAGS += -DUSE_HIDAPI
#CFLAGS += -arch i386 -arch x86_64
#CFLAGS += -pthread
CFLAGS += -I blink1-tool/hidapi/hidapi 
OBJS = blink1-tool/hidapi/mac/hid.o
endif

ifeq "$(USBLIB_TYPE)" "HIDDATA"
CFLAGS += -DUSE_HIDDATA
OBJS = blink1-tool/hiddata.o
OPT_HOME := /opt/local/bin
CFLAGS += `$(OPT_HOME)/libusb-config --cflags`
LIBS   += `$(OPT_HOME)/libusb-config --libs`
endif

#
JAVAINCLUDEDIR = /System/Library/Frameworks/JavaVM.framework/Headers
JAVANATINC = -I $(JAVAINCLUDEDIR)/./
JAVAINCLUDE = -I $(JAVAINCLUDEDIR)

OS_CFLAGS = -g -O2 -D_BSD_SOURCE -std=gnu99  $(USBFLAGS) 
#OS_CFLAGS += -isysroot /Developer/SDKs/MacOSX10.5.sdk -mmacosx-version-min=10.5
OS_LDFLAGS =  -bundle -Wl,-search_paths_first -framework JavaVM -framework IOKit -framework CoreFoundation $(USBLIBS) 
endif

#################  Windows  ##################################################
ifeq "$(OS)" "windows"
LIBTARGET = Blink1.dll
LIBUSBA   = libusb-windows.a
USBLIBS   = -lhid -lsetupapi

ifeq "$(USBLIB_TYPE)" "HIDAPI"
CFLAGS += -DUSE_HIDAPI
CFLAGS += -I blink1-tool/hidapi/hidapi 
OBJS = blink1-tool/hidapi/windows/hid.o
endif

ifeq "$(USBLIB_TYPE)" "HIDDATA"
CFLAGS += -DUSE_HIDDATA
OBJS = blink1-tool/hiddata.o
endif

ifndef JAVA_HOME
  ${error JAVA_HOME not set. Set it with 'export JAVA_HOME=<java dir>'. It is often C:\\Program Files\\Java\jdk-10.}
endif

PATH := $(JAVA_HOME)\\bin:$(PATH)

JAVA_NATIVE_INC = -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/win32"
OS_CFLAGS  = ${JAVA_NATIVE_INC}
OS_LDFLAGS = -s -shared -Wl,--export-all-symbols -Wl,--kill-a $(USBLIBS)

endif

#################  Linux  ###################################################
ifeq "$(OS)" "linux"
# ???
LIBTARGET = libBlink1.so
#USBFLAGS += `pkg-config libusb-1.0 --cflags`
#USBLIBS  += `pkg-config libusb-1.0 --libs` 
#OBJS = ../blink1-tool/hidapi/libusb/hid.o

ifeq "$(USBLIB_TYPE)" "HIDAPI"
CFLAGS += -DUSE_HIDAPI
CFLAGS += -I blink1-tool/hidapi/hidapi 
OBJS = blink1-tool/hidapi/libusb/hid.o
CFLAGS += `pkg-config libusb-1.0 --cflags` -fPIC
LIBS   += `pkg-config libusb-1.0 --libs` -lrt -lpthread -ldl
endif

ifeq "$(USBLIB_TYPE)" "HIDDATA"
CFLAGS += -DUSE_HIDDATA
OBJS = blink1-tool/hiddata.o
CFLAGS += `pkg-config libusb --cflags` -fPIC
LIBS   += `pkg-config libusb --libs` 
endif

# Possible values for JAVA_HOME, there are probably more
#JAVA_HOME=/usr/lib/jvm/java-6-openjdk
#JAVA_HOME=/usr/lib/jvm/java-6-sun
#JAVA_HOME=/usr/lib/jvm/default-java

ifndef JAVA_HOME
 ${error JAVA_HOME not set. Set it in the makefile or with 'export JAVA_HOME=<java dir>'. It is often in /usr/lib/jvm/default-java, so try 'export JAVA_HOME=/usr/lib/jvm/default-java'}
endif

# gotta find jni.h in here somewhere (is there a better way of doing this?)
OS_CFLAGS  = -shared -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux
OS_LDFLAGS = $(LIBS)
endif


# now construct normal env vars based on OS-specific ones
INCLUDES = -I. -I blink1-tool 
INCLUDES += $(JAVAINCLUDE) $(JAVANATINC) 

OBJS += blink1-tool/blink1-lib.o  nativeBlink1.o 

CFLAGS  += $(OS_CFLAGS) -O -Wall -std=gnu99  $(INCLUDES)
LDFLAGS += $(OS_LDFLAGS) 

#CC = gcc


all: help

help:
	@echo "This Makefile has no default rule. Use one of the following:"
	@echo "make prep ...... check out needed submodule"
	@echo "make javac ..... to build all java classes"
	@echo "make jni ....... to build JNI stubs"
	@echo "make compile ....to build the C code" 
	@echo "make jar ....... to build the jar <-- run this one"
	@echo "make processing. to build the processing library (for current arch)"
	@echo "make clean ..... to clean all built files"
	@echo "make javadoc ... to make the javadoc"

prep:
	git submodule update --init
	mkdir -p build

javac:
#	javac -target $(JAVA_VER) thingm/blink1/Blink1.java	
	javac thingm/blink1/Blink1.java	

jni:
	javac -h . thingm/blink1/Blink1.java

clean-blink1lib:
	make -C blink1-tool clean

# the "libprep" is to pull out the libusb.a into its own dir, for static build
#libprep:
#	mkdir -p $(LIBUSBA_DIR)
#	cp `libusb-config --exec-prefix`/lib/libusb.a $(LIBUSBA_DIR)/$(LIBUSBA)

#	$(CC)  -o $(LIBTARGET) $(CFLAGS) $(LDFLAGS) $(OBJ)  -lc
compile: msg clean-blink1lib javac jni $(OBJS)
	$(CC)  -o $(LIBTARGET) $(CFLAGS) $(OBJS) $(LDFLAGS) 
	mv $(LIBTARGET) build

.c.o:
	$(CC) $(ARCH_COMPILE) $(CFLAGS) -c $*.c -o $*.o

msg: 
	@echo "building for OS=$(OS)"

# the main one
jar: prep javac jni compile
	jar -cfm blink1.jar  packaging/Manifest.txt thingm/blink1/*.class
	mv blink1.jar build

processing: processinglib
processinglib: jar
	rm -f $(LIBZIPNAME)
	rm -rf blink1/library
	rm -rf blink1/examples
	mkdir -p blink1/library
	mkdir -p blink1/examples
	cp packaging/processing-export.txt blink1/library/export.txt
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
	cd ../docs/javadoc && javadoc -sourcepath ../../java thingm.blink1 && cd ../../java

clean: clean-blink1lib
	rm -f thingm/blink1/*.class 	
	rm -f libtargets/blink1.jar
	rm -f $(OBJS)
	rm -f libtargets/$(LIBTARGET)
	rm -f $(LIBTARGET) thingm_blink1_Blink1.h
	rm -f blink1jar $(LIBZIPNAME)
	rm -rf blink1

distclean: clean
	rm -rf blink1
	rm -rf libtargets





###########################################################################

## NOTES ##

#     -I /export/home/jdk1.2/include 
#     -I/export/home/jdk1.2/include/linux nativelib.c  
#     -static -lc

# see:
# http://macosx.com/forums/mac-os-x-system-mac-software/277799-problem-c-dynamic-library-jni.html
#
# for Linux
#
# In Linux the following options worked
# compiler:
#  gcc -Wall -O3 -c -I${JAVA_HOME}/include -I${JAVA_HOME}/include/win32
# linker:
#  gcc -s -shared

# for Windows
# In Windows the following options worked
# compiler:
#  gcc -Wall -O3 -c -I${JAVA_HOME}\include -I${JAVA_HOME}\include\win32
# linker:
#  gcc -s -shared -Wl,--export-all-symbols -Wl,--kill-a


# 
# os x: use "otool -L blah.jnilib" to find shared lib dependencies
# linux: use "ldd blah.so" to do the same
# windows:  ??? no clue, maybe "DLL Show" by Greg Braun (but only for running)


# 
# NOTE: to statically link libusb on Mac OS X, use the "-Wl,-search_paths_first"
# linker option and point to a directory that contains *only* the libusb.a file.
# The standard libusb libs contain a .dylib and .a, and the dylib will get 
# picked up first.
#
