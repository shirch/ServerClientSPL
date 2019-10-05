CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread

# All Targets
all: BBclient

# Tool invocations
# Executable "make" depends on the files make.o and make.o.
BBclient: bin/connectionHandler.o bin/BBclient.o
	@echo 'Building target: BBclient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/BBclient bin/connectionHandler.o bin/BBclient.o $(LDFLAGS)
	@echo 'Finished building target: BBclient'
	@echo ' '

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BBclient.o: src/BBclient.cpp
	g++ $(CFLAGS) -o bin/BBclient.o src/BBclient.cpp

.PHONY: clean
clean:
	rm -f bin/*
	