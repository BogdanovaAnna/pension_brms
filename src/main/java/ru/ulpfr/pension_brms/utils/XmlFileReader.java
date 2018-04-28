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

import ru.ulpfr.pension_brms.model.XmlBlock;

public class XmlFileReader {
	/**
	 * Класс загружает и парсит xml-файл
	 */

	public List<XmlBlock> readFromFile(File file) throws FileNotFoundException, XMLStreamException {
		InputStream is = new FileInputStream(file);
		return readFromXML(is);
	}
	
	public List<XmlBlock> readFromXML(InputStream is) throws XMLStreamException {
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
 
    private List<XmlBlock> readDocument(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    
                    /****cчитываем блок о клиентаx*****/
                    if (elementName.equals("clients"))
                        return readClients(reader);
                    
                    if (elementName.equals("client")) {
                    	List<XmlBlock> list = new ArrayList<>();
                    	list.add(readClient(reader));
                    	return list;
                    }   
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
                case XMLStreamReader.END_DOCUMENT:
                	return null;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
    
    private List<XmlBlock> readClients(XMLStreamReader reader) throws XMLStreamException {
        List<XmlBlock> clients = new ArrayList<XmlBlock>();
         
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
 
    private XmlBlock readClient(XMLStreamReader reader) throws XMLStreamException {
    	XmlBlock client = new XmlBlock();  
    	String elementName = new String();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                   elementName = reader.getLocalName();
                   client.setValue(elementName, readStringValue(reader));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return client;
            }
        }
        throw new XMLStreamException("Premature end of file in client section ["+elementName+"]");
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
   
}
