#
GNAME= ul
GSRC= $(GNAME).g

all: grammar compiler

grammar: $(GSRCS)
	java -cp "./antlr3.jar" org.antlr.Tool -fo ./src/ ./src/$(GSRC) 

compiler:
	javac -cp "./antlr3.jar" ./src/*.java ./src/*/*.java -d ./build

.PHONY: test

test:
#	(cd ./test; ./ul-test.sh)
#	(cd ./test; ./type-check-test.sh)
#	(cd ./test; ./ir-test.sh)
#	(cd ./test; ./j-test.sh)

clean:
	@-rm ./src/$(GNAME)*.java ./src/$(GNAME).tokens
	@-rm -r ./build
	@-rm -r ./src/output
	@-rm ./test/ul-accepted-valid/*.ir
	@-rm ./test/ul-accepted-valid/*.j	
	@-rm ./test/ul-accepted-valid/*.class
