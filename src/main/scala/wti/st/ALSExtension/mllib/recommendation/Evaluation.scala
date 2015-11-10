import org.apache.spark.rdd.RDD

object Evaluation{

  def MSE(ratesAndPreds:RDD[((Int,Int),(Double,Double))]):Double = {
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = r1 - r2
      err * err
    }.mean()
    MSE
  }

  def TopKRatio(ratesAndPreds:RDD[((Int,Int),(Double,Double))], k:Int, relevant:Double, numItems:Int):(Double,Double)={
    val userPred = ratesAndPreds.map {
      case ((user,item),(rates,pred)) => (user,(item,rates,pred))
    }.groupByKey().map(x=>{
      var Nk = 0
      var Nu = 0
      val interestPreVector = new Array[(Double,Double)](numItems)
      x._2.foreach(y => {
        interestPreVector(y._1) = (y._2, y._3)
        if (y._2 > relevant) Nu = Nu + 1
      })
      val sortedInterestPreVector = interestPreVector.sortWith(_._2 > _._2)
      for(i <- 0 until k){
        if (sortedInterestPreVector(i)._1 > relevant) Nk = Nk + 1
      }
      (x._1,(Nk,Nu))
    })
    val userNum = ratesAndPreds.count()
    val sumNkandsumNu = userPred.map{ case (user,(nk,nu)) => (nk,nu)}
      .reduce((x,y)=>(x._1+y._1,x._2+y._2))
    val recall = sumNkandsumNu._1.toDouble/sumNkandsumNu._2.toDouble
    val Hk = userPred.map{ case (user,(nk,nu)) => ("tag",nk.toDouble/nu.toDouble)}
      .reduceByKey(_+_).collect()(0)._2/userNum
    (Hk,recall)
  }
}
