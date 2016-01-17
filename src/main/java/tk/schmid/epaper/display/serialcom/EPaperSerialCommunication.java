package tk.schmid.epaper.display.serialcom;

import java.nio.ByteBuffer;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.schmid.epaper.display.protocol.DisplayCommand;
import tk.schmid.epaper.display.protocol.DisplayConstants;

public class EPaperSerialCommunication {

	private static final Logger log = LoggerFactory.getLogger(EPaperSerialCommunication.class);

	private static final int COMMAND_TIMEOUT_IN_MS = 5000;
	private static final int BAUDRATE = 115200;

	private static final int COMMAND_PART_LENGTH = 1;
	private static final int FRAME_LENGTH_PARTL_ENGTH = 2;
	private static final int PARITIY_BYTE_PARTL_ENGTH = 1;

	private String deviceName;
	private SerialPort port;

	public EPaperSerialCommunication(String deviceName) {
		this.deviceName = deviceName;
	}

	public void connect() {
		try {
			log.debug("Connecting to device {}", this.deviceName);

			SerialPort comPort = new SerialPort(this.deviceName);
			comPort.openPort();
			comPort.setParams(BAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			this.port = comPort;
			log.trace("Connected");

		} catch (SerialPortException e) {
			log.error("Error reading serial device.", e);
			log.error("Used Device {}", this.deviceName, e);
			log.debug("Available devices:");
			String[] portNames = SerialPortList.getPortNames();
			for (int i = 0; i < portNames.length; i++) {
				log.debug(portNames[i]);
			}
			throw new EPaperCommunicationException(e);
		}
	}

	public byte[] sendCommand(DisplayCommand command) {
		return sendCommand(command, new byte[0]);
	}

	public byte[] sendCommand(DisplayCommand command, byte[] parameters) {
		log.trace("Sending command: " + command);
		try {
			byte[] frameToTransmit = buildTransmissionFrame(command, parameters);
			port.writeBytes(frameToTransmit);

			byte[] response = waitForResponse();
			return response;

		} catch (SerialPortException e) {
			throw new EPaperCommunicationException(e);
		}
	}

	private byte[] waitForResponse() {
		try {
			long startTime = System.currentTimeMillis();

			do {
				Thread.sleep(10);
				long currentTime = System.currentTimeMillis();

				if (currentTime - startTime > COMMAND_TIMEOUT_IN_MS) {
					throw new EPaperCommunicationException("Timeout waiting for answer from display.");
				}
			} while (port.getInputBufferBytesCount() == 0);

			
			return port.readBytes();
		} catch (InterruptedException e) {
			throw new EPaperCommunicationException("Exception during waiting for answer from display.", e);
		} catch (SerialPortException e) {
			throw new EPaperCommunicationException("Exception during communication with display.", e);
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

	private byte calculateParityByte(byte[] frame) {
		byte parityByte = (byte) 0x00;

		for (int i = 0; i < frame.length; i++) {
			parityByte ^= frame[i];
		}

		return parityByte;
	}
}
