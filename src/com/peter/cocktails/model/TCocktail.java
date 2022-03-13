package com.peter.cocktails.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_cocktails",schema = "co") //namapovanie na triedy na tabulku t_cocktails
public class TCocktail implements Serializable{

	//nazvy premenych podla stlpcov v tabulke
	


	//k stlpcu id je potrebne pridat anotaciu id a sequence-ako sa bude navysovat id... a generatedvalue-auto increment
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cocktails_seq")
	@SequenceGenerator(name = "t_cocktails_seq", sequenceName = "co.t_cocktails_cocktail_id_seq", allocationSize = 1, initialValue = 0)
	@Column(name = "cocktail_id", nullable = false) //namapovanie cez anotaciu na stlpce tabulky
	private Long cocktailId;
	
	@Column(name = "name", nullable = false) //namapovanie cez anotaciu na stlpce tabulky
	private String name;
	
	@Column(name = "url", nullable = false) //namapovanie cez anotaciu na stlpce tabulky
	private String url;

	
	//Bezparametricky konstruktor...
	public TCocktail() {
		super();
	}


	public Long getCocktailId() {
		return cocktailId;
	}


	public void setCocktailId(Long cocktailId) {
		this.cocktailId = cocktailId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public void reset() {
		this.cocktailId = 1L;
	}
	
	
	
	
	


	

	
	
	
	
	
	
}