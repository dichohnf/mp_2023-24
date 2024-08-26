package jds.display.mulfunction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.ComunicationChannel;
import jds.MockComunicationChannel;
import jds.display.Display;
import jds.display.DisplaySetterToTestMulfunctions;
import jds.display.StandardDisplay;
import jds.display.interfaces.VideoInterface;
import jds.exception.AbsentVideoInterfaceException;

public class UnexpectedlyDisconnectedInterfaceCheckerTest {

	int maxNits;
	List<String> supportedResolutions;
	ComunicationChannel displayChannel;
	List<VideoInterface> supportedInterfaces;
	ComunicationChannel interfaceChannel;
	VideoInterface videoInterface;
	Display display;
	MulfunctionChecker checker1;
	MulfunctionChecker checker2;

	@Before
	public void setUp() {
		maxNits 			= 600;
		supportedResolutions= List.of("1366x768", "720p");
		displayChannel 		= new MockComunicationChannel();
		interfaceChannel 	= new MockComunicationChannel();
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
		DisplaySetterToTestMulfunctions.setSelectedVideoInterface(
				display, new VideoInterface(
							"HDMI", 
							"1.2", 
							new MockComunicationChannel()));
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("Unexpectedly disconnected interface");
		DisplaySetterToTestMulfunctions.setSelectedVideoInterface(
				display, new VideoInterface(
							"HDMI", 
							"1.2", 
							new MockComunicationChannel()));
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("Unexpectedly disconnected interface");
	}

}
