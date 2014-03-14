#!/bin/sh

code_home=/home/hadoop/git_project_home/bigdata-netty-server
jar_file=${code_home}/target/BigDataPGetNettyServer-jar-with-dependencies.jar

target_path=/home/hadoop/netty_server

echo "Remote copying..."
scp ${jar_file} hadoop@node0:${target_path}
echo "Node0 copied"
scp ${jar_file} hadoop@node1:${target_path}
echo "Node1 copied"
scp ${jar_file} hadoop@node2:${target_path}
echo "Node2 copied"
scp ${jar_file} hadoop@node3:${target_path}
echo "Node3 copied"
echo "Remote copying is done. Remote killing..."

proc=`ssh hadoop@node0 ps aux | grep BigDataPGetNettyServer-jar-with-dependencies | awk '{print $2}'`
ssh hadoop@node0 kill -9 ${proc}
echo "Node0 old server killed(${proc})."

proc=`ssh hadoop@node1 ps aux | grep BigDataPGetNettyServer-jar-with-dependencies | awk '{print $2}'`
ssh hadoop@node1 kill -9 ${proc}
echo "Node1 old server killed(${proc})."

proc=`ssh hadoop@node2 ps aux | grep BigDataPGetNettyServer-jar-with-dependencies | awk '{print $2}'`
ssh hadoop@node2 kill -9 ${proc}
echo "Node2 old server killed(${proc})."

proc=`ssh hadoop@node3 ps aux | grep BigDataPGetNettyServer-jar-with-dependencies | awk '{print $2}'`
ssh hadoop@node3 kill -9 ${proc}
echo "Node3 old server killed(${proc})."

ssh hadoop@node0 nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9000 &
echo "Node0 new server started."
ssh hadoop@node1 nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9000 &
echo "Node1 new server started."
ssh hadoop@node2 nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9000 &
echo "Node2 new server started."
ssh hadoop@node3 nohup /usr/java/jdk1.7.0_45/bin/java -jar /home/hadoop/netty_server/BigDataPGetNettyServer-jar-with-dependencies.jar 9000 &
echo "Node3 new server started."
echo "All done"