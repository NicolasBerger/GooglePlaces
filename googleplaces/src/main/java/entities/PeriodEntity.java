package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="\"PERIODS\"")
public class PeriodEntity {

	@Id
	@Column(name="id_Period")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="\"PLACE_ID\"")
	private PlaceEntity place;
	
	@Column(name="\"OPENING_DAY\"")
	private String openingDay;
	
	@Column(name="\"CLOSING_DAY\"")
	private String closingDay;
	
	@Column(name="\"OPENING_TIME\"")
	private String openingTime;
	
	@Column(name="\"CLOSING_TIME\"")
	private String closingTime;

	public PeriodEntity(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpeningDay() {
		return openingDay;
	}

	public void setOpeningDay(String openingDay) {
		this.openingDay = openingDay;
	}

	public String getClosingDay() {
		return closingDay;
	}

	public void setClosingDay(String closingDay) {
		this.closingDay = closingDay;
	}

	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	public String getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}

	public void setPlace(PlaceEntity place) {
		this.place = place;
	}

}
