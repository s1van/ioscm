#!/bin/bash

EXPRESS_HOME=$(dirname $0)/../

find ./ -iname '*.sh'| xargs dos2unix;
find ./ -iname '*.py'| xargs dos2unix;
find ./ -iname '*.java'| xargs dos2unix;
