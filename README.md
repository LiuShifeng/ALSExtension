# ALSExtension
PMF SoRec for Spark.

An extention of Spark based on the original ALS Algorithom provided in Spark.

**Thanks the team building ALS algorithm on Spark.**

* The PMF model is the one in [1], and there is also a Top-K version like the implict one in mlib.recommendation.ALS
* The SoRec model is the one mentioned in [2], and a Top-K version is provied as well
* All the Top-k als formulas could be found in [3]
* [4] is also a good reference to get an overall view of matrix factorization for Top-K version

# **How to use**
All codes listed here is developped on spark 1.3.1. So you should re-package the spark 1.3.1 with these source files here since many privete classes of spark and mllib are called in these files.

* For the source files in ml/recommendation, you should add them into org/apache/spark/ml/recommendation in spark 1.3.1  
* For the source files in mllib/recommendation, you should add them into org/apache/spark/mllib/recommendation in spark 1.3.1
* For the package process, there is nothing different from the original process. Check [Building Spark](org/apache/spark/ml/recommendation in spark 1.3.1)for more details.

# **ml**
similar to org.apache.spark.ml.recommendation.
* ALSPMF.scala:the computation part of PMF, called by PMF.scala in mllib
* ALSSoRec.scla:the computation part of SoRec, called by SoRec.scala in mllib

# **mllib**
similar to org.apache.spark.mllib.recommendation
* PMF.scala: Interface of PMF
  * train: traditional collaborative filtering method only focusing on observerd ratings
  * trainImplicit:Top-K version collaborative filtering method focusing on all user-item.For unobserved ratings, we give them a value of zero

* SoRec.scala: Interface of SoRec
  * train: traditional collaborative filtering method only focusing on observerd ratings
  * trainImplicit:Top-K version collaborative filtering method focusing on all user-item.For unobserved ratings, we give them a value of zero


# **examples**
Example scripts of how to apply these two models, which are very similar to the ALS example in mllib guide

# **Reference**
due the limitation for accessing the Google Schoolar, the references can not be gained now. I would fill it latter
* [1]Mnih A, Salakhutdinov R. Probabilistic matrix factorization[C]//Advances in neural information processing systems. 2007: 1257-1264.
* [2]Ma H, Yang H, Lyu M R, et al. Sorec: social recommendation using probabilistic matrix factorization[C]//Proceedings of the 17th ACM conference on Information and knowledge management. ACM, 2008: 931-940.
* [3]Yang X, Steck H, Guo Y, et al. On top-k recommendation using social networks[C]//Proceedings of the sixth ACM conference on Recommender systems. ACM, 2012: 67-74.
* [4]Yang X, Guo Y, Liu Y, et al. A survey of collaborative filtering based social recommender systems[J]. Computer Communications, 2014, 41: 1-10.
