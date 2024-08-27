package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import jds.StreamChannel;
import jds.MockStreamChannel;
import jds.display.interfaces.VideoInterface;
import jds.display.mulfunction.MockMulfunctionChecker;
import jds.display.mulfunction.MulfunctionChecker;
import jds.exception.AbsentVideoInterfaceException;

public class StandardDisplayTest {

	int maxNits;
	List<String> supportedResolutions;
	StreamChannel displayChannel;
	List<VideoInterface> supportedInterfaces;
	StreamChannel interfaceChannel;
	VideoInterface videoInterface;
	Display display;
	
	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions= List.of("1366x768", "720p");
		displayChannel = new MockStreamChannel();
		interfaceChannel = new MockStreamChannel();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		display = new StandardDisplay(maxNits, supportedResolutions, displayChannel, supportedInterfaces);
	}

	@Test
	public void testStandardDisplay() {
		negativeNitsExceptionCheck();
		nullSupportedResolutionExceptionCheck();
		emptySupportedResolutionExceptionCheck();
		nullChannelExceptionCheck();
		nullSupportedInterfacesExceptionCheck();
		emptySupportedInterfacesExceptionCheck();
		assertThat(display)
			.isInstanceOf(Display.class)
			.hasNoNullFieldsOrProperties();
		assertThat(((StandardDisplay)display).currentNits).isEqualTo(300);
	}

	private void negativeNitsExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(-1, supportedResolutions, displayChannel, supportedInterfaces))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Negative maxNits argument");
	}
	
	private void nullSupportedResolutionExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(maxNits, null, displayChannel, supportedInterfaces))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null supportedResolution argument");
	}

	private void emptySupportedResolutionExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(maxNits, List.of(), displayChannel, supportedInterfaces))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty supportedResolution argument");
	}
	
	private void nullChannelExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(maxNits, supportedResolutions, null, supportedInterfaces))
		.isInstanceOf(NullPointerException.class)
		.hasMessage("Null channel argument");
	}

	private void nullSupportedInterfacesExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(maxNits, supportedResolutions, displayChannel, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null supportedInterfaces argument");
	}

	private void emptySupportedInterfacesExceptionCheck() {
		assertThatThrownBy(() -> new StandardDisplay(maxNits, supportedResolutions, displayChannel, List.of()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty supportedInterfaces argument");
	}
	
	@Test
	public void testDisplayStream() {
		assertThatThrownBy(() -> display.displayStream())
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage("Not selected interface");
		((StandardDisplay)display).connectedInterfaces.add(videoInterface);
		((StandardDisplay)display).selectedInterface = Optional.of(videoInterface);
		display.displayStream();
		assertThat(
				((MockStreamChannel) displayChannel).sentStreams.get(0))
			.isSameAs(videoInterface.getStream());
	}

	@Test
	public void testDisplayMenu() {
		display.displayMenu();
		assertThat(
				((MockStreamChannel) displayChannel).requests.get(0))
			.isEqualTo("Menu");
	}

	@Test
	public void testDisplayError() {
		display.displayError("error test");
		assertThat(
				((MockStreamChannel) displayChannel).requests.get(0))
			.isEqualTo("Error: error test");
	}

	@Test
	public void testGetSupportedInterfaces() {
		assertThat(display.getSupportedInterfaces())
			.isEqualTo(supportedInterfaces)
			.isUnmodifiable();
	}

	@Test
	public void testSetBrightness() {
		assertThatThrownBy(() -> display.setBrightness(-0.1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		assertThatThrownBy(() -> display.setBrightness(5))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		display.setBrightness(0.8);
		assertThat(((StandardDisplay)display).currentNits)
			.isEqualTo((int) (0.8 * maxNits));
	}

	@Test
	public void testGetBrightness() {
		assertThat(display.getBrightness())
			.isEqualTo((double) ((StandardDisplay)display).currentNits / maxNits);
	}

	@Test
	public void testSetColorTemperature() {
        assertThatThrownBy(() -> display.setColorTemperature(-1))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        assertThatThrownBy(() -> display.setColorTemperature(11))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        display.setColorTemperature(9);
        assertThat(((StandardDisplay)display).colorTemperature)
        	.isEqualTo(9);
	}

	@Test
	public void testSetResolution() {
		assertThatThrownBy(() -> display.setResolution(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null resolution argument");
		assertThatThrownBy(() -> display.setResolution("1440p"))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Selected resolution is not supported");
        assertThat(((StandardDisplay)display).resolution)
    	.isEqualTo("1366x768");
        display.setResolution("720p");
        assertThat(((StandardDisplay)display).resolution)
        	.isEqualTo("720p");
	}

	@Test
	public void testConnectInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> display.connectInterface(
						new VideoInterface("HDMI",
								"1.1", 
								new MockStreamChannel())))
        	.isInstanceOf(AbsentVideoInterfaceException.class)
        	.hasMessage("Impossible connection: Display is not provided with specified video interface");
        assertThat(display.connectInterface(videoInterface))
        	.isTrue();
        assertThat(((StandardDisplay)display).connectedInterfaces)
        	.contains(videoInterface);
	}

	@Test
	public void testDisconnectInterface() {
		((StandardDisplay)display).connectedInterfaces.add(videoInterface);
        assertThat(display.disconnectInterface(videoInterface))
    		.isTrue();
        assertThat(((StandardDisplay)display).connectedInterfaces)
    		.doesNotContain(videoInterface);
        assertThat(display.disconnectInterface(videoInterface))
    		.isFalse();
	}

	@Test
	public void testGetConnectedInterfaces() {
		assertThat(display.getConnectedInterfaces())
			.isUnmodifiable()
			.isEmpty();
	}

	@Test
	public void testSelectInputInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(() -> display.selectInputInterface(videoInterface))
			.isInstanceOf(AbsentVideoInterfaceException.class)
			.hasMessage("Selected interface is not conneted");
		((StandardDisplay)display).connectedInterfaces.add(videoInterface);
		display.selectInputInterface(videoInterface);	
		assertThat(((StandardDisplay)display).selectedInterface)
			.isPresent()
			.contains(videoInterface);
		}

	@Test
	public void testGetSelectedInterface() {
		assertThatThrownBy(() -> display.getSelectedInterface())
			.isInstanceOf(NoSuchElementException.class);
		((StandardDisplay)display).connectedInterfaces.add(videoInterface);
		((StandardDisplay)display).selectedInterface = Optional.of(videoInterface);
		assertThat(display.getSelectedInterface())
			.isEqualTo(videoInterface);
	}

	@Test
	public void testMulfunctionTest() {
		MulfunctionChecker checker = new MockMulfunctionChecker(null);
		assertThat(display.mulfunctionTest(checker))
			.isEqualTo("No mulfunction detected");
	}

}
