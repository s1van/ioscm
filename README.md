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
``ant -f build.xml dist``

#HowTo#
* xml file in ``conf/`` defines the type and parameters for each I/O stream (group) in the testing
* ``tool/ioscm-conf.py`` is a tool to specify parameters in configuration files
* scripts in ``tests/`` give detailed usage cases

#TraceReplayer#
A TraceReplayer takes an I/O trace file as input, and performs I/O accordingly. The format of an I/O trace file is
* Trace line format: "offset(bytes, sector)|size(bytes)|operation|wait_after_operation(seconds)"
* There're 4 types of operations: R(RandomAccessFile.read), r(blocking read), W(write), w(write and sync)

To launch multiple TraceReplayers at the same time, one can launch an I/O stream group called TraceReplayerBatch with the following configuration:
		
		<Stream label="TraceReplayerBatch" type="TraceReplayerBatch">
        	<period>4</period>
        	<dataDir>/expr/data</dataDir>
       		<traceDir>/expr/trace/WebSearch1.spc_s</traceDir>
       		<isBlock>true</isBlock>
		</Stream>

In the above case, ioscm will launch an I/O stream for each trace file within /expr/trace/WebSearch1.spc_s/. It is required that 
each trace file has a data file with the same file name inside /expr/data/ before the test starts. In addition, all streams will halt after 4 seconds, and the offset unit is block(512Bytes). For detailed usage case, please refer to 
	``tests/TraceReplayerBatchTest.sh``

#TraceReplayer7#

##Environment##
Set the home of JAVA 1.7 in reference to ``conf/ioscm-env.sh``

##Change from TraceReplayer##
TraceReplayer7 requires Java1.7 and offers a slightly different set of I/O operations
* R(asynchronous read), r(blocking read), W(asynchronous write), w(blocking write and sync)

##Configuration##
More options are included in its configuration file
		
		<Stream label="TraceReplayer7Batch" type="TraceReplayer7Batch">
        	<period>4</period>
        	<dataLocation>/dev/raw/raw1</dataLocation>
        	<dataIsDir>false</dataIsDir>
        	<traceDir>/home/siv/expr/trace/tiny/</traceDir>
        	<blockSize>1</blockSize>
        	<intervalScaleFactor>1</intervalScaleFactor>
        	<AIOPoolSize>128</AIOPoolSize>
        	<SyncMode>SYNC_ALL</SyncMode>
   		</Stream>

Here ``<traceDir>`` gives the path of the folder that contains all the I/O trace files. For each trace file, the replayer will then launch a thread for replaying. Besides, ``<dataLocation>`` specifies where to access the data, and ``<dataIsDir>`` indicates whether the specific locaiton contains one singel file, or a set of files. If false, all launched streams will access the same file concurrently; otherwise, for each trace found in ``<traceDir>``, there must be one data file using the same file name within ``<dataLocation>``.

In addition, two options are supplied to let the user slightly twist the I/O trace at runtime. ``<intervalScaleFactor>`` allow user to scale the ``wait_after_operation(seconds)`` in trace as needed. For instance, assigning a value 2 would simply double the waiting time. ``<SyncMode>`` is another handy option. Value ``SYNC_ALL`` change all opeations to synchrounous; and value ``ASYNC_ALL`` asynchrounous.

There is also one AsynchronousFileChannel specific option, ``<AIOPoolSize>``. The value of this tag specifies the size of the thread pool for asynchronous I/O.

##Raw Device Support##
Raw Device access is a way to bypass OS cache buffers for Java programs. To enable such functionality, the jar package needs to be launched with ``-Dsun.nio.PageAlignDirectMemory=true`` argument. More details can be found in script ``tests/TraceReplayer7Batch.sh``.

#Combine Different Stream Groups#
To corun different groups of streams, one only needs to put their group configurations together. ``conf/UnlimitedWriterReaderBatch.xml`` gives an example:
 	
 	<Stream type="UnlimitedWriterBatch" label='UnlimitedWriterBatch'>
        	<number>4</number>
        	<interval>0</interval>
        	<period>16</period>
        	<rsize>65536</rsize>
        	<path>/tmp/</path>
   	</Stream>
  	<Stream type="UnlimitedReaderBatch" label='UnlimitedReaderBatch'>
        	<number>4</number>
        	<interval>0</interval>
        	<period>16</period>
        	<rsize>65536</rsize>
        	<path>/tmp/</path>
   	</Stream>

Note that labels on different groups should be different.

#Log#
By default, **ioscm** dump logs to terminal at runtime. There are two types of messages. 
* Message on progress. It indicates the state of current I/O stream. It nomarmlly comes with a state message followed by the stream ID. Examples inlcude:
	
	``Sync.start	ID``
	
	``Sync.end	ID``

* Message for profiling. It has the format:
	
	``MeterTimer  tag  label  ID  start_time  end_time  duration  val1  val2  [op]``

#People#
* Developer: [Siyuan Ma](http://siyuan.biz)
