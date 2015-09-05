package tk.schmid.epaper.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.schmid.epaper.display.EPaperDisplay;
import tk.schmid.epaper.display.protocol.DisplayDirection;
import tk.schmid.epaper.display.protocol.DisplayFontSize;
import tk.schmid.epaper.display.serialcom.SerialEPaperDisplay;

public class EPaperCommandLineMode {
	private static final Logger log = LoggerFactory.getLogger(EPaperCommandLineMode.class);

	private static Option help = Option.builder("h").longOpt("help").desc("displays help").build();
	private static Option device = Option.builder("dev").argName("dev").hasArg().longOpt("device").desc("serial display device").build();
	private static Option clearScreen = Option.builder("c").longOpt("clear").desc("clear display content").build();
	private static Option refreshScreen = Option.builder("r").longOpt("refresh").desc("refresh the display content").build();
	private static Option fill = Option.builder("f").longOpt("fill").desc("fill the following shape if possible").build();

	private static Option drawPoint = Option.builder("p").longOpt("drawPoint").numberOfArgs(2).argName("x y").desc("draws a point").build();
	private static Option drawLine = Option.builder("l").longOpt("drawLine").numberOfArgs(4).argName("x0 y0 x1 y1").desc("draws a line").build();

	private static Option drawRectangle = Option.builder("rect").longOpt("drawRectangle").numberOfArgs(4).argName("x0 y0 x1 y1").desc("draws a rectangle").build();
	private static Option drawCircle = Option.builder("circ").longOpt("drawCircle").numberOfArgs(3).argName("x y r").desc("draws a circle").build();
	private static Option drawTriangle = Option.builder("tri").longOpt("drawTriangle").numberOfArgs(6).argName("x0 y0 x1 y1 x2 y2").desc("draws a triangle").build();

	private static Option displayText = Option.builder("t").longOpt("displayText").numberOfArgs(3).argName("x y text").desc("displays given text. (Max. 1020 chars)").build();
	private static Option displayImage = Option.builder("i").longOpt("displayImage").numberOfArgs(3).argName("imageFileName").desc("displays an image").build();

	private static Option importFont = Option.builder("if").longOpt("importFont").numberOfArgs(1).argName("fontFileName").desc("imports a font to the internal memory from card").build();
	private static Option importImage = Option.builder("ii").longOpt("importImage").numberOfArgs(2).argName("imageFileName").desc("imports a image to the internal memory from card").build();

	private static Option setDisplayDirection = Option.builder("sd").longOpt("setDisplayDirection").numberOfArgs(1).argName("Normal|Rotated").desc("draws a line between given coordinates").build();
	private static Option setFontSize = Option.builder("sf").longOpt("setFonzSize").numberOfArgs(1).argName("DotMatrix_32|DotMatrix_48|DotMatrix_64").desc("draws a line between given coordinates").build();

	public static Options buildCliOptions() {
		Options options = new Options();
		options.addOption(help);
		options.addOption(device);
		options.addOption(clearScreen);
		options.addOption(refreshScreen);
		options.addOption(fill);
		options.addOption(drawLine);
		options.addOption(drawRectangle);
		options.addOption(drawCircle);
		options.addOption(drawTriangle);
		options.addOption(displayText);
		options.addOption(displayImage);
		options.addOption(importFont);
		options.addOption(importImage);
		options.addOption(setDisplayDirection);
		options.addOption(setFontSize);
		options.addOption(drawPoint);
		return options;
	}

	public static void performCommands(CommandLine parsedCommands) {
		String deviceName = parsedCommands.getOptionValue(device.getOpt());
		EPaperDisplay display = new SerialEPaperDisplay(deviceName);
		
		display.connect();

		for (int i = 0; i < parsedCommands.getOptions().length; i++) {

			Option currentCommand = parsedCommands.getOptions()[i];
			boolean isFilled = i > 0 && parsedCommands.getOptions()[i - 1].equals(fill);

			if (currentCommand.equals(clearScreen)) {
				display.clearScreen();
			}
			if (currentCommand.equals(refreshScreen)) {
				display.repaint();
			}
			if (currentCommand.equals(drawPoint)) {
				int[] args = castArgsToInt(currentCommand.getValues());
				display.drawPoint(args[0], args[1]);
			}
			if (currentCommand.equals(drawLine)) {
				int[] args = castArgsToInt(currentCommand.getValues());
				display.drawLine(args[0], args[1], args[2], args[3]);

			}
			if (currentCommand.equals(drawRectangle)) {
				int[] args = castArgsToInt(currentCommand.getValues());
				display.drawRectangle(args[0], args[1], args[2], args[3], isFilled);

			}
			if (currentCommand.equals(drawCircle)) {
				int[] args = castArgsToInt(currentCommand.getValues());
				display.drawCircle(args[0], args[1], args[2], isFilled);

			}
			if (currentCommand.equals(drawTriangle)) {
				int[] args = castArgsToInt(currentCommand.getValues());
				display.drawTriangle(args[0], args[1], args[2], args[3], args[4], args[5], isFilled);

			}
			if (currentCommand.equals(displayText)) {
				int x = Integer.parseInt(currentCommand.getValue(0));
				int y = Integer.parseInt(currentCommand.getValue(1));
				String text = currentCommand.getValue(2);
				display.displayText(x, y, text);

			}
			if (currentCommand.equals(displayImage)) {
				int x = Integer.parseInt(currentCommand.getValue(0));
				int y = Integer.parseInt(currentCommand.getValue(1));
				String filename = currentCommand.getValue(2);
				display.displayImage(x, y, filename);

			}
			if (currentCommand.equals(importFont)) {
				String fontName = currentCommand.getValue(0);
				display.importFont(fontName);
			}
			if (currentCommand.equals(importImage)) {
				String filename = currentCommand.getValue(0);
				display.importImage(filename);

			}
			if (currentCommand.equals(setDisplayDirection)) {
				DisplayDirection displayDirection = DisplayDirection.valueOf(currentCommand.getValue());
				display.setDisplayDirection(displayDirection);

			}
			if (currentCommand.equals(setFontSize)) {
				DisplayFontSize fontSizeToSet = DisplayFontSize.valueOf(currentCommand.getValue());
				display.setEnglishFontSize(fontSizeToSet);
			}
		}
	}

	private static int[] castArgsToInt(String[] args) {
		int[] asInteger = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			asInteger[i] = Integer.parseInt(args[i]);
		}
		return asInteger;
	}

	public static void main(String[] args) {

		Options cliOptions = buildCliOptions();

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine parsedCommands = parser.parse(cliOptions, args);

			if (args.length == 0 || parsedCommands.hasOption(help.getOpt())) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(800);
				formatter.printHelp("epaperapi.jar", cliOptions);
				return;
			}

			if (!parsedCommands.hasOption(device.getOpt())) {
				log.error("Device to use is mandatory");
				return;
			}

			performCommands(parsedCommands);

		} catch (ParseException e) {
			log.error("Parsing failed.", e);
		}
	}
}
