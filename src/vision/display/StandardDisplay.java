  package vision.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import vision.ComunicationChannel;
import vision.display.interfaces.video.VideoInterface;
import vision.display.mulfunction.MulfunctionChecker;
import vision.exception.AbsentVideoInterfaceException;

public final class StandardDisplay implements Display {
	
	public static class DisplayCurrentValues {
		
		private int currentNits;
		private int colorTemperature;
		private String resolution;
		private VideoInterface selectedInterface;
		private List<VideoInterface> connectedInterfaces;

		public DisplayCurrentValues(int nits, int colorTemperature,
				String resolution, List<VideoInterface> connectedInterfaces,
				VideoInterface selectedInterface
				) throws AbsentVideoInterfaceException {
			if(nits < 0)
				throw new IllegalArgumentException("Negative nits argument");
			currentNits = nits;
			if(colorTemperature < 0 || colorTemperature > 10)
				throw new IllegalArgumentException("Not acceptable colorTemperature argument: must be between 0 and 10");
			this.colorTemperature = colorTemperature;
			this.resolution = Objects.requireNonNull(
					resolution, 
					"Null resolution argument");
			this.connectedInterfaces = Objects.requireNonNullElse(
					connectedInterfaces, 
					new ArrayList<>());
			if(!connectedInterfaces.contains(selectedInterface))
				throw new AbsentVideoInterfaceException("Selected interface not connected");
			this.selectedInterface = selectedInterface;
			this.connectedInterfaces = connectedInterfaces;
		}
	}

	private final int maxNits;
	private final List<String> supportedResolutions;
	private final ComunicationChannel channel;
	private final List<VideoInterface> supportedInterfaces;
	private DisplayCurrentValues currentValues;
	
	public StandardDisplay(int maxNits,
			List<String> supportedResolutions,
			ComunicationChannel channel, 
			List<VideoInterface> supportedInterfaces, 
			DisplayCurrentValues selectedValues) throws AbsentVideoInterfaceException {
		
		this.maxNits = checkedMaxNits(maxNits);
		this.supportedResolutions = checkedSupportedResolutions(supportedResolutions);
		this.channel = checkedChannel(channel);
		this.supportedInterfaces = checkedSupportedInterfaces(supportedInterfaces);
		currentValues = checkedSelectedValuesOrDefaultValues(maxNits, supportedResolutions, selectedValues);
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

	private ComunicationChannel checkedChannel(ComunicationChannel channel) {
		return Objects.requireNonNull(
				channel, 
				"Null channel argument");
	}

	private List<VideoInterface> checkedSupportedInterfaces(List<VideoInterface> supportedInterfaces) {
		if(Objects.requireNonNull(
				supportedInterfaces, 
				"Null availableInterfaces argument")
			.isEmpty())
			throw new IllegalArgumentException("Empty availableInterfaces argument");
		return supportedInterfaces;
	}
	
	private DisplayCurrentValues checkedSelectedValuesOrDefaultValues(int maxNits, List<String> supportedResolutions,
			DisplayCurrentValues selectedValues) throws AbsentVideoInterfaceException {
		if(selectedValues.connectedInterfaces
				.stream().anyMatch(
						connected -> !supportedInterfaces.contains(connected)))
			throw new AbsentVideoInterfaceException("Connected interface is not supported");
		return Objects.requireNonNullElse(
				currentValues, 
				new DisplayCurrentValues(
						maxNits/2, 
						selectedValues.colorTemperature, 
						(!supportedResolutions.contains(selectedValues.resolution))?
								selectedValues.resolution:
								supportedResolutions.stream()
									.findAny().orElseThrow(),
						selectedValues.connectedInterfaces, 
						selectedValues.connectedInterfaces.get(0)));
	}
	
	@Override
	public void displayStream() {
		channel.sendStream(ResolutionCompressor.compress(currentValues.selectedInterface.getStream()));
	}
		
	@Override
	public void displayMenu() {
		channel.sendRequest("Menu");
	}
	
	@Override
	public void displayError(String message) {
		channel.sendRequest("Error: " + message);
	}
	
	public List<VideoInterface> getSupportedInterfaces(){
		return Collections.unmodifiableList(supportedInterfaces);
	}
			
	@Override
	public void setBrightness(double newBrightness){
		if(newBrightness < 0 || newBrightness > 1)
			throw new IllegalArgumentException("Brightness value not acceptable: Defined from 0 to 1");
		currentValues.currentNits = (int) newBrightness * maxNits;
	}
	
	@Override
	public double getBrightness(){
		return (double)currentValues.currentNits / maxNits;
	}
	
	@Override
	public void setColorTemperature(int newTemperature) {
		if(newTemperature < 0 || newTemperature > 10)
			throw new IllegalArgumentException("Not acceptable colorTemperature argument: must be between 0 and 10");
		currentValues.colorTemperature = newTemperature;
	}
	
	@Override
	public int getColorTemperature() {
		return currentValues.colorTemperature;
	}
	
	@Override
	public void setResolution(String resolution) {
		if(!supportedResolutions.contains(
				Objects.requireNonNull(
						resolution, 
						"Null resolution argument")))
			throw new IllegalArgumentException("Not supported selected resolution");
		currentValues.resolution = resolution;
	}
	
	@Override
	public String getResolution() {
		return currentValues.resolution;
	}
	
	@Override
	public boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException {
		if(!supportedInterfaces.contains(videoInterface))
			throw new AbsentVideoInterfaceException("Impossible connection: Display is not provided with specified video interface");
		return currentValues.connectedInterfaces.add(videoInterface);
	}

	@Override
	public boolean disconnectInterface(VideoInterface videoInterface) {
		return currentValues.connectedInterfaces.remove(videoInterface);
	}
	
	@Override
	public List<VideoInterface> getConnectedInterfaces() {
		return Collections.unmodifiableList(currentValues.connectedInterfaces);
	}
	
	@Override
	public void selectInputInterface(VideoInterface videoIterface) {
		if(!currentValues.connectedInterfaces.contains(videoIterface))
			channel.sendRequest("Input error: Interface not present");
		currentValues.selectedInterface = videoIterface;
	}
	
	@Override
	public VideoInterface getSelectedInputInterface() {
		return currentValues.selectedInterface;
	}

	@Override
	public String mulfunctionTest(MulfunctionChecker checker) {
		return checker.checkMulfunction(this);
	}

}
