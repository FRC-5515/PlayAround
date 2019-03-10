#!/bin/bash
### BEGIN INIT INFO
# Provides:          FRCVISION
# Required-Start:    $local_fs $network
# Required-Stop:     $local_fs
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: PythonFRCVISION service
# Description:       PythonFRCVISION service
### END INIT INFO

filepath="/home/frc5515/Desktop/FRCVISION-19-3-9.py"

start(){
    nohup python3 $filepath>/dev/null 2>&1 &
    echo 'FuzzyMatching service OK'
}


stop(){
    serverpid=`ps -aux|grep "$filepath"|grep -v grep|awk '{print $2}'`
    kill -9 $serverpid
    echo 'FuzzyMatching stop OK'
}


restart(){
    stop
    echo 'FuzzyMatching stop OK'
    start
    echo 'FuzzyMatching service OK'
}


case $1 in
    start)
    start
    ;;
    stop)
    stop
    ;;
    restart)
    restart
    ;;
    *)
    start
esac