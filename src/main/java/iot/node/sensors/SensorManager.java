package iot.node.sensors;

import iot.Sensor;
import iot.SensorEventListener;
import iot.messagging.Message;

public class SensorManager implements SensorEventListener {


    public SensorManager() {

    }

    public void startAllSensors() throws Exception {

        SensorLoader.getInstance().loadSensorsFromJson( this);

        SensorLoader.getInstance().getAllSensors().values().forEach(sensor -> {
            sensor.setSensorEventListener(this);
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

    }
}
