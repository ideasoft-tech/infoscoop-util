package org.infoscoop.util.test;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
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

	public void testPackageHierarchy() throws Exception {
		String[] files = {"sample03.xml"};
		for (int i = 0; i < files.length; i++) {
			packageHierarchyConvert(files[i]);
		}
	}

	public void packageHierarchyConvert(String filename) throws Exception {
 		URL url = getClass().getResource(filename);
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);
		
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
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

	public void testBPAForum() throws Exception {
		String[] files = {"forum001.xml"};
		for (int i = 0; i < files.length; i++) {
			bpaForumConvert(files[i]);
		}
	}

	public void testScorecardHierarchy() throws Exception {
		String[] files = {"scorecardHierarchy.xml"};
		for (int i = 0; i < files.length; i++) {
			scorecardHierarchyConvert(files[i]);
		}
	}

	public void testScorecardList() throws Exception {
		String[] files = {"scorecardList.xml"};
		for (int i = 0; i < files.length; i++) {
			scorecardListConvert(files[i]);
		}
	}

	public void bpaForumConvert(String filename) throws Exception {
		URL url = getClass().getResource(filename);
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);
		
		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("topic");
		//xml2json.addRepeatableNames("references");
		xml2json.addPathRule("/forum/topic/author", null, false,true);
		xml2json.addPathRule("/forum/topic/note", null, false,true);
		xml2json.addPathRule("/forum/topic/created", null, false,true);
		xml2json.addPathRule("/forum/topic/title", null, false,true);
		xml2json.addPathRule("/forum/topic/references", null, false,true);
		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		FileWriter f= new FileWriter(outputName);
		f.write(jsonOutput);
		f.close();
		
		
		
		System.out.println(jsonOutput);
		
		
	}

	public void scorecardHierarchyConvert(String filename) throws Exception {
		URL url = getClass().getResource(filename);
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("objectiveNode");
		xml2json.addRepeatableNames("indicatorNode");
		xml2json.addRepeatableNames("strategicMapNode");
		xml2json.addRepeatableNames("smartView");
		xml2json.addRepeatableNames("indicator");
		xml2json.addNameRenamer("objectiveNode", "children");
		xml2json.addNameRenamer("indicatorNode", "children");
		xml2json.addNameRenamer("strategicMapNode", "children");
		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		FileWriter f= new FileWriter(outputName);
		f.write(jsonOutput);
		f.close();



		System.out.println(jsonOutput);


	}

	public void scorecardListConvert(String filename) throws Exception {
		URL url = getClass().getResource(filename);
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("scorecard");
		xml2json.addPathRule("/scorecards/scorecard/description", null, false,true);
		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		FileWriter f= new FileWriter(outputName);
		f.write(jsonOutput);
		f.close();



		System.out.println(jsonOutput);


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
