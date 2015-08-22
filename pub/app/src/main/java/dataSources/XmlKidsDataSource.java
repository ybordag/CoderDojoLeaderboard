package dataSources;

import android.util.Log;

import com.example.test2.ApplicationUtilities;
import com.example.test2.Kid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/*KidsDataSource implemented as XML file*/
public class XmlKidsDataSource extends KidsDataSource {
	public final String TAG = "XmlKidDataSource";

	//elements
	public static String RootElement = "Kids";
	public static String KidElement = "Kid";
	public static String NameAttribute = "Name";

	private final String defaultFileName = "KidsXML";

	private TransformerFactory mTransformerFactory;
	private Transformer mTransformer;


	private String mFileName;
	private DocumentBuilder mDocBuilder;
	private Document mKidsXml;
	private Element mXmlRoot;


	/******************
	 * Default constructor uses default xml filename
	 * @throws Exception
	 */
	public XmlKidsDataSource()throws Exception{
		initialize(defaultFileName);
	}


	/***************
	 * Constructor specifies filename
	 * @param fileName:  name of file containing XML
	 * @throws Exception
	 */
	public XmlKidsDataSource(String fileName) throws Exception{
		initialize(fileName);
	}

	/****************
	 * Iitialize used by constructors to read file and populate DOM
	 * @param fileName
	 * @throws Exception
	 */
	private void initialize(String fileName) throws Exception {
		mTransformerFactory = TransformerFactory.newInstance();
		mTransformer = mTransformerFactory.newTransformer();

		mFileName = fileName;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		mDocBuilder = factory.newDocumentBuilder();
		mKidsXml = mDocBuilder.newDocument();

		//create root
		mXmlRoot = mKidsXml.createElement(RootElement);
		mKidsXml.appendChild(mXmlRoot);
	}

	@Override
	/****************
	 * Adds a Kid to the data
	 */
	public void addKid(Kid newKid)  throws Exception{
		String name = newKid.getName();
		
		//verify name unique
		Kid found = getKid(name);
		if (found != null){
			Log.d(TAG, "addKid name not unique:  " + name);
			throw new Exception("Kid's name is not unique");
		}
		
		Element newKidElement = mKidsXml.createElement(KidElement);
		mXmlRoot.appendChild(newKidElement);
		
		Attr newKidName = mKidsXml.createAttribute(NameAttribute);
		newKidName.setValue(name);
		newKidElement.setAttributeNode(newKidName);
				
	}

	@Override
	/*******************
	 * Removes a Kid from the data
	 */
	public void removeKid(String name) throws Exception {
		NodeList nodes = mXmlRoot.getElementsByTagName(KidElement);
		for (int i=0; i<nodes.getLength(); i++){
			Element nextElement = (Element)nodes.item(i);
			Attr nextNameAttribute = nextElement.getAttributeNode(NameAttribute);
			String nextName = nextNameAttribute.getValue();
			if (nextName.equalsIgnoreCase(name)){
				//need to remove this element
				mXmlRoot.removeChild(nextElement);
			}
		}
		//do nothing if kid not in list
	}

	@Override
	/******************
	 * Returns an ArrayList of Kids
	 */
	public ArrayList<Kid> getKids() {
		ArrayList<Kid> kids = new ArrayList<Kid>();
		int size = kids.size();
		NodeList nodes = mXmlRoot.getElementsByTagName(KidElement);
		for (int i=0; i<nodes.getLength(); i++){
			Element nextElement = (Element)nodes.item(i);
			Attr nextNameAttribute = nextElement.getAttributeNode(NameAttribute);
			String nextName = nextNameAttribute.getValue();
			Kid nextKid = new Kid(nextName);
			kids.add(nextKid);
		}
		return kids;
	}

	@Override
	/*******************
	 * Returns the Kid with the specified username
	 */
	public Kid getKid(String name) {
		NodeList nodes = mXmlRoot.getElementsByTagName(KidElement);
		for (int i=0; i<nodes.getLength(); i++){
			Element nextElement = (Element)nodes.item(i);
			Attr nextNameAttribute = nextElement.getAttributeNode(NameAttribute);
			String nextName = nextNameAttribute.getValue();
			if (nextName.equalsIgnoreCase(name)){
				Kid foundKid = new Kid(nextName);
				return foundKid;
			}
		}

		//not found - return null
		return null;
	}


