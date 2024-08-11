package vision.display;

import java.util.List;
import java.util.Objects;

import vision.display.interfaces.video.VideoInterface;
import vision.display.mulfunction.MulfunctionChecker;
import vision.display.sensor.Sensor;
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
	public void displayStream() {
		setBestConfiguration();
		component.displayStream();
	}
	
	@Override
	public final void displayMenu() {
		setBestConfiguration();
		component.displayMenu();
	}
	
	@Override
	public void displayError(String message) {
		setBestConfiguration();
		component.displayError(message);
	}
	
	@Override
	public final void setBrightness(double newBrightness) {
		component.setBrightness(newBrightness);
	}
	
	@Override
	public double getBrightness() {
		return component.getBrightness();
	}
		
	@Override
	public final void setColorTemperature(int newTemperature) {
		component.setColorTemperature(newTemperature);
	}
	
	@Override
	public int getColorTemperature() {
		return component.getColorTemperature();
	}
	
	public void setResolution(String resolution) {
		component.setResolution(resolution);
	}
	
	public String getResolution() {
		return component.getResolution();
	}
	
	@Override
	public final boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		return component.connectInterface(videoInterface);
	}

	@Override
	public final boolean disconnectInterface(VideoInterface videoInterface) {
		return component.disconnectInterface(videoInterface);
	}
	
	public List<VideoInterface> getConnectedInterfaces(){
		return component.getConnectedInterfaces();
	}
	
	@Override
	public final void selectInputInterface(VideoInterface videoIterface) {
		component.selectInputInterface(videoIterface);
	}
	
	@Override
	public VideoInterface getSelectedInputInterface() {
		return component.getSelectedInputInterface();
	}
	
	@Override
	public String mulfunctionTest(MulfunctionChecker checker) {
		return component.mulfunctionTest(checker);
	}
	
	public void setBestConfiguration() {
		try {
			setValue(sensor.getMeasure());
		} catch (PoorlyDefinedMeasureException e) {
			e.printStackTrace();
		}
	}

	protected abstract void setValue(T measure) throws PoorlyDefinedMeasureException;
	
}
