

import se.walkercrou.places.RequestHandler;

public class Main {
	
	private final static String API_KEY = "AIzaSyBESI3OQRa3Q8c1XIaQIrf7x6kjMex7EPo";
	private static RequestHandler resquestHandler = new MyRequestHandler();

	public static void main(String[] args) throws Exception {
		new Launcher(new MyGooglePlaces(API_KEY,resquestHandler)).start();
	}

}