	/****************
	 * Populates data source from file
	 * @throws Exception
	 */
	public void load() throws Exception{
		Log.d(TAG,"load()");
		//verify storage card exits
		if (!DataSourceUtilities.isExternalStorageReadable()){
			throw new Exception("Storage card must be inserted befor reading.");
		}

		//get path to file
		try {
			File kidsFile = DataSourceUtilities.getDataStorageFile(mFileName);
			Log.d(TAG, kidsFile.getPath());
			if (kidsFile.exists()
					&& kidsFile.isFile()
					&& kidsFile.canRead()
					&& kidsFile.canWrite())
			{
				Log.d(TAG, "file exists");

				//String kidsXML = readXmlFile();
				//Log.d(TAG, kidsXML);

				//read XML file into DOM
				Document loadedDoc = mDocBuilder.parse(kidsFile);
				//get root
				Element loadedRoot = (Element) mKidsXml.getElementsByTagName(RootElement).item(0);
				NodeList loadedChildren = loadedRoot.getChildNodes();

				//should validate against XML schema.  Load only if good XML
				if ((loadedDoc != null) && (loadedRoot != null)) {
					mKidsXml = loadedDoc;
					mXmlRoot = loadedRoot;
					NodeList nodes = mXmlRoot.getChildNodes();
				}
			}
			else{
				initialize(mFileName);
			}
		}catch(Exception ex){
			Log.d(TAG, "Unable to read/parse XML file" + ex.getMessage());
		}

	}

	private String readXmlFile(){
		String kidsString = "";
		//get path to file
		try {
			File kidsFile = DataSourceUtilities.getDataStorageFile(mFileName);
			String path = kidsFile.getAbsolutePath();

			//read comma separated string
			if (kidsFile.exists()) {
				/*
				char[] buffer = new char[4000];
				FileReader reader = new FileReader(path);
				int readSize = reader.read(buffer,0,4000);
				reader.close();
				*/

				BufferedReader lineReader = new BufferedReader(new FileReader(path));
				kidsString = lineReader.readLine();
				lineReader.close();

			}
		}
		catch(Exception ex){
			Log.d(TAG, "Error reading XML file:  " + ex.getMessage());
		}

		return kidsString;
	}

	/*
	Delete current data and start empty
	 */
	public void clear() throws Exception{
		//verify storage card exits
		if (!DataSourceUtilities.isExternalStorageWritable()){
			throw new Exception("Storage card must be inserted before clearing.");
		}

		//remove file
		File kidsFile = DataSourceUtilities.getDataStorageFile(defaultFileName);
		if (kidsFile.exists()) {
			kidsFile.delete();
		}


	}

	/**********************
	 * Saves XML to file
	 * @throws Exception
	 */
	public void save() throws Exception{
 		File kidsFile = DataSourceUtilities.getDataStorageFile(mFileName);
        DOMSource source = new DOMSource(mKidsXml);
        StreamResult result = new StreamResult(new File(kidsFile.getAbsolutePath()));
        mTransformer.transform(source, result);
    }

	public void close() throws Exception{
		Log.d(TAG, "close");
	}

	/*********************
	 * Creaes XML string representation
	 * @return: XML String representing the data
	 */
	public String toString(){
		Log.d(TAG,"toString");
		String output = "";
		output = this.mKidsXml.toString();

		try {
			DOMSource source = new DOMSource(mKidsXml);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			mTransformer.transform(source, result);
			output = writer.toString();
		}
		catch(Exception ex){
			Log.d(TAG, "Exception in toString(): " + ex.getMessage());
		}

		return output;
	}

	/*****************
	 * Parses XML string to populate DOM
	 * @param input: XML string containing Kids data
	 * @throws Exception
	 */
	public void fromString(String input) throws Exception{
		//reset xml document to null in case of failure
		mKidsXml = null;

		//parse the string
		StringReader reader = new StringReader(input);
		InputSource source = new InputSource(reader);
		mKidsXml = mDocBuilder.parse(source);

		//get root
		mXmlRoot = (Element)mKidsXml.getElementsByTagName("kids").item(0);

	}
	
	

}
//</editor-fold>
