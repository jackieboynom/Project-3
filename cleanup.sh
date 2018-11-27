#!/bin/bash


# Change this to your netid
netid=kxc170002

#
# Root directory of your project
PROJDIR=./CS6378/Project-3/src

#
# Directory where the config file is located on your local system
CONFIGLOCAL=./src/config_file.txt

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read line
    i=$( echo $line | awk '{ print $1 }' )
    echo $i
    while [[ $n -lt $i ]]
    do
    	read line
        host=$( echo $line | awk '{ print $2 }' )

        echo $host
        konsole -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host.utdallas.edu killall -u $netid" &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
