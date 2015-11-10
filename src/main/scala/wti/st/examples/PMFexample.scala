import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, Rating}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.PMF

object PMFexample{
  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("PMFExample")
      .set("spark.akka.heartbeat.pauses", "1200")
      .set("spark.akka.failure-detector.threshold", "600")
      .set("spark.akka.heartbeat.interval", "2000")
    val sc = new SparkContext(sparkConf)
    // Load and parse the data
    val data = sc.textFile("data/mllib/als/test.data")
    val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val alpha = 0.01
    val lambda = 0.01
    //Rating Version
    val model = PMF.train(ratings, rank, numIterations, lambda)
    //Top-K Version
    //val model = PMF.trainImplicit(ratings, rank, numIterations, lambda, alpha)

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
      val err = r1 - r2
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    // Save and load model
    model.save(sc, "myModelPath")
    val sameModel = MatrixFactorizationModel.load(sc, "myModelPath")


  }
}

