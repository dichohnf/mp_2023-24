package vision.display;

import java.util.Objects;

import vision.display.sensor.Sensor;
import vision.display.video_interface.VideoInterface;
import vision.exception.AbsentVideoInterfaceException;
import vision.exception.PoorlyDefinedMeasureException;

public abstract class DisplayWithSensorDecorator<T> implements Display{

	private final Sensor<T> sensor;
	private final Display component;
	
	protected DisplayWithSensorDecorator(Sensor<T> sensor, Display component) {
		this.sensor = Objects.requireNonNull(
				sensor, 
				"Null sensor argument");
		this.component = Objects.requireNonNull(
				component, 
				"Null cmponent argument");
	}
	
	@Override
	public final void setBrightness(double newBrightness) {
		component.setBrightness(newBrightness);
	}
	
	@Override
	public final void setColorTemperature(int newTemperature) {
		component.setColorTemperature(newTemperature);
	}
	
	@Override
	public final boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		return component.connectInterface(videoInterface);
	}

	@Override
	public final boolean disconnectInterface(VideoInterface videoInterface) {
		return component.disconnectInterface(videoInterface);
	}
	
	@Override
	public final void selectInputInterface(VideoInterface videoIterface) {
		component.selectInputInterface(videoIterface);
	}
	
	@Override
	public final void displayMenu() {
		try {
			setBestConfiguration();
		} catch (PoorlyDefinedMeasureException e) {
			e.printStackTrace();
		}
		component.displayMenu();
	}
	
	@Override
	public void displayInputError(String message) {
		try {
			setBestConfiguration();
		} catch (PoorlyDefinedMeasureException e) {
			e.printStackTrace();
		}
		component.displayInputError(message);
	}
	
	public void setBestConfiguration() throws PoorlyDefinedMeasureException {
		setValue(sensor.getMeasure());
	}

	protected abstract void setValue(T measure) throws PoorlyDefinedMeasureException;

	
}
