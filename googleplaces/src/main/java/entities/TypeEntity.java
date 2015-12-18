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
@Table(name = "\"TYPE\"")
public class TypeEntity {

	@Id
	@Column(name="id_Type")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="\"LABEL\"")
	private String label;
	
	@ManyToOne
	@JoinColumn(name="\"PLACE_ID\"")
	private PlaceEntity place;

	public TypeEntity(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PlaceEntity getPlace() {
		return place;
	}

	public void setPlace(PlaceEntity place) {
		this.place = place;
	}
}
