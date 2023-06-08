#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"



TIME_NOW=$(date +%c)

# build 파일 복사
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
nohup java -jar $JAR_FILE 

CURRENT_PID=$(pgrep -f $JAR_FILE)

if [ -n PID ]
then
    echo "Kill -15 $PID"
    kill -15 $PID
    sleep 5
fi

nohup java -jar -Dspring.profiles.active=prod $JAR_FILE > /dev/null 2> /dev/null < /dev/null &
