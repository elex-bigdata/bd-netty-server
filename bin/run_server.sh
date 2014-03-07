#!/bin/sh

code_home=/home/hadoop/git_project_home/bd-netty-server
jar_file=${code_home}/target/BigDataPGetNettyServer-jar-with-dependencies.jar
java_bin=/usr/java/jdk1.7.0_45/bin

if [ "" = "$1" ];then
  echo "No server port assigned."
  exit 1
else
  port=${1}
fi

${java_bin}/java -jar ${jar_file} ${port}
