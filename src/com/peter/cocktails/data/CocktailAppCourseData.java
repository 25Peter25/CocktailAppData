package com.peter.cocktails.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CocktailAppCourseData {

	private static final String PERSISTENCE_UNIT_NAME = "LuigiCocktailsData";
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
		Elements liElements = divElement.getElementsByTag("li");
		
		for(Element element : liElements) {
			String attrHref = element.getElementsByTag("a").isEmpty() ? "" : element.getElementsByTag("a").get(0).attr("href");
			
			if(attrHref.contains("(cocktail)")) {
				cocktailsUrlList.add("https://en.wikipedia.org" + attrHref);
			}
		}
		
		for(Iterator<String> iterator = cocktailsUrlList.iterator();iterator.hasNext();) {
			String cocktailUrl = iterator.next();
			
			//V ramci forcyklu sa budeme postupne pripajat na vsetky adresy ktore mame v zozname a z tagu caption ziskam nazov drinku
			Document document2 = Jsoup.connect(cocktailUrl)
					.header("Accept-Encoding", "gzip, deflate")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
			String name = document2.getElementsByTag("caption").get(0).text();
			
			// v casti infobox su udaje o ingredienciach a mnozstve
			// z tagu li ziskam nazov ingrediencie z casti a
			Element tableElement = document2.getElementsByClass("infobox").isEmpty() ? null : document2.getElementsByClass("infobox").get(0);
			
			Elements liElements2 = tableElement.getElementsByTag("li");
			
			for(Element liElement : liElements2) {
			//Ingredient Name
				String ingredient = liElement.getElementsByTag("a").isEmpty() ? "" : liElement.getElementsByTag("a").get(0).text().toLowerCase();
				
				System.out.println(ingredient);
			}
			
			
			
			
			System.out.println(name);
		}
		
		em.getTransaction().commit();
		em.close();
		
		
		

	}

}
