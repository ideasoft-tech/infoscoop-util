/* infoScoop OpenSource
 * Copyright (C) 2010 Beacon IT Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 */

package org.infoscoop.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * repeatable & keypath : 'tagname' : { 'key' : {...}, 'key' : {...}, ...}
 * repeatable : 'tagname' : [ {...}, {...}, ... ]
 * keypath : 'key' : { ... }
 * arrayPath : [[{...}], [{...}]]
 * 
 * @author a-kimura
 * 
 */
public class Xml2Json {
	private static Log log = LogFactory.getLog(Xml2Json.class);

	private List<String> keyPaths = new ArrayList<String>();

	private Map<String, String> pathMaps = new HashMap<String, String>();

	private List<String> repeatables = new ArrayList<String>();
	private List<String> repeatableNames = new ArrayList<String>();

	private List<String> singles = new ArrayList<String>();

	private List<String> skips = new ArrayList<String>();

	private List<String> ignores = new ArrayList<String>();

	private List<String> arrays = new ArrayList<String>();

	private Map<String, String> namespaceResolvers = new HashMap<String, String>();

	private Map<String, String> nameRenamer = new HashMap<>();

	private String basePath;

	private Xml2JsonListener listner = new NoOpListner();

	class NoOpListner implements Xml2JsonListener {
		public String text(String text) {
			return text;
		}
	}

	public void addPathRule(String xpath, String keyAttrName,
			boolean isRepeatable, boolean isSingle) {
		if (keyAttrName != null) {
			keyPaths.add(xpath);
			pathMaps.put(xpath, keyAttrName);
		}
		if (isRepeatable) {
			repeatables.add(xpath);
		}
		if (isSingle) {
			singles.add(xpath);
		}
	}

	public void addSkipRule(String xpath) {
		skips.add(xpath);
	}

	public void addIgnoreRule(String xpath) {
		ignores.add(xpath);
	}

	public void addRepeatableNames(String name) {
		repeatableNames.add(name);
	}
	
	public void addRepeatable(String xpath) {
		repeatables.add(xpath);
	}

	public void addArrayPath(String xpath) {
		arrays.add(xpath);
	}

	public void addNamespaceResolver(String prefix, String uri) {
		namespaceResolvers.put(uri, prefix);
	}

    public void addNameRenamer(String baseName, String rename) {
      nameRenamer.put(baseName, rename);
    }

    public String getRenamedName(String baseName) {
      String result = nameRenamer.get(baseName);
      if (result == null) {
        return baseName;
      }
      return result;
    }

	public void setListner(Xml2JsonListener textFilter) {
		this.listner = textFilter;
	}

	public JSONObject xml2jsonObj(NodeList nodes)  {
		this.basePath = null;
		if (nodes == null || nodes.getLength() == 0)
			return null;
		Node baseNode = nodes.item(0).getParentNode();
		if (baseNode == null)
			return null;
		this.basePath = getXPath((Element) baseNode);
		Map map = new SequencedHashMap();
		nodelist2json(map, nodes);
		return new JSONObject(map);
	}

	public JSONObject xml2jsonObj(Element element)  {
		this.basePath = null;
		Node baseNode = element.getParentNode();
		if (baseNode != null && baseNode.getNodeType() == Node.ELEMENT_NODE)
			this.basePath = getXPath((Element) baseNode);
		JSONObject obj = (JSONObject) node2json(element);
		return obj;
	}

	public String xml2json(NodeList nodes)  {
		JSONObject obj = xml2jsonObj(nodes);
		if (obj == null)
			return "";
		return obj.toString(1);
	}

	public String xml2json(Element element){
		JSONObject obj = xml2jsonObj(element);
		return obj.toString(1);
	}

