EXPR="$HOME/expr/ioscm";

TESTSET="UW-TEST-L";
#./UW-TEST-L.sh "/tmp/$TESTSET" "$EXPR/${TESTSET}-Fusion-HDD-EXT"
#./UW-TEST-L.sh "/mnt/SSD/$TESTSET" "$EXPR/${TESTSET}-Fusion-SSD-EXT"
#./UW-TEST-L.sh "/mnt/Hybrid/$TESTSET" "$EXPR/${TESTSET}-Fusion-Hybrid-EXT"
#./UW-TEST-L.sh "/mnt/shm/$TESTSET" "$EXPR/${TESTSET}-Fusion-SHM-EXT"

TESTSET="UR-TEST-L";
#./UR-TEST-L.sh "/tmp/$TESTSET" "$EXPR/${TESTSET}-Fusion-HDD-EXT" 128 4
#./UR-TEST-L.sh "/mnt/SSD/$TESTSET" "$EXPR/${TESTSET}-Fusion-SSD-EXT" 512 1
#./UR-TEST-L.sh "/mnt/Hybrid/$TESTSET" "$EXPR/${TESTSET}-Fusion-Hybrid-EXT" 256 4
#./UR-TEST-L.sh "/mnt/shm/$TESTSET" "$EXPR/${TESTSET}-Fusion-SHM-EXT 2048 1

TESTSET="UWR-TEST-L";
./UWR-TEST-L.sh "/tmp/$TESTSET" "$EXPR/${TESTSET}-Fusion-HDD-EXT" 128 4
./UWR-TEST-L.sh "/mnt/SSD/$TESTSET" "$EXPR/${TESTSET}-Fusion-SSD-EXT" 512 1
./UWR-TEST-L.sh "/mnt/Hybrid/$TESTSET" "$EXPR/${TESTSET}-Fusion-Hybrid-EXT" 256 2
#./UWR-TEST-L.sh "/mnt/shm/$TESTSET" "$EXPR/${TESTSET}-Fusion-SHM-EXT 2048 1