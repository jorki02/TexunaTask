package com.jorki.generator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public PageSettings parse(File fXml){
        PageSettings pageSettings = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(fXml);

            Element root = doc.getDocumentElement();
            Element pageSize = (Element) root.getElementsByTagName("page").item(0);
            Element pageColumns = (Element) root.getElementsByTagName("columns").item(0);
            NodeList columnNodes = pageColumns.getElementsByTagName("column");
            List<String> columns = new ArrayList<>();
            List<Integer> columnsSize = new ArrayList<>();
            for(int j = 0; j < columnNodes.getLength(); j++){
                columns.add(((Element) columnNodes.item(j)).getElementsByTagName("title").item(0).getTextContent());
                columnsSize.add(Integer.parseInt(((Element) columnNodes.item(j)).getElementsByTagName("width").item(0).getTextContent()));
            }
            pageSettings = new PageSettings(Integer.parseInt(pageSize.getElementsByTagName("width").item(0).getTextContent()),
                    Integer.parseInt(pageSize.getElementsByTagName("height").item(0).getTextContent()),
                    columns,
                    columnsSize);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return pageSettings;
    }

}
