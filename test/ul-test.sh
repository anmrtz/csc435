#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

echo "Testing valid .ul files for acceptance"
for f in ./ul-accepted-valid/*.ul; do
    echo "Testing $f file... "
    java -cp "../antlr3.jar:../build" Compiler $f
    if [ $? -eq 0 ]
    then
        echo -e "${GREEN}PASS${NC}"
    else
        echo -e "${RED}FAIL${NC}"
    fi
done

echo -e "\nTesting invalid .ul files for rejection"
for f in ./ul-rejected/*.ul; do
    echo -n "Testing $f file... "
    java -cp "../antlr3.jar:../build" Compiler $f 
    if [ $? -ne 0 ]
    then
        echo -e "${GREEN}PASS${NC}"
    else
        echo -e "${RED}FAIL${NC}"
    fi
done
