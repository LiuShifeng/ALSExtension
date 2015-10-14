/**
 * Created by LiuShifeng on 2015/10/12.
 */
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating
import wti.st.ALSExtension.mllib.recommendation.SoRec

object SoRecexample{
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("SoRecExample").set("spark.akka.heartbeat.pauses", "1200").set("spark.akka.failure-detector.threshold", "600").set("spark.akka.heartbeat.interval", "2000")
    val sc = new SparkContext(sparkConf)

    // Load and parse the data
    val data = sc.textFile("data/mllib/als/test.data")
    val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val numItem = 642
    val alpha = 0.01
    val lambda = 0.01
    val gamma = 0.01
    val belta = 0.01
    //Rating version
    val model = SoRec.train(ratings, rank, numIterations, numItem, lambda, gamma)
    //Top-K version
    val model = SoRec.trainImplicit(ratings, rank, numIterations, numItem, lambda, alpha,belta,gamma)

    // Evaluate the model on rating data
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    // Save and load model
    model.save(sc, "myModelPath")
    val sameModel = MatrixFactorizationModel.load(sc, "myModelPath")
  }
}


