# Getting Started with Kafka Streams

> Frameworks that use micro-batching are often optimized for greater throughput at the cost of higher latency. In Kafka Streams, you can achieve extremely low latency while also maintaining high throughput by splitting data across many partitions.

## Processor Topologies
Three basic kinds of processors in Kafka Streams:
1. Source processors - Sources are where information flows into the Kafka Streams application. Data is read from a Kafka topic and sent to 
one or more stream processors.
2. Stream Processors - These processors are responsible for applying data processing/transformation logic on the input stream. In the 
high-level DSL, these processors are defined using a set of built-in operators that are exposed by the Kafka Streams library, which we 
will be going over in detail in the following chapters. Some example operators are filter, map, flatMap, and join.
3. Sink Processors - Sinks are where enriched, transformed, filtered, or otherwise processed records are written back to Kafka, either to 
be handled by another stream processing application or to be sent to a downstream data store via something like Kafka Connect. Like 
source processors, sink processors are connected to a Kafka topic.

Collection of processors form Processor Topology

### Sub topologies
if our application needs to consume from multiple source topics, then Kafka Streams will (under most circumstances15) divide our topology 
into smaller sub-topologies to parallelize the work even further. This division of work is possible since operations on one input stream 
can be executed independently of operations on another input stream.

### Depth first processing
Kafka Streams uses a depth-first strategy when processing data. When a new record is received, it is routed through each stream processor 
in the topology before another record is processed.

> Only 1 record goes through the topology at once

**Task** - is the smallest unit of work that can be performed in parallel in a Kafka Streams application. We can calculate 
the number of tasks that can be created for a given Kafka Streams sub-topology : *max(source_topic_1_partitions, ... source_topic_n_partitions)*
tasks are just logical units that are used to instantiate and run a processor topology.

**Threads** - are what actually execute the task. They are designed to be thread safe. No. of threads can be configured 
by using *num.stream.threads = 4* property

Refer figure 'kafka streams - task running in 2 threads'

## High Level DSL Vs Processor Api
“Processor Topologies”, designing a processor topology involves specifying a set of source and sink processors, which 
correspond to the topics your application will read from and write to. However, instead of working with Kafka topics 
directly, the Kafka Streams DSL allows you to work with different representations of a topic, each of which are suitable 
for different use cases. There are two ways to model the data in your Kafka topics: as a stream (also called a record stream) 
or a table (also known as a changelog stream)

**Streams** - These can be thought of as inserts in database parlance. Each distinct record remains in this view of the log.
**Tables** - Tables can be thought of as updates to a database. In this view of the logs, only the current state 
(either the latest record for a given key or some kind of aggregation) for each key is retained. Tables are usually built 
from compacted topics (i.e., topics that are configured with a cleanup.policy of compact, which tells Kafka that you only 
want to keep the latest representation of each key).
Tables, by nature, are stateful, and are often used for performing aggregations in Kafka Streams
Table is materialized on the Kafka Streams side using a key-value store which, by default, is implemented using RocksDB.26

One of the benefits of using the high-level DSL over the lower-level Processor API in Kafka Streams is that the former 
includes a set of abstractions that make working with streams and tables extremely easy.
1. KStream - A KStream is an abstraction of a partitioned record stream, in which data is represented using insert semantics 
(i.e., each event is considered to be independent of other events).
2. KTable - A KTable is an abstraction of a partitioned table (i.e., changelog stream), in which data is represented using 
update semantics (the latest representation of a given key is tracked by the application). Since KTables are partitioned, 
each Kafka Streams task contains only a subset of the full table
3. GlobalKTable - This is similar to a KTable, except each GlobalKTable contains a complete (i.e., unpartitioned) copy of 
the underlying data. 

