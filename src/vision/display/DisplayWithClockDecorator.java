package vision.display;

import java.time.LocalTime;

import vision.display.sensor.Sensor;
import vision.exception.PoorlyDefinedMeasureException;

public final class DisplayWithClockDecorator extends DisplayWithSensorDecorator<LocalTime> {

	public DisplayWithClockDecorator(Sensor<LocalTime> sensor, Display component) {
		super(sensor, component);
	}

	@Override
	public void setValue(LocalTime measure) throws PoorlyDefinedMeasureException {
		if(measure.isBefore(LocalTime.of(07, 00)) 
		|| measure.isAfter(LocalTime.of(22, 00)))
			setNightColor();
	}

	private void setNightColor() {
		setColorTemperature(10);
	}

}
