package entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="\"PLACES\"")
public class PlaceEntity {
	
	@Id
	@Column(name="\"ID\"")
	private String id;

	@Column(name="\"NAME\"")
	private String name;

	@Column(name="\"ADDRESS\"")
	private String address;
	
	@Column(name="\"ZIPCODE\"")
	private String zipCode;
	
	@Column(name="\"PHONE\"")
	private String phone;
	
	@Column(name="\"INTERN_PHONE\"")
	private String internPhone;
	
	@Column(name="\"WEBSITE\"")
	private String website;
	
	@Column(name="\"ALWAYS_OPENED\"")
	private boolean alwaysOpened;
	
	@Column(name="\"URL\"")
	private String url;
	
	@Column(name="\"PRICE\"")
	private String price;
	
	@Column(name="\"VICINITY\"")
	private String vicinity;
	
	@OneToMany(mappedBy="place")
	private List<TypeEntity> types;
	
	@OneToMany(mappedBy="place")
	private List<PeriodEntity> periods;
	
	@OneToMany(mappedBy="place")
	private List<ReviewEntity> reviews;
	
	public PlaceEntity(){}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInternPhone() {
		return internPhone;
	}
	public void setInternPhone(String internPhone) {
		this.internPhone = internPhone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public boolean isAlwaysOpened() {
		return alwaysOpened;
	}
	public void setAlwaysOpened(boolean alwaysOpened) {
		this.alwaysOpened = alwaysOpened;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public List<TypeEntity> getTypes() {
		return types;
	}

	public void setTypes(List<TypeEntity> types) {
		this.types = types;
	}

	public List<PeriodEntity> getPeriods() {
		return periods;
	}

	public void setPeriods(List<PeriodEntity> periods) {
		this.periods = periods;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
