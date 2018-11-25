### Requirements
JDK 8 or above
SBT 0.13.17

### How to build
```bash
sbt assembly
```
NOTE: We will run tests during assembly task with default configuration file: conf/test.conf

### How to run
## Subscribe to live feed
1) edit conf/local.conf file and specify:
TODO

2) run 
```bash
sh start.sh
```
## Launch at standalone spark cluster
1) sudo docker-compose -f deploy/spark.yaml up
2) validate it at http://localhost:8080/ to check number of registered workers
3) note internal ip of docker vm with spark master
4) edit cfg file at conf/app.conf to specify valid spark master ip:
 * **spark.spark_master**=spark://**172.19.0.2:7077**   
  Please note that all other settings are similar to conf/local.conf but their names must have prefix `spark.`  
5) execute start.sh by invoking sh start.sh it will submit job to spark cluster
```bash
sh spark_test.sh
```
6) results will be shown within stdout of application at WebUI of SparkMaster
 
### Tehcnicality notes
We use docker-compose with mounted volumes with json data file that will allow 
every executor be able to play with data.
