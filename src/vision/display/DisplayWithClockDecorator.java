package vision.display;

import java.time.LocalTime;

import vision.display.sensor.Sensor;
import vision.exception.PoorlyDefinedMeasureException;

public final class DisplayWithClockDecorator extends DisplayWithSensorDecorator<Integer> {

	public DisplayWithClockDecorator(Sensor<Integer> sensor, Display component) {
		super(sensor, component);
	}

	@Override
	public void setValue(Integer measure) throws PoorlyDefinedMeasureException {
		if(LocalTime.now().isBefore(LocalTime.of(07, 00)) 
		|| LocalTime.now().isAfter(LocalTime.of(22, 00)))
			setNightColor();
	}

	private void setNightColor() {
		setColorTemperature(10);
	}

}
