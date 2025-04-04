package iot;

import iot.node.model.SensorManager;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Node Started..");
        new SensorManager().startAllSensors();
    }
}