	public String xml2json(String xml) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		Element root = doc.getDocumentElement();
		return xml2json(root);
	}

	private Object node2json(Element element) throws DOMException {
		Map map = new SequencedHashMap();
		String xpath = getXPath(element);
		if (singles.contains(xpath)) {
			if (element.getFirstChild() != null)
				return listner.text(element.getFirstChild().getNodeValue());
			else
				return "";
		}
		NamedNodeMap attrs = element.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			map.put(getRenamedName(name), listner.text(value));
		}
		NodeList childs = element.getChildNodes();
		nodelist2json(map, childs);
		return new JSONObject(map);
	}

	private void nodelist2json(Map map, NodeList nodes)  {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			switch (node.getNodeType()) {
			case Node.TEXT_NODE:
			case Node.CDATA_SECTION_NODE:
				String text = node.getNodeValue().trim();
				if (text.length() > 0)
					map.put("content", listner.text(node.getNodeValue()));
				break;
			case Node.ELEMENT_NODE:
				Element childElm = (Element) node;
				String childXPath = getXPath(childElm);
				if (!ignores.contains(childXPath)) {
					if (skips.contains(childXPath)) {
						nodelist2json(map, childElm.getChildNodes());
					} else if (arrays.contains(childXPath)) {
						JSONArray obj = (JSONArray) map.get(getRenamedName(childElm.getNodeName()));
						if (obj == null) {
							obj = new JSONArray();
							map.put(getRenamedName(childElm.getNodeName()), obj);
						}
						JSONArray array = new JSONArray();
						NodeList childNodes = childElm.getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Node child = childNodes.item(j);
							//TODO need to support the other node types.
							if (child.getNodeType() != Node.ELEMENT_NODE)
								continue;
							array.put(node2json((Element) child));
						}
						obj.put(array);
					} else {
						String childName = childElm.getNodeName();
						boolean isRepeatable = (repeatables.contains(childXPath) || repeatableNames.contains(childName));
						boolean hasKey = keyPaths.contains(childXPath);
						if (isRepeatable && hasKey) {
							JSONObject obj = (JSONObject) map.get(getRenamedName(childName));
							if (obj == null) {
								obj = new JSONObject();
								map.put(getRenamedName(childName), obj);
							}
							String attrName = (String) pathMaps.get(childXPath);
							String attrValue = childElm.getAttribute(attrName);
							obj.put(attrValue, node2json(childElm));
						} else if (isRepeatable && !hasKey) {
							JSONArray obj = (JSONArray) map.get(getRenamedName(childName));
							if (obj == null) {
								obj = new JSONArray();
								map.put(getRenamedName(childName), obj);
							}
							obj.put(node2json(childElm));
						} else if (hasKey) {
							String attrName = (String) pathMaps.get(childXPath);
							String attrValue = childElm.getAttribute(attrName);
							map.put(attrValue, node2json(childElm));
						} else {
							map.put(getRenamedName(childName), node2json(childElm));
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private String getXPath(Element element) {
		if (element == null)
			return null;
		StringBuffer xpath = new StringBuffer();
		xpath.append("/");
		String uri = element.getNamespaceURI();
		String prefix = (String) namespaceResolvers.get(uri);
		if (prefix != null)
			xpath.append(prefix).append(":");
		xpath.append(getTagName(element));
		Element parent = element;
		try {
			while (true) {
				parent = (Element) parent.getParentNode();
				if (parent == null)
					break;
				xpath.insert(0, getTagName(parent));
				uri = parent.getNamespaceURI();
				prefix = (String) namespaceResolvers.get(uri);
				if (prefix != null)
					xpath.insert(0, prefix + ":");
				xpath.insert(0, "/");
			}
		} catch (ClassCastException e) {

		}
		String xpathStr = xpath.toString();
		if (this.basePath != null)
			xpathStr = xpathStr.replaceFirst("^" + this.basePath, "");
		return xpathStr;
	}

	private String getTagName(Element elem) {
		String name = elem.getLocalName();
		if (name == null)
			name = elem.getNodeName();
		return name;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StringBuffer xml = new StringBuffer();
			Reader reader = new InputStreamReader(new FileInputStream(
					"D:/ProgramFiles/eclipse/workspace/msd-portal_1_2_0_1/InstallTools/DBInitTool/xmldata/widgetconfig/RssReader.xml"), "utf-8");
			try {
				char[] ch = new char[1024];
				int len = 0;
				while ((len = reader.read(ch)) != -1) {
					xml.append(ch, 0, len);
				}
			} finally {
				reader.close();
			}

			Xml2Json x2j = new Xml2Json();
			
			String json = x2j.xml2json(xml.toString());
			System.out.println(json);
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
