# ALSExtension
PMF SoRec for Spark.

An extention of Spark based on the original ALS Algorithom provided in Spark.

**Thanks the team building ALS algorithm on Spark.**

* The PMF model is the one in [1], and there is also a Top-K version like the implict one in mlib.recommendation.ALS
* The SoRec model is the one mentioned in [2], and a Top-K version is provied as well
* All the Top-k als formulas could be found in [3]
* [4] is also a good reference to get an overall view of matrix factorization for Top-K version

# ml
similar to org.apache.spark.ml.recommendation.
* ALSPMF.scala:the computation part of PMF, called by PMF.scala in mllib
* ALSSoRec.scla:the computation part of SoRec, called by SoRec.scala in mllib
* ALS.scala:the original code of ALS on Spark

# mllib
similar to org.apache.spark.mllib.recommendation
* PMF.scala: Interface of PMF
** train: traditional collaborative filtering method only focusing on observerd ratings
** trainImplicit:Top-K version collaborative filtering method focusing on all user-item.For unobserved ratings, we give them a value of zero
* SoRec.scala: Interface of SoRec
** train: traditional collaborative filtering method only focusing on observerd ratings
** trainImplicit:Top-K version collaborative filtering method focusing on all user-item.For unobserved ratings, we give them a value of zero


# examples
Example scripts of how to apply these two models, which are very similar to the ALS example in mllib guide

#Reference
due the limitation for accessing the Google Schoolar, the references can not be gained now. I would fill it latter
* [1]
* [2]
* [3]
* [4]
