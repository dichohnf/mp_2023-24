  package jds.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import jds.StreamSender;
import jds.display.interfaces.VideoInterface;
import jds.display.malfunction.MalfunctionChecker;
import jds.exception.AbsentVideoInterfaceException;

public final class StandardDisplay implements Display {
	
	private static final int MIN_BRIGHTNESS = 0;
	private static final int MAX_BRIGHTNESS = 1;
	private static final int MIN_COLOR_TEMPERATURE = 0;
	private static final int MAX_COLOR_TEMPERATURE = 10;
	
	private final int maxNits;
	private final List<String> supportedResolutions;
	private final StreamSender channel;
	private final List<VideoInterface> supportedInterfaces;
	private int currentNits;
	private int colorTemperature;
	private String resolution;
	private Optional<VideoInterface> selectedInterface;
	private List<VideoInterface> connectedInterfaces;
	
	public StandardDisplay(int maxNits,
			List<String> supportedResolutions,
			StreamSender channel,
			List<VideoInterface> supportedInterfaces) {
		
		this.maxNits = checkedMaxNits(maxNits);
		this.supportedResolutions = checkedSupportedResolutions(supportedResolutions);
		this.channel = checkedChannel(channel);
		this.supportedInterfaces = checkedSupportedInterfaces(supportedInterfaces);
		setBrightness(0.5);
		setColorTemperature(5);
		setResolution(supportedResolutions.stream()
				.findAny().orElseThrow());
		connectedInterfaces = new ArrayList<>();
		selectedInterface = Optional.empty();
	}
	
	private int checkedMaxNits(int maxNits) {
		if(maxNits < 0) 
			throw new IllegalArgumentException("Negative maxNits argument");
		return maxNits;
	}

	private List<String> checkedSupportedResolutions(List<String> supportedResolutions) {
		if(Objects.requireNonNull(
				supportedResolutions, 
				"Null supportedResolution argument")
				.isEmpty())
			throw new IllegalArgumentException("Empty supportedResolution argument");
		return supportedResolutions;
	}

	private StreamSender checkedChannel(StreamSender channel) {
		return Objects.requireNonNull(
				channel, 
				"Null channel argument");
	}

	private List<VideoInterface> checkedSupportedInterfaces(List<VideoInterface> supportedInterfaces) {
		if(Objects.requireNonNull(
				supportedInterfaces, 
				"Null supportedInterfaces argument")
			.isEmpty())
			throw new IllegalArgumentException("Empty supportedInterfaces argument");
		return supportedInterfaces;
	}
	
	@Override
	public void displayStream() {
		channel.sendStream(
				selectedInterface.orElseThrow(
						() -> new NoSuchElementException("Not selected interface"))
				.getStream());
	}
		
	@Override
	public void displayMenu() {
		channel.sendRequest("Menu");
	}
	
	@Override
	public void displayError(String message) {
		channel.sendRequest("Error: " + message);
	}
	
	@Override
	public List<VideoInterface> getSupportedInterfaces(){
		return Collections.unmodifiableList(supportedInterfaces);
	}
			
	@Override
	public void setBrightness(double newBrightness){
		if(newBrightness < MIN_BRIGHTNESS || newBrightness > MAX_BRIGHTNESS)
			throw new IllegalArgumentException("Brightness value not acceptable: Defined from 0 to 1");
		currentNits = (int) (newBrightness * maxNits);
	}
	
	@Override
	public double getBrightness(){
		return (double) currentNits / maxNits;
	}
	
	@Override
	public void setColorTemperature(int newTemperature) {
		if(newTemperature < MIN_COLOR_TEMPERATURE || newTemperature > MAX_COLOR_TEMPERATURE)
			throw new IllegalArgumentException("Not acceptable colorTemperature argument: must be between 0 and 10");
		colorTemperature = newTemperature;
	}
	
	@Override
	public int getColorTemperature() {
		return colorTemperature;
	}
	
	@Override
	public void setResolution(String resolution) {
		if(!supportedResolutions.contains(
				Objects.requireNonNull(
						resolution, 
						"Null resolution argument")))
			throw new IllegalArgumentException("Selected resolution is not supported");
		this.resolution = resolution;
	}
	
	// Only for test
	void forceResolution(String resolution) {
		this.resolution = resolution;
	}
	
	@Override
	public String getResolution() {
		return resolution;
	}
	
	@Override
	public boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		if(!supportedInterfaces.contains(videoInterface))
			throw new AbsentVideoInterfaceException("Impossible connection: Display is not provided with specified video interface");
		return connectedInterfaces.add(videoInterface);
	}

	@Override
	public boolean disconnectInterface(VideoInterface videoInterface) {
		return connectedInterfaces.remove(videoInterface);
	}
	
	@Override
	public List<VideoInterface> getConnectedInterfaces() {
		return Collections.unmodifiableList(connectedInterfaces);
	}
	
	@Override
	public void selectInputInterface(VideoInterface videoIterface) throws AbsentVideoInterfaceException {
		if(!connectedInterfaces.contains(
				Objects.requireNonNull(
						videoIterface, 
						"Null videoInterface argument"))) {
			channel.sendRequest("Input error: Interface not present");
			throw new AbsentVideoInterfaceException("Selected interface is not conneted");
		}
		selectedInterface = Optional.of(videoIterface);
	}
	
	// Only for test
	void forceInputInterface(VideoInterface videoInterface) {
		selectedInterface = Optional.ofNullable(videoInterface);
	}
	
	@Override
	public VideoInterface getSelectedInterface() {
		return selectedInterface.orElseThrow();
	}

	@Override
	public String malfunctionTest(MalfunctionChecker checker) {
		return checker.checkMulfunction(this);
	}

}
