
import java.awt.geom.Point2D;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel labelRequestCount = new JLabel();
	private final JLabel labelCurrentCoordinates = new JLabel();
	private final JLabel labelPlacesCount = new JLabel();
	private final JLabel labelErrorCount = new JLabel();

	private final static String NB_REQUEST = 	"Requests\t\t: ";
	private final static String COORD = 		"GPS coord.\t: ";
	private final static String PLACES = 		"Places\t\t: ";
	private final static String ERRORS = 		"Errors\t\t: ";

	public MyFrame() {
		initUI();
	}
	
	private void initUI() {
		setTitle("Simple example");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		
		setRequestLabelText(0);
		setCoordinatesLabelText(new Point2D.Double(0,0));
		setPlacesLabelText(0);
		setErrorLabelText(0);

		add(labelRequestCount);
		add(labelCurrentCoordinates);
		add(labelPlacesCount);
		add(labelErrorCount);
	}
	
	public void setRequestLabelText(int nb) {
		labelRequestCount.setText(NB_REQUEST + nb);
		revalidate();
	}

	public void setCoordinatesLabelText(Point2D point) {
		labelCurrentCoordinates.setText(COORD + point.getX() + ", " + point.getY());
		revalidate();
	}

	public void setPlacesLabelText(int nb) {
		labelPlacesCount.setText(PLACES + nb);
		revalidate();
	}

	public void setErrorLabelText(int nb) {
		labelErrorCount.setText(ERRORS + nb);
		revalidate();
	}

}