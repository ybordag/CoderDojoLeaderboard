package DojoApp;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DojoAppMain 
{


	public static void main(String args[]) 
	{
		try 
		{

			File leaderboard = new File("Leaderboard.xml");
			DojoAppParser aParser = new DojoAppParser(leaderboard);
			if (aParser.PrintValues())
				System.out.println("XML parsing successful");
			else
				System.out.println("XML parsing unsuccessful");
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

}
	
	
/*	
	public static void main(String args[]) 
	{
		try 
		{

			File leaderboard = new File("Leaderboard.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(leaderboard);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("Coder");
			System.out.println("==========================");

			for (int i = 0; i < nodes.getLength(); i++) 
			{
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) node;
					System.out.println("Name: " + getValue("Name", element));
					System.out.println("Activity: " + getValue("Activities", element));
				}
			}
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private static String getValue(String tag, Element element) 
	{
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
}*/