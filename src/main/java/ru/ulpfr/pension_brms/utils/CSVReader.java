package ru.ulpfr.pension_brms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;

public class CSVReader {
	
	private  List<String> line;
	private ArrayList<List<String>> csv_table = new ArrayList<List<String>>();
	private static final char DEFAULT_SEPARATOR = ';';
    private static final char DEFAULT_QUOTE = '"';
	
	public CSVReader(String resource) {
		try {
			InputStream is = CSVReader.class.getResourceAsStream(resource);
			InputStreamReader isr = new InputStreamReader(is);
			String lineStr;
		    BufferedReader br = new BufferedReader(isr); 
	    	while ((lineStr = br.readLine()) != null) {
	    		line = parseLine(lineStr);
	    		csv_table.add(line);
	    		//System.out.println("Varaible [name= " + line.get(0) + " , type=" + line.get(1) + "]");

	        }
	    } catch (IOException e) {
	    	MainWindow.getInstance().output(resource+ " not loaded: "+e.getMessage(), MESSAGE_TYPE.ERROR);
	        e.printStackTrace();
	    }

	}
	
	//получить таблицу всеx параметров из csv-файла
	public ArrayList<List<String>> getLoadedData() {
		return csv_table;
	}
	
	//парсинг каждой строки по умолчанию
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }
    //парсинг строки с кастомным разделителем
    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }
    //парсинг строки с кастомными разделителем и кавычками
    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
               
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    continue;
                } else if (ch == '\n') {
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }
        result.add(curVal.toString());
        return result;
    }

}
