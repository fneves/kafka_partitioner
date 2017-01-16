# Description

Partitioning Function

The current function assumes you have an account_id and a user_id in a multitenant application.
The users distribution is divided into 3 groups, *big accounts*, *medium accounts* and *small accounts*.
This will have an impact in the total number of records we assume an account has.

Modify the partition function to see what impact your partitioning logic has.

The current function basically partitions an account into N predictive partitions, so you can calculate where they will land.
The current function also guarantees you will have an even distribution of all accounts across all partitions.


To apply this to your current Kafka code use a customer partitioner. This blog shows a great in depth example on how to do this:
[http://howtoprogram.xyz/2016/06/04/write-apache-kafka-custom-partitioner/](http://howtoprogram.xyz/2016/06/04/write-apache-kafka-custom-partitioner/)

![Screenshot](https://raw.githubusercontent.com/fneves/kafka_partitioner/master/chart.png)
# Run the code

```bash
sbt run
```



