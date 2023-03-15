package sk.itlearning.java4.a.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import sk.itlearning.java4.book.Catalog;

public class TestCatalogLoader {

	@Test
	public void test() {
		InputStream is = TestCatalogLoader.class.getResourceAsStream("test.xml");

		Catalog catalog = CatalogLoader.getFullCatagalog(is);

		assertEquals(12, catalog.getBook().size());
	}

}
