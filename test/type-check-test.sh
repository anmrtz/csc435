#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

echo -e "\nTesting invalid .ul files for rejection\n"
for f in ./ul-accepted-valid/*.ul; do
    echo "Testing $f file... "
    echo "----------------------------------------------"
    java -cp "../antlr3.jar:../build" Compiler $f
    if [ $? -eq 0 ]
    then
        echo -e "${GREEN}PASS - No type errors detected in valid file${NC}"
    else
        echo -e "${RED}FAIL - Type error found in valid file or file not accepted${NC}"
    fi
    echo -e "----------------------------------------------\n"
done

echo -e "\nTesting invalid .ul files for rejection\n"
for f in ./ul-accepted-invalid/*.ul; do
    echo "Testing $f file..."
    echo "----------------------------------------------"
    java -cp "../antlr3.jar:../build" Compiler $f 
    if [ $? -ne 0 ]
    then
        echo -e "${GREEN}PASS - Type error successfully detected${NC}"
    else
        echo -e "${RED}FAIL - Type error not detected in invalid file${NC}"
    fi
    echo -e "----------------------------------------------\n"
done
