package com.peter.cocktails.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
	
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
		
		for(String url : cocktailsUrlList) {
			System.out.println(url);
		}
		System.out.println(cocktailsUrlList.size());
	
	}
}
