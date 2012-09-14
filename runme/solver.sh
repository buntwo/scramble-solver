#!/bin/sh

cat words | java ScrambleSolver $1 > input
python PRODUCER1.py input
