# Drains
Drains can do crazy stuff. They also can wrap drains. Drains fire Events.
All drains extend the AbstactBasicDrain or AbstractWrappingDrain.
For every drain exists a drain factory.

## async drain
The async drain adds metrics from a list of metrics to a queue. Async Drains are threads.
```
{
    "type" : "queued",
    "max-queue-size" : 10,
    "inner" : {
        ...
    }
}
```
## bifroest drain
The bifroest drain generates a JSONObject for each element of a metric list. These JSONObjects are send to bifroest as include-metric commands.
Bifroest can work with the JSONObjects and respond to them, for example Bifroest can add the metric to a cache and the prefix tree or update the
metrics timestamp in the prefix tree.

```
{
    "type" : "bifroest",
    "host" : "localhost",
    "port" : 5102
}
```

## buffering drain
This drain is used to create one single database query instead of multiple small ones. 
The buffering drain has a configurable capacity and warnLimit. It adds metrics to a buffer; the buffersize is determined by the capacity. The warnLimit is a time frame configured in seconds.
If the buffer contains metrics for a longer time than the warnLimit, the system will throw a warning message.

```
{
    "type" : "buffered",
    "buffersize" : 10,
    "warnlimit" : "20s",
    "inner" : {
        ...
    }
}
```

## carbon drain
This drain is the primary internal communication drain. It uses the carbon plaintext protocol.
The carbon drain needs a host and port. It sends data (metric names, timestamps and values) to the host.
Hostnames are written reversed.

```
{
    "type" : "carbon",
    "carbon host" : "localhost",
    "carbon port" : "5200"
},
{
    "type" : "carbon",
    "carbon host" : "test-carbon.services.ggs-net.com",
    "carbon port" : "5200"
}

    
```

## chunking drain
The chunking drain splits a list of metrics to smaller lists. The size of the smaller lists is set in the configuration.

```
{
    "type" : "chunked",
    "chunksize" : 65000,
    "inner" : {
        ...
    }
}
```

## counting drain
The counting drain counts the number of metrics in a metric list. With this functionality the amount of metrics sent and received by a system can be 
monitored. It will also show wheather metrics were lost or removed by another drain. The number of metrics which were counted by this drain 
will be saved using the drain-id and thus can be shown in a monitoring system.

```
{
    "type" : "count-metrics",
    "drain-id" : "statistics.carbon",
    "inner" : {
        ...
    }
}
```

## debug drain
The DebugLoggingDrain filters a list of metrics and enables debug logging for them. Logging messages will be written to the debug log.
This might be switched off in a stable system.

```
{
    "type" : "debug logging"
    "inner" : {
        ...
    }
}
```

## empty drain
The empty drain does - what a surprise - nothing. It is used for testing.

## filterU drain
A value of "U" means "undefined" in Munin. Some drains might not be able to handle these values. To solve the problem of
these values causing exceptions in some drains they can be filtered using the filterU drain.
The filterU drain filters a list of metrics for elements of which the value == NaN. Metrics with a value are added to a new
list, those with a value equal to NaN will not be added to the new list.

```
{
    "filter-u",
    "inner" : {
        ...
    }
}
```
## persistent cassandra drain
This drain belongs to the stream rewriter.

The persistent cassandra drain creates batch statements from a list of metrics. It writes metrics to the database and also it
can trigger the creation of new database tables.
```
{
    "cassandra" : {
        "seeds" : [
            "my.first.cassandra.seed",
            "my.second.cassandra.seed"
        ],
        "keyspace" : "graphite"
    }
}
```

## repeated flush drain
The repeated flush drain flushes buffers after a certain time interval. This task will be repeated with a certain frequency which
can be set in the configuration.

```
{
    "type" : "repeated-flush",
    "each" : "10s",
    "inner" : {
        ...
    }
}
```

## serial drains
### serial fail first drain
This drain triggers an output of a metric list to other drains and waits for the result. If a drain throws an exception, the fail 
first drain will stop outputting the metric list to other drains.

```
{
    "type" : "fail-first",
    "inner" : [
        {
            ...
        }
    ]
}
```

### serial never fail drain
The never fail drain outputs a list of metrics only to drains that are able to log exceptions?? 

```
{
    "type" : "never-fail",
    "inner" : [
        {
            ...
        }
    ]
}      
```

## tree and cache drain
This drain belongs to bifroest.

The tree and cache drain adds for each metric in a list an entry in bifroest's prefix tree and puts the metric into the metric Cache. This is needed 
to update the prefix trees contents, such as the latest timestamp at which a metric has been sent to bifroest. 

```
{
    "type" : "tree-and-cache"
}
```

