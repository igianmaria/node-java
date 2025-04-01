package iot;

import iot.node.sensors.SensorLoader;
import iot.node.sensors.SensorManager;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Node Started..");
        new SensorManager().startAllSensors();
    }
}