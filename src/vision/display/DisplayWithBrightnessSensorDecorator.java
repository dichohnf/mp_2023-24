package vision.display;

import java.util.Objects;

import vision.display.sensor.Sensor;
import vision.exception.PoorlyDefinedMeasureException;

public final class DisplayWithBrightnessSensorDecorator extends DisplayWithSensorDecorator<Double> {
	
	private static final Double DEFAULT_SATURATION_LUX = Double.valueOf(10000);
	private final Double saturationLuxAmount;

	protected DisplayWithBrightnessSensorDecorator(
			Sensor<Double> sensor, Display component, Double saturationLuxAmount) {
		super(sensor, component);
		this.saturationLuxAmount =Objects.requireNonNullElse(
				saturationLuxAmount, 
				DEFAULT_SATURATION_LUX);
	}

	@Override
	protected void setValue(Double measure) throws PoorlyDefinedMeasureException {
		setBrightness(calculateBrightness(measure));
	}

	private double calculateBrightness(Double measure) throws PoorlyDefinedMeasureException {
		if(measure < 0 || measure.isNaN() || measure.isInfinite())
			throw new PoorlyDefinedMeasureException("Measure is poorly defined");
		// Linear algorithm to select right brightness
		// Can add a strategy to define different algorithms
		return (measure >= saturationLuxAmount) ? 1 : measure/saturationLuxAmount;
	}

}
