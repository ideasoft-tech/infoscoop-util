package org.infoscoop.util.test;

import java.io.FileWriter;
import java.io.IOException;
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

	public void testJcr() throws Exception {
		String outputName = buildOutputFilename("sample04.xml");

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream("sample04.xml"));
		int car = -1;
		StringBuffer buf = new StringBuffer();
		while ((car = fr.read()) != -1) {
			buf.append((char) car);
		}
		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("children");

		String result = xml2json.xml2json(buf.toString());
		saveJsonFile(outputName, result);

		System.out.println(result);
	}

	public void testPackageHierarchy() throws Exception {
		String[] files = {"sample03.xml"};
		for (int i = 0; i < files.length; i++) {
			packageHierarchyConvert(files[i]);
		}
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

	public void testIndicatorEvolution() throws Exception {
		String[] files = {"indicatorData.xml"};
		for (int i = 0; i < files.length; i++) {
			indicatorEvolutionConvert(files[i]);
		}
	}

	public void testAlerts() throws Exception {
		String[] files = {"alerts.xml"};
		for (int i = 0; i < files.length; i++) {
			alertsConvert(files[i]);
		}
	}

	public void testSearch() throws Exception {
		String[] files = {"search.xml"};
		for (int i = 0; i < files.length; i++) {
			searchConvert(files[i]);
		}
	}

	private void packageHierarchyConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

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
		saveJsonFile(outputName, result);

		//System.out.println(result);
	}

	private void bpaForumConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

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

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void scorecardHierarchyConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

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

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void scorecardListConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();
		xml2json.addRepeatableNames("scorecard");
		xml2json.addPathRule("/scorecards/scorecard/description", null, false,true);
		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void indicatorEvolutionConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();
		xml2json.addIgnoreRule("/xml/indicator");
		xml2json.addIgnoreRule("/xml/indicatorValue");
		xml2json.addIgnoreRule("/xml/artifactinstances");
		xml2json.addIgnoreRule("/xml/metric");
		xml2json.addIgnoreRule("/xml/root");
		xml2json.addIgnoreRule("/xml/chartData");
		xml2json.addIgnoreRule("/xml/forum");
		xml2json.addIgnoreRule("/xml/result");
		xml2json.addIgnoreRule("/xml/businessActions");

		xml2json.addRepeatableNames("series");

		xml2json.addPathRule("/xml/metricData/series/date", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/border.yellow", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/period", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/goal", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/green", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/tendency", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/yellow", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/label", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/blue", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/border.green", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/state", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/border.blue", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/value", null, false,true);
		xml2json.addPathRule("/xml/metricData/series/tolerance", null, false,true);
		xml2json.addPathRule("/xml/metricData/description/unit", null, false,true);
		xml2json.addPathRule("/xml/metricData/description/scale", null, false,true);

		xml2json.addNameRenamer("MetricData", "metricDataPeriod");

		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void alertsConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();

		xml2json.addRepeatableNames("atom:entry");
		xml2json.addRepeatableNames("atom:category");

		xml2json.addPathRule("/atom:feed/atom:id", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:title", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:title", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:updated", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:content", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:id", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:summary", null, false,true);
		xml2json.addPathRule("/atom:feed/atom:entry/atom:published", null, false,true);

		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void searchConvert(String filename) throws Exception {
		String outputName = buildOutputFilename(filename);

		InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
		String xml = IOUtils.toString(fr);

		Xml2Json xml2json = new Xml2Json();

		xml2json.addRepeatableNames("data");

		xml2json.addPathRule("/search/data/label", null, false,true);
		xml2json.addPathRule("/search/data/owner/label", null, false,true);
		xml2json.addPathRule("/search/data/owner/id", null, false,true);
		xml2json.addPathRule("/search/data/owner/type", null, false,true);

		String jsonOutput = xml2json.xml2json(xml);
		System.out.println("JSon: " + jsonOutput);

		saveJsonFile(outputName, jsonOutput);

		System.out.println(jsonOutput);
	}

	private void saveJsonFile(String outputName, String jsonOutput) throws IOException {
		FileWriter f = new FileWriter(outputName);
		f.write(jsonOutput);
		f.close();
	}

	private String buildOutputFilename(String filename) {
		URL url = getClass().getResource(filename);
		System.out.println("url = " + url.toExternalForm());
		System.out.println("url = " + url.toString());
		String outputName = url.toString().replaceAll(".xml", ".json").substring(5);
		return outputName;
	}
}
