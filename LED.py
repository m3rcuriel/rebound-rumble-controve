import socket
import RPi.GPIO as io
import struct
from array import array

UDP_PORT = 5803

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

try:
    io.setmode(io.BCM)
    io.setup(26, io.OUT)
    io.setup(13, io.OUT)
    io.setup(19, io.OUT)
    rPWM = io.PWM(19, 50)
    gPWM = io.PWM(13, 50)
    bPWM = io.PWM(26, 50)
    rPWM.start(0)
    gPWM.start(0)
    bPWM.start(0)

    def setRGBValues(r, g, b, brightness):
        rPWM.ChangeDutyCycle((r * 100.0 / 255)*(brightness * 100.0 / 255)
        gPWM.ChangeDutyCycle((g * 100.0 / 255)*(brightness * 100.0 / 255)
        bPWM.ChangeDutyCycle((b * 100.0 / 255)*(brightness * 100.0 / 255)

    sock.bind(("", UDP_PORT))

    r = 255
    g = 255
    b = 255
    a = 255
    while True:
        data, addr = sock.recvfrom(8)  # recieve 8 bytes (4 shorts)
        print(len(data))

        # unpack rgba values
        if len(data) >= 6:
            r = int(struct.unpack('>h', data[0:2])[0])
            g = int(struct.unpack('>h', data[2:4])[0])
            b = int(struct.unpack('>h', data[4:6])[0])
            if len(data) is not 6:
                a = int(struct.unpack('>h', data[6:8])[0])
            else:
                a = 255

        setRGBValues(r, g, b, a)
finally:
    sock.close()
    io.cleanup()
