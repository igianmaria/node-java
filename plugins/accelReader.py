import spidev
import struct
import time

spi = spidev.SpiDev()
spi.open(0, 0)
spi.max_speed_hz = 500000
spi.mode = 3

spi.xfer2([0x2D, 0x08])  # put on measurement mode.
time.sleep(0.1)
resp = spi.xfer2([0xF2] + [0x00]*6)

# little-endian
x = struct.unpack('<h', bytes(resp[1:3]))[0]
y = struct.unpack('<h', bytes(resp[3:5]))[0]
z = struct.unpack('<h', bytes(resp[5:7]))[0]

x *= 0.0039
y *= 0.0039
z *= 0.0039

print(f"{x:.4f},{y:.4f},{z:.4f}")
spi.close()