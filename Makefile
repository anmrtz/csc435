#
GNAME= ul
GSRC= $(GNAME).g

all: grammar compiler

grammar: $(GSRCS)
	java org.antlr.Tool -fo ./src/ ./src/$(GSRC) 

compiler:
	javac ./src/*.java -d ./build

clean:
	rm ./src/$(GNAME)*.java ./src/$(GNAME).tokens
	rm -r ./build
