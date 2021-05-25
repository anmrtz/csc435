#
GNAME= ul
GSRC= $(GNAME).g

all: grammar compiler test

grammar: $(GSRCS)
	java org.antlr.Tool -fo ./src/ ./src/$(GSRC) 

compiler:
	javac ./src/*.java -d ./build

.PHONY: test

test:
	cd ./test; ./ul-test.sh

clean:
	rm ./src/$(GNAME)*.java ./src/$(GNAME).tokens
	rm -r ./build
