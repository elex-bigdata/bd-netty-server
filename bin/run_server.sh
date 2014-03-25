#!/bin/sh

code_home=/home/hadoop/git_project_home/bigdata-netty-server
jar_name=BigDataPGetNettyServer-jar-with-dependencies.jar
jar_file=${code_home}/target/${jar_name}
java_bin=/usr/java/jdk1.7.0_45/bin
pattern=zab

if [ "" = "$1" ];then
  echo "No server port assigned."
  exit 1
else
  port=${1}
fi

proc=`ps aux | grep ${jar_name} | grep ${port} | awk '{print $2}'`
echo "Shutdown exists netty server"
nohup ${java_bin}/java -jar ${jar_file} ${port} ${pattern} &
