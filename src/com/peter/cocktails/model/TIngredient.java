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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_seq")
	@SequenceGenerator(name = "ingredients_seq", sequenceName = "co.ingredients_ingredient_id_seq", initialValue=0, allocationSize = 1)
	@Column (name = "ingredient_id", nullable = false)
	private Long ingredientId;
	
	@Column (name = "name", nullable = false)
	private String name;


	public TIngredient() {
		super();
	}


	public Long getIngredientId() {
		return ingredientId;
	}


	public void setCocktail_id(Long cocktail_id) {
		this.ingredientId = cocktail_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public void reset() {
		this.ingredientId = 1L;
	}
	
	
	
	

}

