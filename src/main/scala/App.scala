package example

import java.security.SecureRandom

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import grizzled.math.stats._
import com.quantifind.charts.Highcharts._

object App {
  def main(args: Array[String]) {
    val nPartitions = 200
    val nAccounts = 1000
    val maxRecords = 100
    val windowSpan = 20
    val generator = new SecureRandom()
    val groupSize = nAccounts / 3
    val usersPerAccount = 0.to(nAccounts).map { i =>
      if(i < groupSize) {
        100000
      } else if (i < groupSize * 2){
        25000
      } else {
        1000
      }
    }
    val partitions: Array[Int] = new Array(nPartitions)

    def partition(accountId: Int, userId: Int, partitions: Int,  maxwindowSize: Int): Int = {
      if(partitions == 1) {
        0
      } else {
        val windowSize: Int = Math.min(partitions, maxwindowSize)

        val windows = Math.ceil(partitions / windowSize.toDouble).toInt

        val spotInWindow = Math.abs(userId.hashCode()) % windowSize
        val window = Math.abs(accountId.hashCode()) % windows

        val partitionNumber = window * windowSize + spotInWindow

        if(partitionNumber >= partitions) partitionNumber - partitions else partitionNumber
      }
    }

    def generateRecords(accountId: Int, nPartitions: Int, span: Int): Unit = {
      0.to(usersPerAccount(accountId)).foreach { userId =>
        0.to(generator.nextInt(maxRecords)).foreach { _ =>
          val p =  partition(accountId, userId, nPartitions, span)
          partitions(p) += 1
        }
      }
    }

    println("Calculating distribution. Please be patient as this might take some time...")
    implicit val ec = scala.concurrent.ExecutionContext.global
    0.to(nAccounts).map { accountId => Future { generateRecords(accountId, nPartitions, windowSpan) } }.foreach(Await.result(_, 10 seconds))

    startServer()

    partitions.zipWithIndex.foreach{ case (el, index) => println(s"${index}: ${el}") }

    val partitionsMean = mean(partitions: _*)
    val partitionsVariance = populationVariance(partitions: _*)
    val partitionsStddev = popStdDev(partitions: _*)

    println(s"The mean per partition is: ${partitionsMean}")
    println(s"The variance per partition is: ${partitionsVariance}")
    println(s"The partition std dev is: ${partitionsStddev} - ${((partitionsStddev * 100.0f) / partitionsMean)}%")
    column(partitions.toList)
    yAxis("number items")
    hold()
    column(0 until nPartitions)
    xAxis("partitions")
  }
}
