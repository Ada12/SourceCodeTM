package dao;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchen on 16-5-2.
 */
public class GetTopicWeight {
    public static List<List> getNames(String path) throws ParserConfigurationException, IOException, SAXException {
        List<List> xml = new ArrayList<List>();
        List<String> ids = new ArrayList<String>();
        List<String> alphas = new ArrayList<String>();
        List<String> titles = new ArrayList<String>();
        File inputFile = new File(path);
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dFactory.newDocumentBuilder();
        Document doc = builder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :"
                + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("topic");
        System.out.println("----------------------------");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            String id = eElement.getAttribute("id");
            ids.add(id);
            String alpha = eElement.getAttribute("alpha");
            alphas.add(alpha);
            String title = eElement.getAttribute("titles");
            titles.add(title);
        }
        xml.add(ids);
        xml.add(alphas);
        xml.add(titles);
        return xml;
    }
}
