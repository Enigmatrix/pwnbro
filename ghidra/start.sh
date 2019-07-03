#!/bin/bash

cp server/hosts /etc/hosts

server/ghidraSvr start
server/ghidraSvr stop
server/svrAdmin -add enigmatrix
server/svrAdmin -add daniellimws
server/svrAdmin -add ghidra
server/svrAdmin -add root
server/ghidraSvr console
