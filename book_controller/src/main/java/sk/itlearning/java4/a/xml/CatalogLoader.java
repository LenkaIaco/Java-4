package sk.itlearning.java4.a.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import sk.itlearning.java4.book.Catalog;

public class CatalogLoader {

	public static Catalog getFullCatagalog(InputStream inputStream) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Catalog catalog = (Catalog) jaxbUnmarshaller.unmarshal(inputStream);
			return catalog;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void saveCatagalog(Catalog c, OutputStream os) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(c, os);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
