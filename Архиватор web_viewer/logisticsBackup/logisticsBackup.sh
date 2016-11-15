#!/bin/bash
date=$1
DBHost=$2
DBPort=$3
DBName=$4
User=$5
Password=$6
java -jar logisticsBackup.jar $date $DBHost $DBPort $DBName $User $Password