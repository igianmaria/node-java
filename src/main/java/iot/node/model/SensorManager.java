package iot.node.model;

import iot.Node;
import iot.Sensor;
import iot.SensorEventListener;
import iot.messagging.Message;

public class SensorManager implements SensorEventListener {


    public SensorManager() {

    }

    public void startAllSensors() throws Exception {

        SensorLoader.getInstance().loadNode();
        Node.sensors.forEach(sensor -> {
            sensor.setListener(this);
            sensor.turnOn();
        });
    }

    public void stopAllSensors() {
        SensorLoader.getInstance().getAllSensors().values().forEach(Sensor::turnOff);
    }

    @Override
    public void onSensorDataReady(String sensorName, Message message) {
        System.out.println("Sensor " + sensorName + " sending data:" + message);
    }

    @Override
    public void onSensorAlarm(String sensorName, Message message) {
        System.out.println("Sensor " + sensorName + " sending alarm:" + message);
    }
}
