package tk.schmid.epaper.display.serialcom;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.schmid.epaper.display.protocol.DisplayCommand;
import tk.schmid.epaper.display.protocol.DisplayConstants;

public class EPaperSerialCommunication {

	private static final Logger log = LoggerFactory.getLogger(EPaperSerialCommunication.class);

	private static final int CONNECT_TIMEOUT_IN_MS = 1000;
	private static final int COMMAND_TIMEOUT_IN_MS = 5000;
	private static final int BAUDRATE = 115200;

	private static final int COMMAND_PART_LENGTH = 1;
	private static final int FRAME_LENGTH_PARTL_ENGTH = 2;
	private static final int PARITIY_BYTE_PARTL_ENGTH = 1;
	private static final int MAXIUM_SUPPORTED_ANSWER_SIZE_IN_CHARS = 128;

	private static final String CONNECT_IDENTIFIER = "RaspberryPiEPaperDriver";
	private String deviceName;

	public EPaperSerialCommunication(String deviceName) {
		this.deviceName = deviceName;
	}

	private OutputStream outputStream;
	private InputStream inputStream;
	private InputStreamReader inputStreamReader;

	public void connect() {
		try {
			log.debug("Connecting to device {}", this.deviceName);

			CommPortIdentifier portIdentifier = obtainPortIdentifier();
			SerialPort comPort = obtainSerialComPort(portIdentifier);
			comPort.setSerialPortParams(BAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			log.trace("Getting output stream for device...");
			outputStream = comPort.getOutputStream();
			inputStream = comPort.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);

			log.trace("Connected");

		} catch (NoSuchPortException e) {
			log.error("Given Device {} could not be found!", this.deviceName, e);
			throw new EPaperCommunicationException(e);
		} catch (PortInUseException e) {
			log.error("Given Device is already in use!", e);
			throw new EPaperCommunicationException(e);
		} catch (UnsupportedCommOperationException e) {
			log.error("Given Configuration is not supported by device!", e);
			throw new EPaperCommunicationException(e);
		} catch (IOException e) {
			log.error("Error reading device!", e);
			throw new EPaperCommunicationException(e);
		}
	}

	private CommPortIdentifier obtainPortIdentifier() throws NoSuchPortException {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(this.deviceName);

		if (portIdentifier.isCurrentlyOwned()) {
			log.error("Device is already in use!");
			throw new EPaperCommunicationException("Device is already in use");
		}
		log.trace("Got port identifier {}", portIdentifier.getName());
		return portIdentifier;
	}

	private SerialPort obtainSerialComPort(CommPortIdentifier portIdentifier) throws PortInUseException {
		CommPort commPort = portIdentifier.open(CONNECT_IDENTIFIER, CONNECT_TIMEOUT_IN_MS);

		if (!(commPort instanceof SerialPort)) {
			log.error("Device is not a serial interface!");
			throw new EPaperCommunicationException("Device is not a serial interface");
		}

		log.trace("Got port {}", commPort);

		SerialPort serialPort = (SerialPort) commPort;

		return serialPort;
	}

	public byte[] sendCommand(DisplayCommand command) {
		return sendCommand(command, new byte[0]);
	}

	public byte[] sendCommand(DisplayCommand command, byte[] parameters) {
		log.trace("Sending command: " + command);
		try {
			byte[] frameToTransmit = buildTransmissionFrame(command, parameters);
			outputStream.write(frameToTransmit);
			outputStream.flush();

			waitTillResponseIsAvailable();

			byte[] response = readResponse();
			return response;
		} catch (IOException e) {
			throw new EPaperCommunicationException(e);
		} catch (InterruptedException e) {
			throw new EPaperCommunicationException(e);
		}
	}

	private byte[] readResponse() throws IOException, InterruptedException {
		waitTillResponseIsAvailable();

		int charsAvailable = inputStream.available();

		if (charsAvailable >= MAXIUM_SUPPORTED_ANSWER_SIZE_IN_CHARS) {
			log.error("More data read than excpected");
			throw new EPaperCommunicationException("More data read than excpected");
		}

		char[] responseBuffer = new char[charsAvailable];
		inputStreamReader.read(responseBuffer);

		String response = new String(responseBuffer).trim();

		log.trace("Received response: " + response);

		return response.getBytes();
	}

	private void waitTillResponseIsAvailable() {
		try {
			long startTime = System.currentTimeMillis();
			while (inputStream.available() < 1) {
				Thread.sleep(10);
				long currentTime = System.currentTimeMillis();

				if (currentTime - startTime > COMMAND_TIMEOUT_IN_MS) {
					throw new EPaperCommunicationException("Timeout waiting for answer from display.");
				}
			}
		} catch (IOException e) {
			throw new EPaperCommunicationException("Exception during waiting for answer from display.", e);
		} catch (InterruptedException e) {
			throw new EPaperCommunicationException("Exception during waiting for answer from display.", e);
		}
	}

	private byte[] buildTransmissionFrame(DisplayCommand command, byte[] parameters) {
		int frameLength = DisplayConstants.FRAME_HEADER.length + FRAME_LENGTH_PARTL_ENGTH + COMMAND_PART_LENGTH + parameters.length + DisplayConstants.Frame_END.length + PARITIY_BYTE_PARTL_ENGTH;

		ByteBuffer frameToTransmit = ByteBuffer.allocate(frameLength);
		frameToTransmit.put(DisplayConstants.FRAME_HEADER);
		frameToTransmit.putShort((short) frameLength);
		frameToTransmit.put(command.getCommandCode());
		frameToTransmit.put(parameters);
		frameToTransmit.put(DisplayConstants.Frame_END);

		byte parityByte = calculateParityByte(frameToTransmit.array());
		frameToTransmit.put(parityByte);

		return frameToTransmit.array();
	}

	private void debugPrint(byte[] frameToTransmit, byte[] parameters) {
		for (int i = 0; i < frameToTransmit.length; i++) {

			byte b = frameToTransmit[i];
			if (i == 0) {
				System.out.print("Header: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == 1) {
				System.out.print("Length A: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == 2) {
				System.out.print("Length B: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == 3) {
				System.out.print("Command: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == 4 && parameters.length > 0) {
				System.out.print("Parameters: ");
				System.out.format("%x ", b);
			} else if (i == frameToTransmit.length - 5) {
				System.out.println();
				System.out.print("Ende A: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == frameToTransmit.length - 4) {
				System.out.print("Ende B: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == frameToTransmit.length - 3) {
				System.out.print("Ende C: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == frameToTransmit.length - 2) {
				System.out.print("Ende D: ");
				System.out.format("%x ", b);
				System.out.println();
			} else if (i == frameToTransmit.length - 1) {
				System.out.print("Parity: ");
				System.out.format("%x ", b);
				System.out.println();
			} else {
				System.out.format("%x ", b);
			}
		}
		System.out.println("----");
	}

	private byte calculateParityByte(byte[] frame) {
		byte parityByte = (byte) 0x00;

		for (int i = 0; i < frame.length; i++) {
			parityByte ^= frame[i];
		}

		return parityByte;
	}
}
