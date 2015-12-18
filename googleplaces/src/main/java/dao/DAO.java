package dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.ParamEntity;
import entities.PeriodEntity;
import entities.PlaceEntity;
import entities.ReviewEntity;
import entities.TypeEntity;

public class DAO {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction transaction;

	public void insertPlace(PlaceEntity place) {
		setEntityManager();
		try{
			entityManager.persist(place);
			for (PeriodEntity period : place.getPeriods()) entityManager.persist(period);
			for (TypeEntity type : place.getTypes()) entityManager.persist(type);
			for (ReviewEntity reviewEntity : place.getReviews()) entityManager.persist(reviewEntity);
		}catch(EntityExistsException e){
			e.printStackTrace();
		}
		closeManagerAndFactory();
	}
	
	public void updatePlace(PlaceEntity place) {
		setEntityManager();
		entityManager.merge(place);
		for (PeriodEntity period : place.getPeriods())entityManager.merge(period);
		for (TypeEntity type : place.getTypes()) entityManager.merge(type);
		for (ReviewEntity reviewEntity : place.getReviews()) entityManager.merge(reviewEntity);
		closeManagerAndFactory();
	}
	
	public void updateParam(List<ParamEntity> params) {
		setEntityManager();
		for (ParamEntity paramEntity : params) entityManager.merge(paramEntity);
		closeManagerAndFactory();
	}
	
	public int getPlaceNumber() throws SQLException {
		setEntityManager();
		Long i = (Long) this.entityManager.createQuery("SELECT COUNT(P) FROM PlaceEntity P").getSingleResult();
		closeManagerAndFactory();
		return i.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> selectParam(){
		setEntityManager();
		Map<String,String> map = new HashMap<>();
		List<ParamEntity> params = ((Query) this.entityManager.createQuery("SELECT P FROM ParamEntity P")).getResultList();
		for (ParamEntity p : params) map.put(p.getCle(), p.getValeur());
		closeManagerAndFactory();
		return map;
	}

	private void setEntityManager() {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("UP_GooglePlaces");
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
	}

	private void closeManagerAndFactory() {
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
}