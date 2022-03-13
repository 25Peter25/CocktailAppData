package com.peter.cocktails.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.peter.cocktails.model.CocktailIngredientId;
import com.peter.cocktails.model.TCocktail;
import com.peter.cocktails.model.TCocktailXIngredient;
import com.peter.cocktails.model.TIngredient;

public class CocktailAppCourseData {

	private static final String PERSISTENCE_UNIT_NAME = "CocktailData";
	private static EntityManagerFactory factory;
	
	
	public static void main(String[] args) throws IOException {

		
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		
		List<String> cocktailsUrlList = new LinkedList<String>();
		
		//kod sa pripoji na podstarnku wikipedia a stiahne vsetok zdrojovy kod. 
		//v maxBodySize urcim nech stihane tento kod nech je akokolvek velky
		//timetou 600 sekund nech ho stihne stiahnut
		//getom si stiahneme zdrojovy kod a vypiesme si ho
		Document document = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_IBA_official_cocktails")
				.header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
				.maxBodySize(0).timeout(600000).get();
		
		//Ziskame zoznam elemtnov cez getElementsByClass. Lebo zoznam cocktailov má túto triedu....
		Elements divElements = document.getElementsByClass("mw-parser-output");
		
		///Predpokladame, ze na stranke je len jeden takyto element
		Element divElement = divElements.get(0);
		
		//z divElement musim ziskat vsetky li elementy. Lebo kazdy napoj je sucastou li elementu...
		//z divu ziskame vsetky liElementy...
		Elements liElements = divElement.getElementsByTag("dt");
		
		for(Element element : liElements) {
			String attrHref = element.getElementsByTag("a").isEmpty() ? "" : element.getElementsByTag("a").get(0).attr("href");
			
			if(attrHref.equals("")) {
				continue;
			}
				cocktailsUrlList.add("https://en.wikipedia.org" + attrHref);
		}
		
		for(Iterator<String> iterator = cocktailsUrlList.iterator();iterator.hasNext();) {
			String cocktailUrl = iterator.next();
			
			//V ramci forcyklu sa budeme postupne pripajat na vsetky adresy ktore mame v zozname a z tagu caption ziskam nazov drinku
			Document document2 = Jsoup.connect(cocktailUrl)
					.header("Accept-Encoding", "gzip, deflate")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
			String name = document2.getElementsByTag("caption").get(0).text(); //COCKTAIL NAME
			
			//VOLANIE INSERT METODY DO NA COCKTAIL
			TCocktail cocktailForCrossTable = insertCocktail(name, cocktailUrl, em);
			
			
			// v casti infobox su udaje o ingredienciach a mnozstve
			// z tagu li ziskam nazov ingrediencie z casti a
			Element tableElement = document2.getElementsByClass("infobox").isEmpty() ? null : document2.getElementsByClass("infobox").get(0);
			
			Elements liElements2 = tableElement.getElementsByTag("li");
			
			for(Element liElement : liElements2) {
			//Ingredient Name
				String ingredient = liElement.getElementsByTag("a").isEmpty() ? "" : liElement.getElementsByTag("a").get(0).text().toLowerCase();
				
				//VOLANIE METODY INSERTCOCKTAILXINGREDIENT AK INGREDIENCIA JE VYPLNENA
				if(!ingredient.equals("")) {
					TIngredient ingredientForCrossTable = insertIngredient(ingredient, em);
					insertCocktailXIngredient(cocktailForCrossTable, ingredientForCrossTable, em);
					ingredientForCrossTable = null;
				}
				
				
				System.out.println(ingredient);
			}
			
			cocktailForCrossTable = null;
			
		}
		
		em.getTransaction().commit();
		em.close();
		
		System.out.println("ALL DONE");
		
	}
	
	

	//Vkladanie cocktailov do databazy
	private static TCocktail insertCocktail(String cocktailName, String cocktailUrl, EntityManager em) {
		TCocktail cocktail = new TCocktail();
		cocktail.setName(cocktailName.trim());
		cocktail.setUrl(cocktailUrl.trim());
		//persist vlozi zaznam do databazy a flush aby sa zmena ihned prejavila
		em.persist(cocktail);
		em.flush();
		
		System.out.println("Inserted cocktail with name "+cocktail.getName());
		return cocktail;
	}
	
	//vkladanie ingrediencii do databazy ak este nie su vlozene
	private static TIngredient insertIngredient(String ingredientName, EntityManager em) {
		Query query = em.createQuery("SELECT t FROM TIngredient t");
		List<TIngredient> ingredientList = query.getResultList();
		
		for(Iterator<TIngredient> it = ingredientList.iterator(); it.hasNext(); ) {
			TIngredient ingredientFromTable = it.next();
			
			if(ingredientName.trim().toLowerCase().equalsIgnoreCase(ingredientFromTable.getName())) {
				return ingredientFromTable;
			}
		}
		TIngredient newIngredient = new TIngredient();
		newIngredient.setName(ingredientName.trim().toLowerCase());
		em.persist(newIngredient);
		em.flush();
		
		System.out.println("Inserted ingredient with name "+newIngredient.getName());
		
		return newIngredient;	
	}
	
	//vkladanie zaznamu do krizovej tabulky databazy ak sa tam este nenachadza
	private static void insertCocktailXIngredient(TCocktail cocktail, TIngredient ingredient, EntityManager em) {
		
		TypedQuery<TCocktailXIngredient> query = em.createQuery(
				"SELECT t FROM TCocktailXIngredient t " +
				"WHERE t.cocktail.cocktailId = :cocktailId " +
				"AND t.ingredient.ingredientId = :ingredientId", TCocktailXIngredient.class);
		
		query.setParameter("cocktailId" , cocktail.getCocktailId());
		query.setParameter("ingredientId", ingredient.getIngredientId());
		
		if(query.getResultList().size() == 0) {
			
			TCocktailXIngredient cocktailXIngredient = new TCocktailXIngredient();
			
			cocktailXIngredient.setCocktail(cocktail);
			cocktailXIngredient.setIngredient(ingredient);
			
			cocktailXIngredient.setId(new CocktailIngredientId(cocktail.getCocktailId(), ingredient.getIngredientId()));
			
			em.persist(cocktailXIngredient);
			em.flush();
			
			System.out.println("Inserted cocktail and ingredient with names " + cocktail.getName() + "/" + ingredient.getName());
			
			
		}
		
		
		
	}
	
	
	

}
