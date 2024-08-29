package jds.display;

import java.util.List;
import java.util.Objects;

import jds.Sensor;
import jds.display.interfaces.VideoInterface;
import jds.display.malfunction.MalfunctionChecker;
import jds.exception.AbsentVideoInterfaceException;
import jds.exception.PoorlyDefinedMeasureException;

public abstract class DisplayWithSensorDecorator<T> implements Display{

	final Display component;
	private final Sensor<T> sensor;
	
	protected DisplayWithSensorDecorator(Sensor<T> sensor, Display component) {
		this.sensor = Objects.requireNonNull(
				sensor, 
				"Null sensor argument");
		this.component = Objects.requireNonNull(
				component, 
				"Null component argument");
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
	public List<VideoInterface> getSupportedInterfaces() {
		return component.getSupportedInterfaces();
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
	
	@Override
	public List<VideoInterface> getConnectedInterfaces(){
		return component.getConnectedInterfaces();
	}
	
	@Override
	public final void selectInputInterface(VideoInterface videoIterface) throws AbsentVideoInterfaceException {
		component.selectInputInterface(videoIterface);
	}
	
	@Override
	public VideoInterface getSelectedInterface() {
		return component.getSelectedInterface();
	}
	
	@Override
	public String malfunctionTest(MalfunctionChecker checker) {
		return component.malfunctionTest(checker);
	}
	
	public void setBestConfiguration() {
		try {
			setValue(sensor.getMeasure());
		} catch (PoorlyDefinedMeasureException e) {
			e.printStackTrace();
		}
	}

	protected abstract void setValue(T measure) throws PoorlyDefinedMeasureException;
	
	Display getComponent() {
		return component;
	}
	
}
