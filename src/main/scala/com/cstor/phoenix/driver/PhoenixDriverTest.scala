package com.cstor.phoenix.driver

import java.sql.DriverManager

/**
 * Created by Administrator on 2016/5/18.
 */
object PhoenixDriverTest {


  def main(args: Array[String]) {

    Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")

    val conn = DriverManager.getConnection("jdbc:phoenix:192.168.6.154")

    val statement = conn.createStatement()

    val result = statement.executeQuery("select * from TEST03_PHOENIX;")

    while (result != null && result.next()) {
      println(result.getString(1) + ", " + result.getString(2))
    }
  }

}
