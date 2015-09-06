import RPi.GPIO as GPIO
import time
import picamera  # new
import datetime
import subprocess

sensor = 12

def get_file_name():
    return datetime.datetime.now().strftime("%Y-%m-%d_%H.%M.%S.jpg")

#GPIO.setmode(GPIO.BCM)
GPIO.setmode(GPIO.BOARD)
#GPIO.setup(sensor, GPIO.IN, GPIO.PUD_DOWN)
GPIO.setup(sensor,GPIO.IN)
previous_state = False
current_state = False

cam = picamera.PiCamera()  # new

while True:
	previous_state = current_state
	current_state = GPIO.input(sensor)
	if current_state != previous_state:
        	new_state = "HIGH" if current_state else "LOW"
        	print("GPIO pin %s is %s" % (sensor,new_state))
	if current_state:
		fileName = get_file_name()
		cam.capture(fileName)
		subprocess.call("java -jar /home/pi/East-Sweden-Hack-2015/EastSwedenHack/dist/EastSwedenHack.jar", shell=True)
		time.sleep(5.0)
		#break