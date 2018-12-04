#!/bin/bash

# Change this to your netid
netid=kxc170002

# Root directory of your project
PROJDIR=./CS6378/Project-3

# Directory where the config file is located on your local system
CONFIGLOCAL=./src/config_file.txt

# Directory your java classes are in
BINDIR=$PROJDIR/src

# Your main project class
PROG=Main

konsole -e "scp -r ./src $netid@dc42.utdallas.edu:./CS6378/Project-3"
konsole --noclose -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@dc42.utdallas.edu javac $BINDIR/*.java"

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
    
    konsole -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host.utdallas.edu java -cp $BINDIR $PROG $n; exec bash" &

        n=$(( n + 1 ))
    done
)
