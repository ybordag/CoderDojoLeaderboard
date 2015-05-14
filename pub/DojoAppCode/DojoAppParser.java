package DojoApp;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DojoAppParser 
{
	Document doc;

	public DojoAppParser(File xmlName) 
	{
		try 
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlName);
			doc.getDocumentElement().normalize();

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	public boolean PrintValues()
	{
		boolean bSuccess = false;
		try
		{
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
			bSuccess = true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		return bSuccess;
	}
	
	public int getNumOfCoders()
	{
		NodeList nodes = doc.getElementsByTagName("Coder");
		return nodes.getLength();
	}
	
	public String getCoderName(int i)
	{
		String sName = null;
		
		try
		{
			NodeList nodes = doc.getElementsByTagName("Coder");

			Node node = nodes.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) 
			{
					Element element = (Element) node;
					sName = getValue("Name", element);
			}
			
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}		
		return sName;
	}
	
	public String getCoderActivity(int i)
	{
		String sActivity = null;
		
		try
		{
			NodeList nodes = doc.getElementsByTagName("Coder");

			Node node = nodes.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) 
			{
					Element element = (Element) node;
					sActivity = getValue("Activities", element);
			}
			
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}		
		return sActivity;
	}

	private static String getValue(String tag, Element element) 
	{
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

}
