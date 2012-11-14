#!/bin/bash

RES="$HOME/expr/ioscm";
DATA="$HOME/expr/data";

TESTDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";

TESTSET="RR-TEST-L";
$TESTDIR/SSD-RR.sh "$DATA" "$RES/${TESTSET}-SSD" 2048 4;
#./UR-TEST-L.sh "/mnt/SSD/$TESTSET" "$EXPR/${TESTSET}-Fusion-SSD-EXT" 512 1
#./UR-TEST-L.sh "/mnt/Hybrid/$TESTSET" "$EXPR/${TESTSET}-Fusion-Hybrid-EXT" 256 4
#./UR-TEST-L.sh "/mnt/shm/$TESTSET" "$EXPR/${TESTSET}-Fusion-SHM-EXT 2048 1
