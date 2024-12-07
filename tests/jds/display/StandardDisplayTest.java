package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import jds.MockStreamReciver;
import jds.MockStreamSender;
import jds.StreamReceiver;
import jds.StreamSender;
import jds.display.interfaces.VideoInterface;
import jds.display.malfunction.MalfunctionChecker;
import jds.display.malfunction.UnexpectedlyChangedResolutionChecker;
import jds.exception.AbsentVideoInterfaceException;

public class StandardDisplayTest {

	int maxNits;
	List<String> supportedResolutions;
	StreamSender sender;
	List<VideoInterface> supportedInterfaces;
	StreamReceiver reciver;
	VideoInterface videoInterface;
	Display display;
	
	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions= List.of("1366x768", "720p");
		sender = new MockStreamSender();
		reciver = new MockStreamReciver();
		videoInterface = new VideoInterface("VGA", "WVGA", reciver);
		supportedInterfaces = List.of(videoInterface);
		display = new StandardDisplay(maxNits, supportedResolutions, sender, supportedInterfaces);
	}

	@Test
	public void testStandardDisplay() {
		negativeNitsExceptionCheck();
		nullSupportedResolutionExceptionCheck();
		emptySupportedResolutionExceptionCheck();
		nullSenderExceptionCheck();
		nullSupportedInterfacesExceptionCheck();
		emptySupportedInterfacesExceptionCheck();
		assertThat(display)
			.isInstanceOf(Display.class)
			.hasNoNullFieldsOrProperties();
		assertThat(display.getBrightness())
			.isEqualTo(0.5);
	}

	private void negativeNitsExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(-1, supportedResolutions, sender, supportedInterfaces))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Negative maxNits argument");
	}
	
	private void nullSupportedResolutionExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(maxNits, null, sender, supportedInterfaces))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null supportedResolution argument");
	}

	private void emptySupportedResolutionExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(maxNits, List.of(), sender, supportedInterfaces))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty supportedResolution argument");
	}
	
	private void nullSenderExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(maxNits, supportedResolutions, null, supportedInterfaces))
		.isInstanceOf(NullPointerException.class)
		.hasMessage("Null channel argument");
	}

	private void nullSupportedInterfacesExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(maxNits, supportedResolutions, sender, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null supportedInterfaces argument");
	}

	private void emptySupportedInterfacesExceptionCheck() {
		assertThatThrownBy(
				() -> new StandardDisplay(maxNits, supportedResolutions, sender, List.of()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty supportedInterfaces argument");
	}
	
	@Test
	public void testDisplayStream() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> display.displayStream())
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage("Not selected interface");
		display.connectInterface(videoInterface);
		((StandardDisplay)display).forceInputInterface(videoInterface);
		display.displayStream();
		assertThat(
				((MockStreamSender) sender).sentStreams.get(0))
			.isSameAs(videoInterface.getStream());
	}

	@Test
	public void testDisplayMenu() {
		display.displayMenu();
		assertThat(
				((MockStreamSender) sender).requests.get(0))
			.isEqualTo("Menu");
	}

	@Test
	public void testDisplayError() {
		display.displayError("error test");
		assertThat(
				((MockStreamSender) sender).requests.get(0))
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
		assertThatThrownBy(
				() -> display.setBrightness(-0.1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		assertThatThrownBy(
				() -> display.setBrightness(5))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		display.setBrightness(0.8);
		assertThat(display.getBrightness())
			.isEqualTo(0.8);
	}

	@Test
	public void testGetBrightness() {
		assertThat(display.getBrightness())
			.isEqualTo(display.getBrightness());
	}

	@Test
	public void testSetColorTemperature() {
        assertThatThrownBy(
        		() -> display.setColorTemperature(-1))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        assertThatThrownBy(
        		() -> display.setColorTemperature(11))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        display.setColorTemperature(9);
        assertThat(display.getColorTemperature())
        	.isEqualTo(9);
	}

	@Test
	public void testSetResolution() {
		assertThatThrownBy(
				() -> display.setResolution(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null resolution argument");
		assertThatThrownBy(
				() -> display.setResolution("1440p"))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Selected resolution is not supported");
        assertThat(display.getResolution())
    		.isEqualTo("1366x768");
        display.setResolution("720p");
        assertThat(display.getResolution())
        	.isEqualTo("720p");
	}

	@Test
	public void testConnectInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> display.connectInterface(
						new VideoInterface("HDMI",
								"1.1", 
								new MockStreamReciver())))
        	.isInstanceOf(AbsentVideoInterfaceException.class)
        	.hasMessage("Impossible connection: Display is not provided with specified video interface");
        assertThat(display.connectInterface(videoInterface))
        	.isTrue();
        assertThat(display.getConnectedInterfaces())
        	.contains(videoInterface);
	}

	@Test
	public void testDisconnectInterface() throws AbsentVideoInterfaceException {
		display.connectInterface(videoInterface);
        assertThat(display.disconnectInterface(videoInterface))
    		.isTrue();
        assertThat(display.getConnectedInterfaces())
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
		assertThatThrownBy(
				() -> display.selectInputInterface(videoInterface))
			.isInstanceOf(AbsentVideoInterfaceException.class)
			.hasMessage("Selected interface is not conneted");
		display.connectInterface(videoInterface);
		display.selectInputInterface(videoInterface);	
		assertThat(display.getSelectedInterface())
			.isSameAs(videoInterface);
		}

	@Test
	public void testGetSelectedInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(() -> display.getSelectedInterface())
			.isInstanceOf(NoSuchElementException.class);
		display.connectInterface(videoInterface);
		((StandardDisplay)display).forceInputInterface(videoInterface);
		assertThat(display.getSelectedInterface())
			.isEqualTo(videoInterface);
	}

	@Test
	public void testMulfunctionTest() {
		MalfunctionChecker checker = new UnexpectedlyChangedResolutionChecker(null);
		assertThat(display.malfunctionTest(checker))
			.isEqualTo("No mulfunction detected");
	}

}
