#Introduction#
I/O Stream Congestion Meter is a benchmark to measure the effect of multiple concurrent I/O streams on one storage system. Each I/O stream in the benchmark is a squence of interleaving I/O operations and fake computation (Idle). It is part of an effort to understand the performance degradation (throughput, skew, fairness, ...) caused by I/O congestion for file systems like HDFS, PVFS, and ext4. Its features include:

* Computation-I/O pattern per I/O stream group
* Fast synchronization for small I/O streams
* Supports hundreds of concurrent read/write I/O streams

#An Example#
<figure>
  <img src="https://lh5.googleusercontent.com/-9zsrK9hxXvE/T_XGqTgektI/AAAAAAAAAA4/hSbJwuRyL-c/s1024/ioscm_intro.png" title="A Case of I/O Congestion" height="331" width="480" />
  
  <br><figcaption><b>Figure 1</b> A Case of I/O Congestion</figcaption>
</figure>

#Build#
``ant -f build.xml jar``

#HowTo#
* xml file in ``conf/`` defines the type and parameters for each I/O stream (group) in the testing
* ``tool/ioscm-conf.py`` is a tool to specify parameters in configuration files
* scripts in ``tests/`` give detailed usage cases

#TraceReplayer#
A TraceReplayer takes an I/O trace file as input, and performs I/O accordingly. The format of an I/O trace file is
* Trace line format: "offset(bytes), size(bytes), operation, wait_after_operation(seconds)"
* There're 4 types of operations: R(RandomAccessFile.read), r(block read), W(write), w(write and sync)

To launch multiple TraceReplayers at the same time, one can launch an I/O stream group called TraceReplayerBatch with the following configuration:
		
		<Stream label="TraceReplayerBatch" type="TraceReplayerBatch">
        	<period>4</period>
        	<dataDir>/expr/data</dataDir>
        	<traceDir>/expr/trace/WebSearch1.spc_s</traceDir>
		</Stream>

In the above case, ioscm will launch an I/O stream for each trace file within /expr/trace/WebSearch1.spc_s/. It is required that 
each trace file has a data file with the same file name inside /expr/data/ before the test starts. In addition, all streams will halt after 4 seconds. For detailed usage case, please refer to 
	``tests/TraceReplayerBatchTest.sh``

#People#
* Developer: [Siyuan Ma](http://siyuan.biz)
