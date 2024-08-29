package jds.display.malfunction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.MockStreamReciver;
import jds.MockStreamSender;
import jds.StreamReciver;
import jds.StreamSender;
import jds.display.Display;
import jds.display.DisplaySetterToTestMalfunctions;
import jds.display.StandardDisplay;
import jds.display.interfaces.VideoInterface;
import jds.exception.AbsentVideoInterfaceException;

public class UnexpectedlyDisconnectedInterfaceCheckerTest {

	int maxNits;
	List<String> supportedResolutions;
	StreamSender displayChannel;
	List<VideoInterface> supportedInterfaces;
	StreamReciver interfaceChannel;
	VideoInterface videoInterface;
	Display display;
	MalfunctionChecker checker1;
	MalfunctionChecker checker2;

	@Before
	public void setUp() {
		maxNits 			= 600;
		supportedResolutions= List.of("1366x768", "720p");
		displayChannel 		= new MockStreamSender();
		interfaceChannel 	= new MockStreamReciver();
		videoInterface 		= new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		display 			= new StandardDisplay(maxNits, supportedResolutions, displayChannel, supportedInterfaces);
		checker1 			= new UnexpectedlyDisconnectedInterfaceChecker(null);
		checker2 			= new UnexpectedlyChangedResolutionChecker(checker1);
	}

	@Test
	public void testCheckMulfunction() throws AbsentVideoInterfaceException {
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("No connected interface detected");
		display.connectInterface(videoInterface);
		display.selectInputInterface(videoInterface);
		String noMulfunction = "No mulfunction detected";
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo(noMulfunction);
		assertThat(checker2.checkMulfunction(display))
			.isEqualTo(noMulfunction);
		DisplaySetterToTestMalfunctions.setSelectedVideoInterface(
				display, new VideoInterface(
							"HDMI", 
							"1.2", 
							new MockStreamReciver()));
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("Unexpectedly disconnected interface");
		DisplaySetterToTestMalfunctions.setSelectedVideoInterface(
				display, new VideoInterface(
							"HDMI", 
							"1.2", 
							new MockStreamReciver()));
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("Unexpectedly disconnected interface");
	}

}
