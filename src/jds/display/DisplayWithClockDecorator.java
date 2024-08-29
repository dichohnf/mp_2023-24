package jds.display;

import java.time.LocalTime;

import jds.Sensor;

public final class DisplayWithClockDecorator extends DisplayWithSensorDecorator<LocalTime> {

	private static final int DAY_COLOR = 5;
	private static final int NIGHT_COLOR = 10;

	public DisplayWithClockDecorator(Sensor<LocalTime> sensor, Display component) {
		super(sensor, component);
	}

	@Override
	protected void setValue(LocalTime measure) {
		if(measure.isBefore(LocalTime.of(07, 00)) 
		|| measure.isAfter(LocalTime.of(22, 00)))
			setColorTemperature(NIGHT_COLOR);
		else
			setColorTemperature(DAY_COLOR);
	}

}
