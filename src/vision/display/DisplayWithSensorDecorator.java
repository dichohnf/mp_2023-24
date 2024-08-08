package vision.display;

import java.util.Collection;
import java.util.Objects;

import vision.display.video_interface.VideoInterface;

public final class DisplayWithSensorDecorator<T> extends Display{

	private final Sensor<T> sensor;
	
	public DisplayWithSensorDecorator(int xPixels, int yPixels, int colorBits, ComunicationChannel channel,
			Collection<VideoInterface> availableInterfaces, Collection<VideoInterface> connectedInterfaces, Sensor<T> sensor) {
		super(xPixels, yPixels, colorBits, channel, availableInterfaces, connectedInterfaces);
		this.sensor = Objects.requireNonNull(
				sensor, 
				"Null sensor argument");
	}
	
	@Override
	public void displayMenu() {
		T measure = sensor.getMeasure();
		
		
	}
	@Override
	public void displayTestScreen() {
		// TODO Auto-generated method stub
		
	}
	
	

}
