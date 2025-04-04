import spidev
import struct
import time
import json
import os

OFFSET_FILE = "/home/gianmaria/node/plugins/offset.json"

# Configura SPI
spi = spidev.SpiDev()
spi.open(0, 0)
spi.max_speed_hz = 500000
spi.mode = 3

# Attiva modalità di misura
spi.xfer2([0x2D, 0x08])
time.sleep(0.1)

# Calibrazione solo se non esiste offset.json
if not os.path.exists(OFFSET_FILE):
    num_samples = 50
    x_offset, y_offset, z_offset = 0.0, 0.0, 0.0

    for _ in range(num_samples):
        resp = spi.xfer2([0xF2] + [0x00]*6)
        x = struct.unpack('<h', bytes(resp[1:3]))[0] * 0.0039
        y = struct.unpack('<h', bytes(resp[3:5]))[0] * 0.0039
        z = struct.unpack('<h', bytes(resp[5:7]))[0] * 0.0039

        x_offset += x
        y_offset += y
        z_offset += z
        time.sleep(0.005)  # più veloce

    x_offset /= num_samples
    y_offset /= num_samples
    z_offset /= num_samples

    # Salva gli offset
    with open(OFFSET_FILE, "w") as f:
        json.dump({"x": x_offset, "y": y_offset, "z": z_offset}, f)

else:
    # Carica gli offset esistenti
    with open(OFFSET_FILE, "r") as f:
        offsets = json.load(f)
        x_offset = offsets["x"]
        y_offset = offsets["y"]
        z_offset = offsets["z"]

# Lettura compensata
resp = spi.xfer2([0xF2] + [0x00]*6)
x = struct.unpack('<h', bytes(resp[1:3]))[0] * 0.0039 - x_offset
y = struct.unpack('<h', bytes(resp[3:5]))[0] * 0.0039 - y_offset
z = struct.unpack('<h', bytes(resp[5:7]))[0] * 0.0039 - z_offset

print(f"{x:.4f},{y:.4f},{z:.4f}")
spi.close()
