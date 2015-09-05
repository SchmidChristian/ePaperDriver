package tk.schmid.epaper.display.serialcom;

public class EPaperCommunicationException extends RuntimeException {

	private static final long serialVersionUID = 1080819606328224786L;

	public EPaperCommunicationException(String message) {
		super(message);
	}

	public EPaperCommunicationException(Throwable cause) {
		super(cause);
	}

	public EPaperCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
