package com.peter.cocktails.model;


import java.io.Serializable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ingredients", schema = "co")
public class TIngredient implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ingredients_seq")
	@SequenceGenerator(name = "t_ingredients_seq", sequenceName = "co.t_ingredients_ingredients_id_seq", initialValue=0, allocationSize = 1)
	@Column (name = "ingredient_id", nullable = false)
	private Long cocktail_id;
	
	@Column (name = "name", nullable = false)
	private String name;


	public TIngredient() {
		super();
	}


	public Long getCocktail_id() {
		return cocktail_id;
	}


	public void setCocktail_id(Long cocktail_id) {
		this.cocktail_id = cocktail_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	

}

