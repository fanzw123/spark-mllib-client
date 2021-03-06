package com.cstor.hbase.client

import java.util
import java.util.UUID

import org.apache.hadoop.hbase.{TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client.coprocessor.{LongColumnInterpreter, AggregationClient}
import org.apache.hadoop.hbase.client.{Get, Put, HTable, Scan}
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by feng.wei on 2016/5/25.
  */
object HbaseFakeData {


  private val conf = HBaseConfiguration.create()

  conf.set("hbase.zookeeper.quorum", "datacube151,datacube154")
  conf.set("zookeeper.znode.parent", "/hbase")

  def fakeData(num: Int, htable: HTable) {

    htable.setAutoFlushTo(false)
    val putList = new util.ArrayList[Put]()
    for (i <- 1 until (num)) {

      val put = new Put(Bytes.toBytes(i + ""))
      val value = UUID.randomUUID().toString.replaceAll("-", "")
      put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(i + ""), Bytes.toBytes(value))
      putList.add(put)

    }
    htable.setAutoFlushTo(true)
    htable.put(putList)

  }

  def getData(hTable: HTable, rowkey: String, family: String, column: String): String = {
    val result = hTable.get(new Get(Bytes.toBytes(rowkey)))
    val value = Bytes.toString(result.getValue(Bytes.toBytes(family), Bytes.toBytes(column)))
    println("get the value is : " + value)
    value
  }

  def countData(tableName: String, cf: Array[Byte]): Long = {
    val aggregationClient = new AggregationClient(conf)
    val scan = new Scan()
    val rowCount = aggregationClient.rowCount(
      TableName.valueOf(tableName)
      , new LongColumnInterpreter()
      , scan)
    println("rowcount = " + rowCount)
    rowCount
  }


  def main(args: Array[String]) {
    val tableName = "cstor:test01"
    val family = Bytes.toBytes("f")
    val htable = new HTable(conf, tableName)
    //    fakeData(100, htable)

    //    val value = getData(htable, "1", "f", "1")
    //    println("value=" + value)

    countData(tableName, family)

    htable.close()
  }

}
