package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="\"PARAM\"")
public class ParamEntity {

	@Id
	@Column(name="\"CLE\"")
	@NotNull
	private String cle;
	
	@Column(name="\"VALEUR\"")
	private String valeur;

	public ParamEntity() {
	}
	
	public ParamEntity(String cle, String valeur) {
		this.cle = cle;
		this.valeur = valeur;
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

}
