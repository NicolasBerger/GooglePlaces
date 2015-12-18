import java.util.ArrayList;
import java.util.List;

import entities.PeriodEntity;
import entities.PlaceEntity;
import entities.ReviewEntity;
import entities.TypeEntity;
import se.walkercrou.places.Hours.Period;
import se.walkercrou.places.Place;
import se.walkercrou.places.Review;

public class Utils {

	public static PlaceEntity convertPlaceToPlaceEntity(Place place){
		PlaceEntity p = new PlaceEntity();
		p.setId(place.getPlaceId());
		p.setName(place.getName());
		p.setAddress(place.getAddress());
		p.setZipCode(extractParisZpiCode(place.getAddress()));
		p.setPhone(place.getPhoneNumber());
		p.setInternPhone(place.getInternationalPhoneNumber());
		p.setWebsite(place.getWebsite());
		p.setAlwaysOpened(place.isAlwaysOpened());
		p.setUrl(place.getGoogleUrl());
		p.setPrice(place.getPrice().toString());
		p.setVicinity(place.getVicinity());
		p.setTypes(convertStringToTypeEntity(p, place.getTypes()));
		p.setPeriods(convertHoursToPeriodEntities(p, place));
		p.setReviews(convertReviewsToReviewEntity(p, place.getReviews()));
		return p;
	}
	
	private static List<ReviewEntity> convertReviewsToReviewEntity(PlaceEntity p, List<Review> reviews) {
		List<ReviewEntity> list = new ArrayList<>();
		if(null != reviews){
			for (Review r : reviews) {
				ReviewEntity review = new ReviewEntity();
				review.setAuthor(r.getAuthor());
				review.setRating(r.getRating());
				review.setPlace(p);
				list.add(review);
			}
		}
		return list;
	}

	private static List<PeriodEntity> convertHoursToPeriodEntities(PlaceEntity placeEntity, Place place) {
		List<PeriodEntity> list = new ArrayList<>();
		if(null != place.getHours() && null != place.getHours().getPeriods()){
			for (Period p : place.getHours().getPeriods()) {
				PeriodEntity pE = new PeriodEntity();
				pE.setOpeningDay(p.getOpeningDay().toString());
				pE.setOpeningTime(p.getOpeningTime());
				pE.setClosingDay(p.getClosingDay().toString());
				pE.setClosingTime(p.getClosingTime());
				pE.setPlace(placeEntity);
				list.add(pE);
			}
		}
		return list;
	}

	private static List<TypeEntity> convertStringToTypeEntity(PlaceEntity placeEntity, List<String> types) {
		List<TypeEntity> list = new ArrayList<>();
		if(null != types){
			for (String s : types) {
				TypeEntity t = new TypeEntity();
				t.setLabel(s);
				t.setPlace(placeEntity);
				list.add(t);
			}
		}
		return list;
	}

	private static String extractParisZpiCode(String address) {
		String res = "NONE";
		if (null != address) {
			int index = address.lastIndexOf("750");
			if (index != -1) {
				res = address.substring(index, index + 5);
			}
		}
		return res;
	}

}
