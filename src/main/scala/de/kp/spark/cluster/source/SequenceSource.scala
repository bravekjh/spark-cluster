package de.kp.spark.cluster.source
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

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import de.kp.spark.core.model._
import de.kp.spark.core.source.{ElasticSource,FileSource,JdbcSource}

import de.kp.spark.cluster.Configuration
import de.kp.spark.cluster.model._

import de.kp.spark.cluster.spec.Sequences

/**
 * A SequenceSource is an abstraction layer on top of
 * different physical data sources to retrieve a sequence
 * database
 */
class SequenceSource (@transient sc:SparkContext) {

  private val model = new SequenceModel(sc)
  
  def get(req:ServiceRequest):RDD[NumberedSequence] = {
    
    val source = req.data("source")
    source match {
      
      /* 
       * Retrieve sequence database persisted as an appropriate 
       * search index from Elasticsearch; the configuration
       * parameters are retrieved from the service configuration 
       */    
      case Sources.ELASTIC => {
        
        val rawset = new ElasticSource(sc).connect(req.data)
        model.buildElastic(req,rawset)
        
      }
      /* 
       * Retrieve sequence database persisted as a file on the (HDFS) 
       * file system; the configuration parameters are retrieved from 
       * the service configuration  
       */    
      case Sources.FILE => {
        
        val path = Configuration.file()._2

        val rawset = new FileSource(sc).connect(req.data,path)
        model.buildFile(req,rawset)
        
      }
      /*
       * Retrieve sequence database persisted as an appropriate table 
       * from a JDBC database; the configuration parameters are retrieved 
       * from the service configuration
       */
      case Sources.JDBC => {
   
        val fields = Sequences.get(req).map(kv => kv._2._1).toList  
                
        val rawset = new JdbcSource(sc).connect(req.data,fields)
        model.buildJDBC(req,rawset)
        
      }
      
      case _ => null
      
    }

  }
  
}