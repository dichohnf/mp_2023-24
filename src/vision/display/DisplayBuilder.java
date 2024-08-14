package vision.display;

import java.time.LocalTime;
import java.util.List;

import vision.ComunicationChannel;
import vision.display.interfaces.video.VideoInterface;
import vision.display.sensor.Sensor;
import vision.exception.AbsentVideoInterfaceException;

public final class DisplayBuilder {
	
	private int maxNits;
	private List<String> supportedResolutions;
	private ComunicationChannel channel;
	private List<VideoInterface> supportedInterfaces;
	private Sensor<Double> brightnessSensor;
	private double saturation;
	private Sensor<LocalTime> clockSensor;
	
	public DisplayBuilder() {
		reset();
	}
	
	void reset() {
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
	
	public void addBrightnessSensorWithSaturation(Sensor<Double> sensor, Double saturation) {
		brightnessSensor = sensor;
		this.saturation = saturation;
	}
	
	public void addClockSensor(Sensor<LocalTime> sensor) {
		clockSensor = sensor;
	}
	
	public DisplayProjector build() throws AbsentVideoInterfaceException {
		Display result = new StandardDisplay(maxNits, supportedResolutions, channel, supportedInterfaces, null);
		if(brightnessSensor != null)
			result = new DisplayWithBrightnessSensorDecorator(brightnessSensor, result, saturation);
		if(clockSensor != null)
			result = new DisplayWithClockDecorator(clockSensor, result);
		return result;
	}
	
}
