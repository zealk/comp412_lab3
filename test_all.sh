#!/bin/bash
for i in report/*.i
do
	echo "$i"
	make sim $i |grep -v "make"
	echo "------------------------------------------"
done
