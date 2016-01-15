package ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Launcher;

public class MyFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel labelRequestCount = new JLabel();
	private final JLabel labelCurrentCoordinates = new JLabel();
	private final JLabel labelPlacesCount = new JLabel();
	private final JLabel labelErrorCount = new JLabel();

	public MyFrame() {
		initUI();
	}
	
	private void initUI() {
		setTitle("Google Places API - Paris");
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JButton button = new JButton("Process");
		button.addActionListener(e ->  Launcher.PROCESS = !Launcher.PROCESS);
		JButton buttonBrowser = new JButton("View coord. on browser");
		buttonBrowser.addActionListener(e ->  openWebpage());
		
		setRequestLabelText(0);
		setCoordinatesLabelText(new Point2D.Double(0,0));
		setPlacesLabelText(0);
		setErrorLabelText(0);

		JPanel panelText = new JPanel();
		panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
		panelText.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelText.add(new JLabel("Requests"));
		panelText.add(Box.createVerticalStrut(5));
		panelText.add(new JLabel("GPS coord."));
		panelText.add(Box.createVerticalStrut(5));
		panelText.add(new JLabel("Places"));
		panelText.add(Box.createVerticalStrut(5));
		panelText.add(new JLabel("Errors"));
		panelText.setPreferredSize(new Dimension(100, 100));
		
		JPanel panelValue = new JPanel();
		panelValue.setLayout(new BoxLayout(panelValue, BoxLayout.Y_AXIS));
		panelValue.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelValue.add(labelRequestCount);
		panelValue.add(Box.createVerticalStrut(5));
		panelValue.add(labelCurrentCoordinates);
		panelValue.add(Box.createVerticalStrut(5));
		panelValue.add(labelPlacesCount);
		panelValue.add(Box.createVerticalStrut(5));
		panelValue.add(labelErrorCount);
		panelValue.setPreferredSize(new Dimension(200, 100));

		Box b1 = Box.createHorizontalBox();
		b1.add(panelText);
		b1.add(Box.createHorizontalStrut(10));
		b1.add(panelValue);
	    
		Box b2 = Box.createHorizontalBox();
	    b2.add(button);
	    b2.add(buttonBrowser);
	    
	    Box b3 = Box.createVerticalBox();
	    b3.add(b1);
	    b3.add(b2);
		getContentPane().add(b3);
	}
	
	public void openWebpage(){
	    try {
			Desktop.getDesktop().browse(new URL("https://www.google.fr/search?q=" + labelCurrentCoordinates.getText().substring(2).replace(" ", "")).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void setRequestLabelText(int nb) {
		labelRequestCount.setText(": " + nb);
		revalidate();
	}

	public void setCoordinatesLabelText(Point2D point) {
		labelCurrentCoordinates.setText(": " + point.getX() + ", " + point.getY());
		revalidate();
	}

	public void setPlacesLabelText(int nb) {
		labelPlacesCount.setText(": " + nb);
		revalidate();
	}

	public void setErrorLabelText(int nb) {
		labelErrorCount.setText(": " + nb);
		revalidate();
	}

}