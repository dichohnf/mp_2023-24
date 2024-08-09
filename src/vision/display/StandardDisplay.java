package vision.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import vision.ComunicationChannel;
import vision.display.video_interface.VideoInterface;
import vision.exception.AbsentVideoInterfaceException;
import vision.exception.MulfunctionCheckException;

public final class StandardDisplay implements Display {
	
	private final int maxNits;
	private int currentNits;
	private int colorTemperature;
	private final ComunicationChannel channel;
	private VideoInterface selectedInterface;
	private final List<VideoInterface> availableInterfaces;
	private final List<VideoInterface> connectedInterfaces;
	private final Optional<MulfunctionChecker> checker;
	
	
	public StandardDisplay(int maxNits, int colorTemperature,
			ComunicationChannel channel, 
			List<VideoInterface> availableInterfaces, 
			List<VideoInterface> connectedInterfaces,
			MulfunctionChecker checker) {
		if(Objects.requireNonNull(
				availableInterfaces, 
				"Null availableInterfaces argument")
			.isEmpty())
			throw new IllegalArgumentException("Empty availableInterfaces argument");
		if(maxNits < 0) 
			throw new IllegalArgumentException("Negative maxNits argument");
		this.maxNits = maxNits;
		currentNits = maxNits/2;
		setColorTemperature(colorTemperature);
		this.channel = Objects.requireNonNull(
				channel, 
				"Null channel argument");
		this.availableInterfaces = Objects.requireNonNull(
				availableInterfaces, 
				"Null avaiable interfaces list");
		this.connectedInterfaces = Objects.requireNonNullElse(
				connectedInterfaces, 
				new ArrayList<>());
		try {
			selectedInterface = connectedInterfaces.get(0);
		} catch (IndexOutOfBoundsException e) {
			displayError("Input error: Insert a compatible cable");
		}
		this.checker = Optional.ofNullable(checker);
	}
	
	public StandardDisplay(int maxNits, ComunicationChannel channel, List<VideoInterface> availableInterfaces, mulfunctionhandler handler){
		this(maxNits, 5,
				channel, availableInterfaces,
				null, handler);
	}
		
	@Override
	public boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		if(!availableInterfaces.contains(videoInterface))
			throw new AbsentVideoInterfaceException("Impossible connection: Display is not provided with specified video interface");
		return connectedInterfaces.add(videoInterface);
	}

	@Override
	public boolean disconnectInterface(VideoInterface videoInterface) {
		return connectedInterfaces.remove(videoInterface);
	}
	
	@Override
	public void setBrightness(double newBrightness){
		if(newBrightness < 0 || newBrightness > 1)
			throw new IllegalArgumentException("Brightness value not acceptable: Defined from 0 to 1");
		currentNits = (int) newBrightness * maxNits;
	}
	
	@Override
	public void setColorTemperature(int newTemperature) {
		if(newTemperature < 0 || newTemperature > 10)
			throw new IllegalArgumentException("Not acceptable colorTemperature argument: must be between 0 and 10");
		colorTemperature = newTemperature;
	}
	
	@Override
	public void selectInputInterface(VideoInterface videoIterface) {
		if(!connectedInterfaces.contains(videoIterface))
			channel.display("Input error: Interface not present");
		selectedInterface = videoIterface;
	}
	
	@Override
	public void displayMenu() {
		channel.display("Menu");
	}
	
	@Override
	public void displayError(String message) {
		channel.display(message);
	}

	@Override
	public void mulfunctionTest() throws MulfunctionCheckException {
		checker.orElseThrow().checkMulfunction(this);
	}

}
