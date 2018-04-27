package ru.ulpfr.pension_brms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlFileReader {
	/**
	 * Класс загружает и парсит xml-файл
	 */

	public List<Object> readFromFile(File file) throws FileNotFoundException, XMLStreamException {
		InputStream is = new FileInputStream(file);
		return readFromXML(is);
	}
	
	public List<Object> readFromXML(InputStream is) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(is);
            return readDocument(reader);
        } catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
 
    private List<Object> readDocument(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            System.out.println(eventType);
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    System.out.println(elementName);
                    //cчитываем блок о клиентаx
                    if (elementName.equals("clients"))
                        return readClients(reader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
                case XMLStreamReader.END_DOCUMENT:
                	return null;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
    
    private List<Object> readClients(XMLStreamReader reader) throws XMLStreamException {
        List<Object> clients = new ArrayList<Object>();
         
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("client"))
                    	clients.add(readClient(reader));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return clients;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
 
    private Object readClient(XMLStreamReader reader) throws XMLStreamException {
        Object client = new Object();//Заменить на обект Клиента!
         
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                   /* String elementName = reader.getLocalName();
                    if (elementName.equals("title"))
                        client.setTitle(readStringValue(reader));
                    else if (elementName.equals("category"))
                        client.setCategory(readCategory(reader));
                    else if (elementName.equals("year"))
                        client.setYear(readIntValue(reader));*/
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return client;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
    
    private String readStringValue(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
     
     
    private int readIntValue(XMLStreamReader reader) throws XMLStreamException {
        String str = readStringValue(reader);
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + str);
        }
    }
}
