package iot.node.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iot.Node;
import iot.Sensor;
import iot.SensorEventListener;
import iot.node.RawNodeData;
import iot.node.RawSensorDescriptor;

import java.util.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

public class SensorLoader {
    private static SensorLoader instance;
    private final Map<String, Sensor> loadedSensors = new HashMap<>();
    private String TAG = "[SensorLoader] ";

    private SensorLoader() {}

    public static synchronized SensorLoader getInstance() {
        if (instance == null) {
            instance = new SensorLoader();
        }
        return instance;
    }

    public void loadNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RawNodeData raw = mapper.readValue(new File("conf/sensors.json"), RawNodeData.class);

        Node.TemperatureType = raw.getTemperatureType();
        Node.VibrationType = raw.getVibrationType();

        for (RawSensorDescriptor desc : raw.getSensors()) {
            Class<?> sensorClass = Class.forName(desc.getClassName());
            Sensor sensor = (Sensor) sensorClass.getDeclaredConstructor().newInstance();
            Node.sensors.add(sensor);

            sensor.setUpdateURL(desc.getUpdateURL());
            sensor.initialize();

            if (desc.getSendInterval() != 0) {
                sensor.setSendInterval(desc.getSendInterval());
            }

        }
    }

    public Map<String, Sensor> getAllSensors() {
        return loadedSensors;
    }

    public Sensor getSensor(String name) {
        return loadedSensors.get(name);
    }

    public void unloadSensor(String name) {
        loadedSensors.remove(name);
    }
}
