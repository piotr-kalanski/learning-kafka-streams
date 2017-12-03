package com.datawizards.kafka

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder, KTable}
import collection.JavaConverters._

object WordCountScala extends App {
  val config = new Properties
  config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application")
  config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)

  // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
  config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")

  val builder: KStreamBuilder = new KStreamBuilder
  // 1 - stream from Kafka

  val textLines: KStream[String, String] = builder.stream[String, String]("word-count-input")

  val wordCounts: KTable[String, java.lang.Long] = textLines
    // 2 - map values to lowercase
    .mapValues[String]((textLine: String) => textLine.toLowerCase)
    // 3 - flatmap values split by space
    .flatMapValues[String]((textLine: String) => textLine.split("\\W+").toSeq.asJava)
    // 4 - select key to apply a key (we discard the old key)
    .selectKey[String]((key: String, word: String) => word)
    // 5 - group by key before aggregation
    .groupByKey
    // 6 - count occurences
    .count("Counts")

  // 7 - to in order to write the results back to kafka
  wordCounts.to(Serdes.String, Serdes.Long, "word-count-output-scala")

  val streams = new KafkaStreams(builder, config)
  streams.start()

  // print the topology
  System.out.println(streams.toString)

  // shutdown hook to correctly close the streams application
  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run(): Unit = {
      streams.close()
    }
  })

}
