

import java.util.List;
import java.util.Locale;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;
import se.walkercrou.places.RequestHandler;
import se.walkercrou.places.exception.GooglePlacesException;

public class MyGooglePlaces extends GooglePlaces {

	public MyGooglePlaces(String apiKey, RequestHandler requestHandler) {
		super(apiKey, requestHandler);
	}
	
    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) throws GooglePlacesException {
        return super.getNearbyPlaces(lat, lng, radius, limit, extraParams);
    }

    @Override
    public Place getPlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&placeid=%s", super.getApiKey(), placeId), extraParams);
            return Place.parseDetails(this, super.getRequestHandler().get(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static String buildUrl(String method, String params, Param... extraParams) {
        String url = String.format(Locale.ENGLISH, "%s%s/json?%s", API_URL, method, params);
        url = url.replace(' ', '+');
        return url;
    }
}
