package com.peter.cocktails.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable //anotacia ktora nevytvara Entitu, ale deklaruje ze do nej budu vlozene-embedded ine entity
public class CocktailIngredientId implements Serializable{

	@Column(name = "cocktailId")
	private Long cocktailId;
	
	@Column(name = "ingredientId")
	private Long ingredientId;


	public CocktailIngredientId(Long cocktailId, Long ingredientId) {
		super();
		this.cocktailId = cocktailId;
		this.ingredientId = ingredientId;
	}

	public CocktailIngredientId() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cocktailId == null) ? 0 : cocktailId.hashCode());
		result = prime * result + ((ingredientId == null) ? 0 : ingredientId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CocktailIngredientId other = (CocktailIngredientId) obj;
		if (cocktailId == null) {
			if (other.cocktailId != null)
				return false;
		} else if (!cocktailId.equals(other.cocktailId))
			return false;
		if (ingredientId == null) {
			if (other.ingredientId != null)
				return false;
		} else if (!ingredientId.equals(other.ingredientId))
			return false;
		return true;
	}
	
	

	


	
	
	//a
	
	
	
	
}