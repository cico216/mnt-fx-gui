. ~/.bash_profile
tpid=`ps x | grep "gameserver.jar" | grep -v grep | grep -v "checkPid test" | awk '{print $1}'`
echo "$tpid"
if [ "$tpid" -gt 1 ] ; then
	kill -15 "$tpid"
	sleep 30
fi
echo "start done !!"
