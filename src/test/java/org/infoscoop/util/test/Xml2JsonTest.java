package org.infoscoop.util.test;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import org.infoscoop.util.Xml2Json;

import junit.framework.TestCase;

public class Xml2JsonTest extends TestCase {
	
	
	
	public void testGrid() throws Exception {
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream("sample01.xml"));
		int car = -1;
		StringBuffer buf = new StringBuffer();
		while ((car = fr.read()) != -1) {
			buf.append((char) car);
		}
		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatable("/grid/columns/col");
		xml2json.addRepeatable("/grid/rows/row");

		System.out.println(xml2json.xml2json(buf.toString()));
		
		
		
		
	}
	
	public void testConcepts() throws Exception {
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream("sample02.xml"));
		int car = -1;
		StringBuffer buf = new StringBuffer();
		while ((car = fr.read()) != -1) {
			buf.append((char) car);
		}
		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatable("/concepts/concept");

		String result = xml2json.xml2json(buf.toString());
		System.out.println(result);
	}

	public void testHierarchy() throws Exception {
		URL url = getClass().getResource("sample03.xml");
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);
		
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream("sample03.xml"));
		int car = -1;
		StringBuffer buf = new StringBuffer();
		while ((car = fr.read()) != -1) {
			buf.append((char) car);
		}
		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("package");
		xml2json.addRepeatableNames("artifact");

		String result = xml2json.xml2json(buf.toString());
		FileWriter f= new FileWriter(outputName);
		f.write(result);
		f.close();
		
		
		
		//System.out.println(result);
		
		
	}

	public void testJcr() throws Exception {
		URL url = getClass().getResource("sample04.xml");
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);
		
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream("sample04.xml"));
		int car = -1;
		StringBuffer buf = new StringBuffer();
		while ((car = fr.read()) != -1) {
			buf.append((char) car);
		}
		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("children");

		String result = xml2json.xml2json(buf.toString());
		FileWriter f= new FileWriter(outputName);
		f.write(result);
		f.close();
		
		
		
		System.out.println(result);
		
		
	}
	

}
