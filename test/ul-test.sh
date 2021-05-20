#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

echo "Testing valid .ul files for acceptance"
for f in ./ul_accept/*.ul; do
    echo "Testing $f file... "
    java -cp "/usr/share/java/antlr3-runtime.jar:../build" Compiler $f
    if [ $? -eq 0 ]
    then
        echo -e "${GREEN}PASS${NC}"
    else
        echo -e "${RED}FAIL${NC}"
    fi
done

echo -e "\nTesting invalid .ul files for rejection"
for f in ./ul_reject/*.ul; do
    echo -n "Testing $f file... "
    java -cp "/usr/share/java/antlr3-runtime.jar:../build" Compiler $f 
    if [ $? -ne 0 ]
    then
        echo -e "${GREEN}PASS${NC}"
    else
        echo -e "${RED}FAIL${NC}"
    fi
done
