package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="\"REVIEWS\"")
public class ReviewEntity {

	@Id
	@Column(name="\"ID\"")
	private int id;
	
	@Column(name="\"RATING\"")
	private int rating;
	
	@Column(name="\"AUTHOR\"")
	private String author;
	
	@ManyToOne
	@JoinColumn(name="\"PLACE_ID\"")
	private PlaceEntity place;

	public ReviewEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public PlaceEntity getPlace() {
		return place;
	}

	public void setPlace(PlaceEntity place) {
		this.place = place;
	}
}
