import RPi.GPIO as gp
import time

gp.setmode(gp.BCM)
channel = [6,13,19,26]
gp.setup(channel,gp.OUT)

a=0
while a<100:
    a=a+1
    p = channel[int(a%4)]
    gp.output(p,1)
    time.sleep(0.5)
    gp.output(p,0)
    time.sleep(0.5)


