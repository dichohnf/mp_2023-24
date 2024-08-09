package vision.display;

import vision.display.video_interface.VideoInterface;
import vision.exception.AbsentVideoInterfaceException;
import vision.exception.MulfunctionCheckException;

public interface Display {
	
	void displayMenu();
	
	void displayError(String message);
	
	void setBrightness(double newBrightness);

	void setColorTemperature(int newTemperature);
	
	boolean disconnectInterface(VideoInterface videoInterface);

	boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException;

	void selectInputInterface(VideoInterface videoIterface);
	
	void mulfunctionTest() throws MulfunctionCheckException;
	
}