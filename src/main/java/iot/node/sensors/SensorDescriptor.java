package iot.node.sensors;

import lombok.Data;

@Data
public class SensorDescriptor {
    private String name;
    private String className;
    private String version;
    private String url;
    private int readIntervall;
}
