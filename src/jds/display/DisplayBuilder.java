package jds.display;

import java.time.LocalTime;
import java.util.List;

import jds.ComunicationChannel;
import jds.Sensor;
import jds.display.interfaces.VideoInterface;

public final class DisplayBuilder {
	
	int maxNits;
	List<String> supportedResolutions;
	ComunicationChannel channel;
	List<VideoInterface> supportedInterfaces;
	Sensor<Double> brightnessSensor;
	double saturation;
	Sensor<LocalTime> clockSensor;
	
	public DisplayBuilder() {
		reset();
	}
	
	public void reset() {
		maxNits = -1;
		supportedResolutions = null;
		channel = null;
		supportedInterfaces = null;
		brightnessSensor = null;
		saturation = -1;
		clockSensor = null;
	}
	
	public void setMaxNits(int maxNits) {
		this.maxNits = maxNits;
	}
	
	public void setSupportedResolutions(List<String> supportedResolutions) {
		this.supportedResolutions = supportedResolutions;
	}
	
	public void setSupportedInterfaces(List<VideoInterface> supportedInterfaces) {
		this.supportedInterfaces = supportedInterfaces;
	}
	
	public void setChannel(ComunicationChannel channel) {
		this.channel = channel;
	}
	
	public void addBrightnessSensorWithSaturation(Sensor<Double> sensor, Double sensorSaturation) {
		brightnessSensor = sensor;
		this.saturation = sensorSaturation;
	}
	
	public void addClockSensor(Sensor<LocalTime> sensor) {
		clockSensor = sensor;
	}
	
	public Display build() {
		Display result = new StandardDisplay(maxNits, supportedResolutions, channel, supportedInterfaces);
		if(brightnessSensor != null)
			result = new DisplayWithBrightnessSensorDecorator(brightnessSensor, result, saturation);
		if(clockSensor != null)
			result = new DisplayWithClockDecorator(clockSensor, result);
		return result;
	}
	
}
