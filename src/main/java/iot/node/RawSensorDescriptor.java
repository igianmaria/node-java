package iot.node;

import lombok.Data;

@Data
public class RawSensorDescriptor {
    private String name;
    private String className;
    private String version;
    private String updateURL;
    private int sendInterval;
}
