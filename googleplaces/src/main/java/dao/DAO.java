package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.Hours.Period;
import se.walkercrou.places.Place;
import se.walkercrou.places.Review;

public class DAO {
	
	private Connection connection;

	private PreparedStatement statement_insertOrUpdatePlace;
    private PreparedStatement statement_insertOrUpdateReview;
    private PreparedStatement statement_insertPeriod;
    private PreparedStatement statement_insertType;
    private PreparedStatement statement_selectPeriodIds;
    private PreparedStatement statement_selectTypeIds;
    private PreparedStatement statement_updatePeriod;
    private PreparedStatement statement_deletePeriod;
    private PreparedStatement statement_deleteType;

    private PreparedStatement statement_selectParams;
    private PreparedStatement statement_updateParam;
    private PreparedStatement statement_countPlaces;
    
    private final static String ON_CONFLICT_PLACE_SET = " ON CONFLICT (\"ID\") DO UPDATE SET ";
    private final static String ON_CONFLICT_REVIEW_SET = " ON CONFLICT (\"PLACE_ID\",\"AUTHOR\") DO UPDATE SET ";

	public void connect() throws ClassNotFoundException, SQLException {      
	      Class.forName("org.postgresql.Driver");
	      String url = "jdbc:postgresql://localhost:5432/GOOGLE_PLACES";
	      String user = "postgres";
	      String passwd = "root";
	      this.connection = DriverManager.getConnection(url, user, passwd);
	      System.out.println("Connexion effective !");         
	      init();
	  }
	
	private void init() throws SQLException{
		this.statement_insertOrUpdatePlace = connection.prepareStatement(" INSERT INTO \"PLACE\" (\"ID\",\"NAME\",\"ADDRESS\",\"ZIPCODE\",\"PHONE\",\"INTERN_PHONE\",\"WEBSITE\",\"ALWAYS_OPENED\",\"STATUS\",\"URL\",\"PRICE\",\"VICINITY\")"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
				+ ON_CONFLICT_PLACE_SET 
				+ " \"ID\"=?,\"NAME\"=?,\"ADDRESS\"=?,\"ZIPCODE\"=?,\"PHONE\"=?,\"INTERN_PHONE\"=?,\"WEBSITE\"=?,\"ALWAYS_OPENED\"=?,\"STATUS\"=?,\"URL\"=?,\"PRICE\"=?,\"VICINITY\"=? ");
		this.statement_insertOrUpdateReview = connection.prepareStatement(" INSERT INTO \"REVIEW\" (\"PLACE_ID\",\"RATING\",\"AUTHOR\") VALUES (?,?,?)"
				+ ON_CONFLICT_REVIEW_SET
				+ " \"PLACE_ID\"=?,\"RATING\"=?,\"AUTHOR\"=? ");
		this.statement_insertPeriod = connection.prepareStatement(" INSERT INTO \"PERIOD\" (\"PLACE_ID\",\"OPENING_DAY\",\"CLOSING_DAY\",\"OPENING_TIME\",\"CLOSING_TIME\")"
				+ " VALUES (?,?,?,?,?)");
		this.statement_insertType = connection.prepareStatement(" INSERT INTO \"TYPE\" (\"PLACE_ID\",\"LABEL\") VALUES (?,?)");
		this.statement_selectPeriodIds = connection.prepareStatement(" SELECT \"ID\" FROM \"PERIOD\" WHERE \"PLACE_ID\" = ? ");
		this.statement_selectTypeIds = connection.prepareStatement(" SELECT \"TYPE\" FROM \"TYPE\" WHERE \"PLACE_ID\" = ? ");
		this.statement_updatePeriod = connection.prepareStatement(" UPDATE \"PERIOD\" SET \"PLACE_ID\"=?,\"OPENING_DAY\"=?,\"CLOSING_DAY\"=?,\"OPENING_TIME\"=?,\"CLOSING_TIME\"=? WHERE \"ID\" = ?");
		this.statement_deletePeriod = connection.prepareStatement(" DELETE FROM \"PERIOD\" WHERE \"ID\" = ?");
		this.statement_deleteType = connection.prepareStatement(" DELETE FROM \"TYPE\" WHERE \"PLACE_ID\" = ? AND \"LABEL\" IN (?)");
		this.statement_selectParams = connection.prepareStatement("SELECT * FROM \"PARAM\"");
		this.statement_updateParam = connection.prepareStatement(" UPDATE \"PARAM\" SET \"VALEUR\"=? WHERE \"CLE\"=? ");
		this.statement_countPlaces = connection.prepareStatement("SELECT COUNT(*) FROM \"PLACE\"");
	}
	
