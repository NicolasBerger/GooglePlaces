package main;

import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geotools.referencing.GeodeticCalculator;

import correctionApi.MyGooglePlaces;
import correctionApi.MyRequestHandler;
import dao.DAO;
import entities.ParamEntity;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;
import ui.MyFrame;
import utils.Utils;
import writer.FileWriter;
import writer.MyLogger;

public class Launcher {

	// CONSTANTES
	private final static boolean WRITE_EXCEL = false;
	private static final int MAX_REQUEST_DAY = 150000;
	private final GeodeticCalculator calc = new GeodeticCalculator();
	private final static int DISTANCE_CALCUL = 20, AZIMUT_EST = 0, AZIMUT_SUD = -90;
	private final static double N = 48.902575, S = 48.815613, E = 2.416901, W = 2.223954;	//14.146m N-S/ 9.752m E-W
	private final List<String> filteredTypes = new ArrayList<String>(Arrays.asList("route","locality"));
	
	private int placeCounter = 0, requestCounter = 0, errorCounter = 0, totalRequests = 0, lastRequestNb = 0;
	private Point2D pointDepart, pointArrivee, pointRecherche;
	private List<String> places = new ArrayList<String>();
	public static boolean PROCESS = false;
	private LocalDate dateNow;
	
	private DAO dao;
	private MyFrame frame;
	private MyLogger logger;
	private GooglePlaces client;
	private FileWriter fileWriter;
	

	public Launcher() throws Exception{
		init();
	}
	
	private void init() throws Exception {
		this.pointDepart = new Point2D.Double(N, W); 
		this.pointArrivee = new Point2D.Double(S, E);
		this.dateNow = LocalDate.now();
		
		// DAO
		this.dao = new DAO();
		this.placeCounter = dao.getPlaceNumber();
		
		// UI
		frame = new MyFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				logger.logInfo(pointRecherche.toString());
				PROCESS = false;
				List<ParamEntity> params = new ArrayList<>();
				params.add(new ParamEntity("LAST_REQUESTS_DAY", LocalDate.now().toString()));
				params.add(new ParamEntity("LAST_REQUESTS_NUMBER", String.valueOf(requestCounter + lastRequestNb)));
				params.add(new ParamEntity("TOTAL_REQUESTS", String.valueOf(totalRequests + requestCounter + lastRequestNb)));
				params.add(new ParamEntity("LAST_COORD_X", String.valueOf(pointRecherche.getX())));
				params.add(new ParamEntity("LAST_COORD_Y", String.valueOf(pointRecherche.getY())));
				dao.updateParam(params);
				System.exit(0);
			}
		});
	    frame.setVisible(true);
	    
	    // Params
	    Map<String, String> params = dao.selectParam();
	    client = new MyGooglePlaces(params.get("API_KEY"), new MyRequestHandler());
	    this.pointRecherche = new Point2D.Double(Double.parseDouble(params.get("LAST_COORD_X")), Double.parseDouble(params.get("LAST_COORD_Y")));
	    if(LocalDate.parse(params.get("LAST_REQUESTS_DAY")).isBefore(LocalDate.now())){
	    	this.lastRequestNb = 0;
	    }else{
	    	this.lastRequestNb = Integer.parseInt(params.get("LAST_REQUESTS_NUMBER"));
	    }
	    this.totalRequests = Integer.parseInt(params.get("TOTAL_REQUESTS"));
		
		// UI label
		frame.setPlacesLabelText(this.placeCounter);
		frame.setRequestLabelText(this.lastRequestNb);
		frame.setCoordinatesLabelText(this.pointRecherche);
		
		// Logger
		this.logger = new MyLogger();
		logger.logInfo("Application started");
		fileWriter  = new FileWriter(WRITE_EXCEL);
	}

	public void start(){
		while(this.pointRecherche.getX()>= this.pointArrivee.getX()){
			while(this.pointRecherche.getY()<= this.pointArrivee.getY()){
				System.out.print("");
				if(PROCESS){
					getPlaces(this.pointRecherche);
					setPointRecherche(prochainPoint(this.pointRecherche, AZIMUT_EST, DISTANCE_CALCUL));
				}
			}
			setPointRecherche(prochainPoint(this.pointRecherche, AZIMUT_SUD, DISTANCE_CALCUL));
			setPointRecherche(new Point2D.Double(this.pointRecherche.getX(), this.pointDepart.getY()));
		}
		System.out.println("DONE");
		openFile();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
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
					System.out.println(detail.getPlaceId() + "--------------------------");
					detail.getTypes().stream().forEach(System.out::println);
					incrementRequestCounter();
					this.placeCounter = this.dao.getPlaceNumber(); //TODO
					frame.setPlacesLabelText(this.placeCounter);
					this.fileWriter.writePlace(place.getPlaceId(), detail);
					dao.insertPlace(Utils.convertPlaceToPlaceEntity(detail));
				}
			}
		}catch(GooglePlacesException e){
			gestionErreurGenerique(e);
			if(e.getMessage().contains("OVER_QUERY_LIMIT")){
				PROCESS = false;
				List<ParamEntity> params = new ArrayList<>();
				params.add(new ParamEntity("LAST_REQUESTS_DAY", LocalDate.now().toString()));
				params.add(new ParamEntity("LAST_REQUESTS_NUMBER", String.valueOf(MAX_REQUEST_DAY)));
				params.add(new ParamEntity("TOTAL_REQUESTS", String.valueOf(totalRequests + MAX_REQUEST_DAY)));
				params.add(new ParamEntity("LAST_COORD_X", String.valueOf(this.pointRecherche.getX())));
				params.add(new ParamEntity("LAST_COORD_Y", String.valueOf(this.pointRecherche.getY())));
				dao.updateParam(params);
				System.exit(0);
			}
		}catch(SQLException e){
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
		frame.setRequestLabelText(this.lastRequestNb + requestCounter);
		this.checkSameDay();
	}
	
	private void checkSameDay(){
		if(dateNow.isBefore(LocalDate.now())){
			dateNow = LocalDate.now();
			List<ParamEntity> params = new ArrayList<>();
			params.add(new ParamEntity("LAST_REQUESTS_DAY", LocalDate.now().toString()));
			params.add(new ParamEntity("LAST_REQUESTS_NUMBER", String.valueOf(requestCounter + lastRequestNb)));
			params.add(new ParamEntity("TOTAL_REQUESTS", String.valueOf(totalRequests + requestCounter + lastRequestNb)));
			dao.updateParam(params);
			totalRequests += lastRequestNb + requestCounter; 
			lastRequestNb = requestCounter = 0;
			System.out.println("Scheduler");
		}
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
