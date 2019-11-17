package tk.schmid.epaper.display.swing;

import tk.schmid.epaper.display.EPaperDisplay;
import tk.schmid.epaper.display.protocol.DisplayColor;
import tk.schmid.epaper.display.protocol.DisplayFontSize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;

import static java.lang.Integer.parseInt;

public class SwingDemo {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JFrame jFrame = new JFrame("Edisplay demo");
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            SwingDisplay display = new SwingDisplay(800, 600);
            Container contentPane = jFrame.getContentPane();
            contentPane.add(display);
            jFrame.pack();
            jFrame.setVisible(true);
            display.drawRectangle(20, 20, 40, 580, true);
            display.drawRectangle(20, 560, 780, 580, true);
            display.drawRectangle(20, 20, 780, 40, true);
            display.drawRectangle(760, 20, 780, 580, true);

            display.setEnglishFontSize(DisplayFontSize.DotMatrix_64);
            display.displayText(300, 260, "Hello World");

            display.setEnglishFontSize(DisplayFontSize.DotMatrix_32);
            display.displayText(100, 360, "https://github.com/SchmidChristian/ePaperDriver");

            display.drawCircle(410, 160, 50, true);

            JFrame control = new JFrame("Control");
            control.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("Point", pointPanel(display));
            tabbedPane.add("Line", linePanel(display));
            tabbedPane.add("Circle", circlePanel(display));
            tabbedPane.add("Rectangle", rectanglePanel(display));
            tabbedPane.add("Triangle", trianglePanel(display));
            tabbedPane.add("Text", textPanel(display));
            tabbedPane.add("Other", otherPanel(display));
            control.getContentPane().add(tabbedPane);
            control.pack();
            control.setVisible(true);
        });
    }

    private static JPanel linePanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("000");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("000");
        panel.add(textFieldY0);
        panel.add(new JLabel("X1"));
        JTextField textFieldX1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX1.setText("100");
        panel.add(textFieldX1);
        JTextField textFieldY1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY1.setText("100");
        panel.add(new JLabel("Y1"));
        panel.add(textFieldY1);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.drawLine(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText()),
                        parseInt(textFieldX1.getText()),
                        parseInt(textFieldY1.getText())
                );
            }
        }));
        return panel;
    }

    private static JPanel pointPanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("000");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("000");
        panel.add(textFieldY0);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.drawPoint(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText())
                );
            }
        }));
        return panel;
    }

    private static JPanel circlePanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("000");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("000");
        panel.add(textFieldY0);
        panel.add(new JLabel("radius"));
        JTextField radius = new JFormattedTextField(NumberFormat.getInstance());
        radius.setText("100");
        panel.add(radius);
        JCheckBox fill = new JCheckBox("Fill");
        panel.add(fill);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                display.drawCircle(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText()),
                        parseInt(radius.getText()),
                        fill.isSelected()
                );
            }
        }));
        return panel;
    }

    private static JPanel rectanglePanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("000");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("000");
        panel.add(textFieldY0);
        panel.add(new JLabel("X1"));
        JTextField textFieldX1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX1.setText("100");
        panel.add(textFieldX1);
        JTextField textFieldY1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY1.setText("100");
        panel.add(new JLabel("Y1"));
        panel.add(textFieldY1);
        JCheckBox fill = new JCheckBox("Fill");
        panel.add(fill);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                display.drawRectangle(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText()),
                        parseInt(textFieldX1.getText()),
                        parseInt(textFieldY1.getText()),
                        fill.isSelected()
                );
            }
        }));
        return panel;
    }

    private static JPanel textPanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("100");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("300");
        panel.add(textFieldY0);
        panel.add(new JLabel("Text"));
        JTextField textField = new JTextField("Enter your text here.");
        panel.add(textField);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                display.displayText(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText()),
                        textField.getText()
                );
            }
        }));
        return panel;
    }

    private static JPanel trianglePanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("X"));
        JTextField textFieldX0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX0.setText("000");
        panel.add(textFieldX0);
        panel.add(new JLabel("Y"));
        JTextField textFieldY0 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY0.setText("000");
        panel.add(textFieldY0);

        panel.add(new JLabel("X1"));
        JTextField textFieldX1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX1.setText("100");
        panel.add(textFieldX1);
        JTextField textFieldY1 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY1.setText("100");
        panel.add(new JLabel("Y1"));
        panel.add(textFieldY1);

        panel.add(new JLabel("X2"));
        JTextField textFieldX2 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldX2.setText("100");
        panel.add(textFieldX2);
        JTextField textFieldY2 = new JFormattedTextField(NumberFormat.getInstance());
        textFieldY2.setText("200");
        panel.add(new JLabel("Y2"));
        panel.add(textFieldY2);

        JCheckBox fill = new JCheckBox("Fill");
        panel.add(fill);

        panel.add(new JButton(new AbstractAction("Draw line") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                display.drawTriangle(
                        parseInt(textFieldX0.getText()),
                        parseInt(textFieldY0.getText()),
                        parseInt(textFieldX1.getText()),
                        parseInt(textFieldY1.getText()),
                        parseInt(textFieldX2.getText()),
                        parseInt(textFieldY2.getText()),
                        fill.isSelected()
                );
            }
        }));
        return panel;
    }

    private static JPanel otherPanel(EPaperDisplay display) {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(new JButton(new AbstractAction("Clear screen") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.clearScreen();
            }
        }));

        JRadioButton fgBlack = new JRadioButton("Black");
        fgBlack.addActionListener((a) -> display.setColor(DisplayColor.Black, DisplayColor.Black));
        JRadioButton fgDark = new JRadioButton("Light Gray");
        fgDark.addActionListener((a) -> display.setColor(DisplayColor.LightGrey, DisplayColor.Black));

        JRadioButton fgLight = new JRadioButton("Dark gray");
        fgLight.addActionListener((a) -> display.setColor(DisplayColor.DarkGrey, DisplayColor.Black));

        JRadioButton fgWhite = new JRadioButton("White");
        fgWhite.addActionListener((a) -> display.setColor(DisplayColor.White, DisplayColor.Black));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(fgBlack);
        buttonGroup.add(fgDark);
        buttonGroup.add(fgLight);
        buttonGroup.add(fgWhite);

        panel.add(fgBlack);
        panel.add(fgDark);
        panel.add(fgLight);
        panel.add(fgWhite);

        return panel;
    }
}
