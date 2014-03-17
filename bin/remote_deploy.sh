#!/bin/sh

code_home=/home/hadoop/git_project_home/bigdata-netty-server
jar_file=${code_home}/target/BigDataPGetNettyServer-jar-with-dependencies.jar
host=REDIS_P_SLAVE
target_path=/home/hadoop/netty_server

echo "Remote copying..."
scp ${jar_file} hadoop@${host}:${target_path}
echo "${host} copied"
echo "Remote copying is done. Remote killing..."

proc=`ssh hadoop@${host} ps aux | grep BigDataPGetNettyServer-jar-with-dependencies | awk '{print $2}'`
ssh hadoop@${host} kill -9 ${proc}
echo "${host} old server killed(${proc})."

ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9001 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9002 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9003 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9004 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9005 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9006 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9007 &
ssh hadoop@${host} nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9008 &
echo "All done"