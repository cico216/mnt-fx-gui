#!/bin/bash
##########################################################
# Write date  : 2015/06/19
# Written by  : duxiang
# Written fea : Game control of shell script
##########################################################
real_path=$(cd `dirname $0`; pwd)
echo  "本脚本所在目录路径是: $real_path "
cd $real_path

. ~/.bash_profile

#old_judge(){
#	tpid=`ps x | grep "game.jar" | grep -v grep | grep -v "checkPid test" | awk '{print $1}'` 
#	echo "$tpid"
#	if [ "$tpid" -gt 1 ] ; then
#		kill -15 "$tpid"
#	fi
#	while :
#		do
#			pd=`ps x | grep "game.jar" | grep -v grep | grep -v "checkPid test" | awk '{print $1}'`
#	if [ "$pd" -gt 1 ] ; then
#		sleep 1
#	else
#		break
#	fi
#done
#}
system() {
	ulimit -c unlimited
	ulimit -n 65534
	sysctl -w kernel.core_pattern=core.%e.%t.%p
	#/usr/sbin/ntpdate time.windows.com 210.72.145.44
	echo 系统时间 `date +%F_%T`
}
#定义显示
msg_correct=`echo -en "[\033[01;32;40m  OK  \033[0m]"`
msg_igone=`echo  "[\033[01;32;40m忽略\033[0m]"`
msg_error=`echo -en "[\033[01;31;40mFAILED\033[0m]"`
#定义距离
style()
{
    sleep 1
    printf "%-51s   %s\n" "$1" "$2"
}
##服务器电信IP和联通IP
CT_IP=$(`which ifconfig` | grep inet  | grep Bcast | awk -F":" '{print $2}' | awk '{print $1}'  | awk 'NR==1')
CNC_IP=$(`which ifconfig` | grep inet  | grep Bcast | awk -F":" '{print $2}' | awk '{print $1}'  | awk 'NR==2')


start_game(){
		
	echo 系统时间 `date +%F\ %T`
	 game_pid=$( /bin/ps auxef | grep -v grep| grep -v "checkPid test"|grep "./gameserver.jar"   |  sed 's#OLDPWD=/##g' | grep $real_path | awk '{print $2}'  )
	 
        if [ $game_pid ];then
    		echo  -e  "\e[31m  目录 $real_path 已有一个游戏引擎进程在运行了!  \e[0m"

			#echo -en "是否需要关闭？[y/n]"
			#read answer
			#if [ $answer == y ];then
			#		/bin/sh $real_path/stop-game.sh
			#	else
			#exit 0
			#fi


        else
               send_command
        fi


}


send_command(){
	echo "正在启动游戏..."
	nohup_=`which nohup`
	java_=`which java`
	jps_=`which jps`
	tag=$(pwd |cut -d/ -f5) #脚本所在的目录名称
	
	chmod 777 ../$tag #给目录写入权限
	
	nohup java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9600 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname= -Xmx512m -Xms512m -Xss1m -XX:NewSize=64m -XX:MaxNewSize=256m -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=30 -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSCompactAtFullCollection -cp ./test-hbase.jar:./lib/* com.mgf.nanoha.game.ServerStart & 
	#java -Djava.rmi.server.hostname=192.168.0.13 -Dcom.sun.management.jmxremote.port=8999 -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false 	-Xmx10g -Xms8g -Xmn7g -Xss1m -XX:PermSize=128m -XX:MaxPermSize=256m -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -cp ./game.jar:./lib/* com.myjk.gline.main.Main &
	sleep 4
	#echo "已经发送启动命令给游戏..."
        #########################################################################################################################
        #检查结果
        #game_pid=$( ps auxef | grep -v grep| grep "./game.jar"   |  sed 's#OLDPWD=/##g' | grep $real_path | awk '{print $2}'  )
        game_pid=$( ps auxef | grep -v grep| grep "./gameserver.jar"   |  sed 's#OLDPWD=/##g'  | awk '{print $2}'  )
        if [ $game_pid ];then
		style "Starting game..." "$msg_correct"
		sleep 8
                echo ----------------------------
		echo "游戏PID是$game_pid"
		$jps_
		echo ----------------------------
        	ps -ef | grep -v grep| grep -v "checkPid test"|grep "./gameserver.jar"   |  sed 's#OLDPWD=/##g' | grep ${CT_IP}
		echo
	else	
		style "Starting game..." "$msg_error"
		$jps_
		echo "请手动检查."
                exit 0
        fi
	}


start_game
