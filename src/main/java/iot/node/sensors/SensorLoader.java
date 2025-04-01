package iot.node.sensors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iot.Sensor;
import iot.SensorEventListener;

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

    public void loadSensorsFromJson(SensorEventListener listener) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<SensorDescriptor> sensors = mapper.readValue(new File("conf/sensors.json"), new TypeReference<List<SensorDescriptor>>() {});

        List<CompletableFuture<Void>> futures = sensors.stream()
                .map(desc -> CompletableFuture.runAsync(() -> {
                    try {
                        loadSensor(desc, listener);
                    } catch (Exception e) {
                        System.err.println(TAG + "Errore nel caricamento del sensore: " + desc.getName());
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void loadSensor(SensorDescriptor descriptor, SensorEventListener listener) {

        try {
            System.out.println(TAG + "Inizializzo sensore dal classpath: " + descriptor.getClassName());

            Class<?> sensorClass = Class.forName(descriptor.getClassName());
            Sensor sensor = (Sensor) sensorClass.getDeclaredConstructor().newInstance();

            sensor.setSensorEventListener(listener);
            sensor.initialize();

            if (descriptor.getReadIntervall() != 0) {
                sensor.setReadingInterval(descriptor.getReadIntervall());
            }

            loadedSensors.put(descriptor.getName(), sensor);
        } catch (Exception e) {
            System.err.println(TAG + "Errore nel caricamento del sensore: " + descriptor.getName());
            e.printStackTrace();
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
