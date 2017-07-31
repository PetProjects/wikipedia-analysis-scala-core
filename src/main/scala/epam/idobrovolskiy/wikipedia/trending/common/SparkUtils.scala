package epam.idobrovolskiy.wikipedia.trending.common

//import org.apache.spark.sql.hive.HiveContext
//import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.sql.SparkSession

import epam.idobrovolskiy.wikipedia.trending.AppName

/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object SparkUtils {
  lazy val sparkSession : SparkSession = {
    //  val sc = new SparkContext(new SparkConf().setAppName(AppName))
    //  val sqlContext = new HiveContext(sc) //deprecated since spark 2.0!

    // Create a SparkSession. No need to create SparkContext
    // You automatically get it as part of the SparkSession
    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
    SparkSession
      .builder()
      .appName(AppName)
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()
  }
}
