

import java.awt.Desktop;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.referencing.GeodeticCalculator;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

public class Launcher {
	
	private int requestCounter = 0;
	private int errorCounter = 0;
	
	//14.146m N-S/ 9.752m E-W
	private final GeodeticCalculator calc = new GeodeticCalculator();
	private final static double N = 48.902575;
	private final static double S = 48.815613;
	private final static double E = 2.416901;
	private final static double W = 2.223954;
	
	private Point2D pointDepart;
	private Point2D pointArrivee;
	private Point2D pointRecherche;
	
	private final static int DISTANCE_CALCUL = 20; //metres
	private final static int AZIMUT_EST = 0;
	private final static int AZIMUT_SUD = -90;
	
	private final List<String> filteredTypes = new ArrayList<String>(Arrays.asList("route","locality"));
	private List<String> places = new ArrayList<String>();
	
	private MyLogger logger;
	private DAO dao;
	private FileWriter fileWriter;
	private final static boolean WRITE_EXCEL = false;
	private GooglePlaces client;
	private MyFrame frame;
	
	public Launcher(MyGooglePlaces gp) throws Exception{
		client = gp;
		init();
	}
	
	private void init() throws Exception {
        	frame = new MyFrame();
            frame.setVisible(true);
			dao = new DAO();
			dao.connect();
			this.logger = new MyLogger();
			logger.logInfo("Application started");
			fileWriter  = new FileWriter(WRITE_EXCEL);
			this.pointDepart = new Point2D.Double(N, W); 
			this.pointArrivee = new Point2D.Double(S, E);
			setPointRecherche(new Point2D.Double(48.8800932,2.3261067));
//			setPointRecherche(this.pointDepart);
	}

	public void start(){
		while(this.pointRecherche.getX()>= this.pointArrivee.getX()){
			while(this.pointRecherche.getY()<= this.pointArrivee.getY()){
				getPlaces(this.pointRecherche);
				setPointRecherche(prochainPoint(this.pointRecherche, AZIMUT_EST, DISTANCE_CALCUL));
			}
			setPointRecherche(prochainPoint(this.pointRecherche, AZIMUT_SUD, DISTANCE_CALCUL));
			setPointRecherche(new Point2D.Double(this.pointRecherche.getX(), this.pointDepart.getY()));
		}
		System.out.println("DONE");
		openFile();
		System.gc();
	}

	private void openFile() {
		File file = this.fileWriter.write();
		if(null != file){
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getPlaces(Point2D pointRecherche) {
		try{
			List<Place> list = this.client.getNearbyPlaces(pointRecherche.getX(), pointRecherche.getY(), DISTANCE_CALCUL, GooglePlaces.MAXIMUM_RESULTS);
			incrementRequestCounter();
			for (Place place : list) {
				if(null == this.filteredTypes.stream().filter(s->place.getTypes().contains(s)).findFirst().orElse(null) && !places.contains(place.getPlaceId())){
					this.places.add(place.getPlaceId());
					Place detail = this.client.getPlaceById(place.getPlaceId(), new Param[0]);
					incrementRequestCounter();
					frame.setPlacesLabelText(places.size());;
//					this.fileWriter.writePlace(place.getPlaceId(), detail);
					dao.insertComplete(detail);
				}
			}
		}catch(GooglePlacesException | SQLException e){
			gestionErreurGenerique(e);
		}
	}

	private void gestionErreurGenerique(Exception e) {
		errorCounter++;
		e.printStackTrace();
		this.logger.logError(e.getMessage());
		this.logger.logError("Dernier point recheché : " + this.pointRecherche);
		frame.setErrorLabelText(errorCounter);
	}

	private void incrementRequestCounter() {
		this.requestCounter++;
		frame.setRequestLabelText(requestCounter);
	}

	private Point2D prochainPoint(Point2D pointRecherche, int azimuth, int distance) {
		this.calc.setStartingGeographicPoint(pointRecherche);
		this.calc.setDirection(azimuth, distance);
		return this.calc.getDestinationGeographicPoint();
	}
	
	private void setPointRecherche(Point2D point){
		this.pointRecherche = point;
		frame.setCoordinatesLabelText(point);
	}
	
}
