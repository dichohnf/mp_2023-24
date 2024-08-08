package vision.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import vision.display.video_interface.VideoInterface;
import vision.exception.AbsentVideoInterfaceException;

public abstract class Display {

	private final int xPixels;
	private final int yPixels;
	private final int colorBits;
	private final ComunicationChannel channel;
	private final Collection<VideoInterface> availableInterfaces;
	private final Collection<VideoInterface> connectedInterfaces;
	
	protected Display(int xPixels, int yPixels, int colorBits, ComunicationChannel channel,
			Collection<VideoInterface> availableInterfaces, 
			Collection<VideoInterface> connectedInterfaces) {
		this.xPixels = xPixels;
		this.yPixels = yPixels;
		this.colorBits = colorBits;
		if(Objects.requireNonNull(
				availableInterfaces, 
				"Null availableInterfaces argument")
			.isEmpty())
			throw new IllegalArgumentException("Empty availableInterfaces argument");
		this.channel = Objects.requireNonNull(
				channel, 
				"Null channel argument");
		this.availableInterfaces = availableInterfaces;
		this.connectedInterfaces = Objects.requireNonNullElse(
				connectedInterfaces, 
				new ArrayList<>());
	}
	
	protected final ComunicationChannel getChannel() {
		return channel;
	}

	public boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		if(!availableInterfaces.contains(videoInterface))
			throw new AbsentVideoInterfaceException("Impossible connection: Display is not provided with specified video interface");
		return connectedInterfaces.add(videoInterface);
	}

	public boolean disconnectInterface(VideoInterface videoInterface) {
		return connectedInterfaces.remove(videoInterface);
	}

	public abstract void displayMenu();
	
	public abstract void displayTestScreen();
	
}