#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

for f in ./ul-accepted-valid/*.ul; do
    echo "Testing $f file... "
    echo "----------------------------------------------"
    java -cp "../antlr3.jar:../build" Compiler $f > $f.ir
    if [ $? -eq 0 ]
    then
        echo -e "${GREEN}PASS - No type errors detected in valid file${NC}"
    else
        echo -e "${RED}FAIL - Type error found in valid file or file not accepted${NC}"
    fi
    echo -e "----------------------------------------------\n"
done
