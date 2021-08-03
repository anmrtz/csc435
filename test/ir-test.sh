#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

(
    cd ./ul-accepted-valid;
for f in ./*.ul; do
    echo "Testing $f file... "
    echo "----------------------------------------------"
    b=${f%.*}
    java -cp "../../antlr3.jar:../../build" Compiler $f > $b.ir
    ../codegen --file=$b.ir > $b.ref.j
    jasmin $b.ref.j
    if [ $? -eq 0 ]
    then
        echo -e "${GREEN}PASS - No type errors detected in valid file${NC}"
    else
        echo -e "${RED}FAIL - Type error found in valid file or file not accepted${NC}"
    fi
    echo -e "----------------------------------------------\n"
done
)