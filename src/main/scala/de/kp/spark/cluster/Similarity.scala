package de.kp.spark.cluster
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Spark-Cluster project
* (https://github.com/skrusche63/spark-cluster).
* 
* Spark-Cluster is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* Spark-Cluster is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* Spark-Cluster. 
* 
* If not, see <http://www.gnu.org/licenses/>.
*/

import scala.language.postfixOps

object CosineSimilarity {
  
  def compute(x: Array[Int], y: Array[Int]): Double = {

    require(x.size == y.size)
    dotProduct(x, y)/(magnitude(x) * magnitude(y))
  
  }
  
  private def dotProduct(x: Array[Int], y: Array[Int]): Int = {
    (for((a, b) <- x zip y) yield a * b) sum
  }
  
  private def magnitude(x: Array[Int]): Double = {
    math.sqrt(x map(i => i*i) sum)
  }
  
}