import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.SoRec

object SoRecexample{
  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("SoRecExample")
      .set("spark.akka.heartbeat.pauses", "1200")
      .set("spark.akka.failure-detector.threshold", "600")
      .set("spark.akka.heartbeat.interval", "2000")
    val sc = new SparkContext(sparkConf)

    // Load and parse the data
    val itemData = sc.textFile("data/mllib/als/test.data")
    val itemRatings = itemData.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    val numItem = 643

    val socialData = sc.textFile("")
    val socialNet = socialData.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt+numItem, rate.toDouble)
    })

    val ratings = itemRatings.union(socialNet)
    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10

    val alpha = 0.01//confidence parameter
    val lambda = 0.01//regulization
    val gamma = 0.01//weight for social influence
    val belta = 0.01//confidence parameter for social influence
    //Rating version
    val model = SoRec.train(ratings, rank, numIterations, numItem, lambda, gamma)
    //Top-K version
    //val model = SoRec.trainImplicit(ratings, rank, numIterations, numItem, lambda, alpha,belta,gamma)

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


