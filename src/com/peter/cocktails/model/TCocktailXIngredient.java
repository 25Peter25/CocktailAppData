package com.peter.cocktails.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "t_cocktails_x_ingredients", schema = "co")
public class TCocktailXIngredient implements Serializable{

	@EmbeddedId
	private CocktailIngredientId id;
	
	@ManyToOne
	@MapsId("cocktailId")
	@JoinColumn(name = "cocktail_id", nullable = false)
	private TCocktail cocktail;
	
	@ManyToOne
	@MapsId("ingredientId")
	@JoinColumn(name = "ingredient_id", nullable = false)
	private TIngredient ingredient;


	public TCocktailXIngredient() {
		super();
	}
	
	
	
	
	
	
}