	public void insertPlace(Place place){
		try {
			int i = 1;
			statement_insertOrUpdatePlace.setString(i++, place.getPlaceId());
			statement_insertOrUpdatePlace.setString(i++, place.getName());
			statement_insertOrUpdatePlace.setString(i++, place.getAddress());
			statement_insertOrUpdatePlace.setString(i++, extractParisZpiCode(place.getAddress()));
			statement_insertOrUpdatePlace.setString(i++, place.getPhoneNumber());
			statement_insertOrUpdatePlace.setString(i++, place.getInternationalPhoneNumber());
			statement_insertOrUpdatePlace.setString(i++, place.getWebsite());
			statement_insertOrUpdatePlace.setBoolean(i++, null == place.getHours()? false : place.isAlwaysOpened());
			statement_insertOrUpdatePlace.setString(i++, place.getStatus().toString());
			statement_insertOrUpdatePlace.setString(i++, place.getGoogleUrl());
			statement_insertOrUpdatePlace.setString(i++, place.getPrice().toString());
			statement_insertOrUpdatePlace.setString(i++, place.getVicinity());
			
			statement_insertOrUpdatePlace.setString(i++, place.getPlaceId());
			statement_insertOrUpdatePlace.setString(i++, place.getName());
			statement_insertOrUpdatePlace.setString(i++, place.getAddress());
			statement_insertOrUpdatePlace.setString(i++, extractParisZpiCode(place.getAddress()));
			statement_insertOrUpdatePlace.setString(i++, place.getPhoneNumber());
			statement_insertOrUpdatePlace.setString(i++, place.getInternationalPhoneNumber());
			statement_insertOrUpdatePlace.setString(i++, place.getWebsite());
			statement_insertOrUpdatePlace.setBoolean(i++, null == place.getHours()? false : place.isAlwaysOpened());
			statement_insertOrUpdatePlace.setString(i++, place.getStatus().toString());
			statement_insertOrUpdatePlace.setString(i++, place.getGoogleUrl());
			statement_insertOrUpdatePlace.setString(i++, place.getPrice().toString());
			statement_insertOrUpdatePlace.setString(i++, place.getVicinity());
			statement_insertOrUpdatePlace.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertReview(String placeID, Review review){
		try {
			statement_insertOrUpdateReview.setString(1, placeID);
			statement_insertOrUpdateReview.setInt(2, review.getRating());
			statement_insertOrUpdateReview.setString(3, review.getAuthor());
			
			statement_insertOrUpdateReview.setString(4, placeID);
			statement_insertOrUpdateReview.setInt(5, review.getRating());
			statement_insertOrUpdateReview.setString(6, review.getAuthor());
			statement_insertOrUpdateReview.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPeriod(String placeID, List<Period> periods) throws SQLException{
		statement_selectPeriodIds.setString(1, placeID);
		ResultSet rs = statement_selectPeriodIds.executeQuery();
		List<Integer> ids = new ArrayList<>();
		while(rs.next()){
			ids.add(rs.getInt(1));
		}
		
		for (int i=0;i<ids.size();i++) {
			if(i < periods.size()){ // Màj de l'existant
				statement_updatePeriod.setString(1, placeID);
				statement_updatePeriod.setString(2, periods.get(i).getOpeningDay().toString());
				statement_updatePeriod.setString(3, periods.get(i).getClosingDay().toString());
				statement_updatePeriod.setString(4, periods.get(i).getOpeningTime().toString());
				statement_updatePeriod.setString(5, periods.get(i).getClosingTime().toString());
				statement_updatePeriod.setInt(6, ids.get(i));
				statement_updatePeriod.executeUpdate();
			}else{ // Suppression des enregistrements en trop
				statement_deletePeriod.setInt(1, ids.get(i));
				statement_deletePeriod.executeUpdate();
			}
		}
		if(periods.size() > ids.size()){ // Ajout des periodes supplémentaires
			for(int i=ids.size();i<periods.size();i++){
				statement_insertPeriod.setString(1, placeID);
				statement_insertPeriod.setString(2, periods.get(i).getOpeningDay().toString());
				statement_insertPeriod.setString(3, periods.get(i).getClosingDay().toString());
				statement_insertPeriod.setString(4, periods.get(i).getOpeningTime().toString());
				statement_insertPeriod.setString(5, periods.get(i).getClosingTime().toString());
				statement_insertPeriod.executeUpdate();
			}
		}
	}

	public void insertType(String placeID, List<String> types) throws SQLException{
		statement_selectTypeIds.setString(1, placeID);
		ResultSet rs = statement_selectTypeIds.executeQuery();
		List<String> typesBDD = new ArrayList<>();
		while(rs.next()){
			typesBDD.add(rs.getString(1));
		}
		
		// Suppression doublons
		for (String string : types) {
			if(typesBDD.contains(string)){
				types.remove(string);
				typesBDD.remove(string);
			}
		}
		
		// Suppression des types qui n'existent plus
		if(!typesBDD.isEmpty()){
			statement_deleteType.setString(1, placeID);
			statement_deleteType.setString(2, "'" + typesBDD.stream().reduce((a,b)-> a + "','" + b).get() + "'");
			statement_deleteType.executeUpdate();
		}
		
		// Ajout des nouveaux types
		for (String string : types) {
			statement_insertType.setString(1, placeID);
			statement_insertType.setString(2, string);
			statement_insertType.executeUpdate();
		}
		
	}
	
	private static String extractParisZpiCode(String address){
		String res = "NONE";
		if(null != address){
			int index = address.lastIndexOf("750");
			if(index != -1){
				res = address.substring(index,index+5);
			}
		}
		return res;
	}

	public void insertComplete(Place place) throws SQLException {
		if (null != place){
			insertPlace(place);
			place.getReviews().stream().forEach(review -> insertReview(place.getPlaceId(), review));
			if(null != place.getHours()){
				insertPeriod(place.getPlaceId(), place.getHours().getPeriods());
			}
			insertType(place.getPlaceId(), place.getTypes());
		}
	}
	
	public String[] getParam() throws SQLException{
		String[] params = new String[6];
		ResultSet rs = statement_selectParams.executeQuery();
		while(rs.next()){
			if(rs.getString(1).equals("TOTAL_REQUESTS")) params[0] = rs.getString(2);
			if(rs.getString(1).equals("LAST_REQUESTS_NUMBER")) params[1] = rs.getString(2);
			if(rs.getString(1).equals("LAST_REQUESTS_DAY")) params[2] = rs.getString(2);
			if(rs.getString(1).equals("LAST_COORD_X")) params[3] = rs.getString(2);
			if(rs.getString(1).equals("LAST_COORD_Y")) params[4] = rs.getString(2);
			if(rs.getString(1).equals("API_KEY")) params[5] = rs.getString(2);
		}
		return params;
	}
	
	public void setParam(String cle, String valeur) throws SQLException{
		this.statement_updateParam.setString(1, valeur);
		this.statement_updateParam.setString(2, cle);
		this.statement_updateParam.execute();
	}

	public int getPlaceNumber() throws SQLException {
		ResultSet rs = this.statement_countPlaces.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
}