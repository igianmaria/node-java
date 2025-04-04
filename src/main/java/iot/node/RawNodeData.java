package iot.node;

import iot.Sensor;
import iot.hw.characterization.MachineTemperatureType;
import iot.hw.characterization.MachineVibrationType;
import lombok.Data;

import java.util.List;

@Data
public class RawNodeData {
    private MachineTemperatureType temperatureType;
    private MachineVibrationType vibrationType;
    private List<RawSensorDescriptor> sensors;
}
