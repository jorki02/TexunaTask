package com.jorki.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.jorki.generator.PageSettings;
import com.jorki.generator.ReportGenerator;
import com.jorki.generator.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Main
{

    public static void main(String[] args)
    {
        XmlParser xmlParser = new XmlParser();
        File ftsv = new File("src/properties/source-data.tsv");
        File fXml = new File("src/properties/settings.xml");
        PageSettings pageSettings = xmlParser.parse(fXml);
        ReportGenerator reportGenerator = new ReportGenerator(pageSettings);
        reportGenerator.generateReport(ftsv);
    }
}
