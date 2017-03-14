package com.kissjava.test.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSONObject;


public class XmlTest {
	private static String getSamplePath(String fileName){
		String path = XmlTest.class.getClass().getResource("/") +"samples/" + fileName;
		path = path.substring(6);
		return path;//.replaceAll("test-classes", "classes");
	}
	public static String getFile(String name){
		String sampleFilePath = getSamplePath(name);
		String str = null;
		try {
			str = FileUtils.readFileToString(new File(sampleFilePath));
		} catch (IOException e) {
		}
		return str;
	}
	
	public static void dom4j(){
		 String name = "test01.xml";
	     try {
			Document doc = DocumentHelper.parseText(getFile(name));
			System.out.println(xmlToString(doc));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	 public static String xmlToString(Document document) {
        try {
            OutputFormat xmlFormat = new OutputFormat();
            xmlFormat.setEncoding("UTF-8");
            xmlFormat.setExpandEmptyElements(true);
            StringWriter out = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(out, xmlFormat);
            xmlWriter.write(document);
            xmlWriter.close();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	private boolean isList(Element element){
		String list = element.attributeValue("list");
		return list!=null && list.equals("list");
			
	}
	public Map<String, Object> xmlToMap(Element rootElement){
		Map<String, Object> map = new HashMap<String, Object>();
		if(rootElement.elements().size()==0){
			String key = rootElement.getName();
			if(isList(rootElement)){
				map.put(key, new ArrayList<>());
			}else{
				map.put(rootElement.getName(), rootElement.getText());
			}
			return map;
		}
		for (Iterator<Element> i = rootElement.elementIterator();i.hasNext();){
			Element element = (Element) i.next();
			String key = element.getName();
			if(element.elements().size()>0){
				if(isList(element)){
					map.put(key, xmlToList(element));
				}else{
					map.put(key, xmlToMap(element));
				}
			}else{
				if(isList(element)){
					map.put(key, new ArrayList<>());
				}else{
					map.put(key, element.getText());
				}
			}
		}
		
		return map;
	}

	public List<Map<String, Object>> xmlToList(Element rootElement){
		List<Map<String, Object>> list = new ArrayList<>();
		for (Iterator<Element> i = rootElement.elementIterator();i.hasNext();){
			Element element = (Element) i.next();
			list.add(xmlToMap(element));
		}
		return list;
	}
	public Map<String, Object> xmlToMap(String name){
		
	     try {
			Document doc = DocumentHelper.parseText(getFile(name));
			Element rootElement = doc.getRootElement();
			Map<String, Object> map = xmlToMap(rootElement);
			Map<String, Object> retMap = new HashMap<>();
			retMap.put(rootElement.getName(), map);
			return retMap;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	     return null;
	}
	
	public static void xmltomap(){
		XmlTest test = new XmlTest();
		 String name = "test01.xml";
		Map<String, Object> map = test.xmlToMap(name);
		//System.out.println("map:" + map);
		System.out.println("list size=0-->json:" + JSONObject.toJSONString(map));
		name = "test02.xml";
		map = test.xmlToMap(name);
		System.out.println("list size=1-->json:" + JSONObject.toJSONString(map));
		name = "test03.xml";
		map = test.xmlToMap(name);
		System.out.println("list size=2-->json:" + JSONObject.toJSONString(map));
		name = "test04.xml";
		map = test.xmlToMap(name);
		System.out.println("list in list size=0-->json:" + JSONObject.toJSONString(map));
		name = "test05.xml";
		map = test.xmlToMap(name);
		System.out.println("list in list size!=0-->json:" + JSONObject.toJSONString(map));
	}
	public static void main(String[] args) {
		dom4j();
	
		
	}

	
//	public static void sax(){
//		SAXParserFactory spf = SAXParserFactory.newInstance();
//		try {
//			 String name = "test01.xml";
//			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
//			 DocumentBuilder db = dbf.newDocumentBuilder();  
//			 org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream((name).getBytes()));
//			System.out.println(doc.getTextContent());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
