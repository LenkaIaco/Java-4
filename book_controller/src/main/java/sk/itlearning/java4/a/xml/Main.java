package sk.itlearning.java4.a.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sk.itlearning.java4.book.Book;
import sk.itlearning.java4.book.Catalog;

public class Main {
	public static void main (String[] args) {
		InputStream is = Main.class.getResourceAsStream("book.xml");//tuto nacitam cely obsah xml suboru do javy a potom uz dole s nim pracujem ako chcem
		Catalog catalog = CatalogLoader.getFullCatagalog(is);
		catalog.getBook().forEach(k -> System.out.println(k.getTitle()));
		
//		catalog.getBook().stream().map(k -> {
//			k.setPrice(1.0f);
//			return k;
//		});
		
		for (Book b : catalog.getBook()) {//catalog.getBook je zoznam knih. prejdi cez kazdu knihu a zmen jej cenu
			b.setPrice(1.0f);
		}
				
		catalog.getBook().forEach(b -> System.out.println(b.getPrice()));
		
		//teraz opak. mame data v jave a chceme ho prekonvert. na xml:
		try(FileOutputStream fos = new FileOutputStream(new File("C:/devel/catalog.xml"))) {//cesta kde sa subor vytvori
			CatalogLoader.saveCatagalog(catalog, fos);//urob xml podla vzoru katalog na adrese fos
		} catch (IOException e) {e.printStackTrace();}
	}
}
