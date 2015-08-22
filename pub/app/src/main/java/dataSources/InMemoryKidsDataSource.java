package dataSources;

import android.util.Log;

import com.example.test2.Kid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class InMemoryKidsDataSource extends KidsDataSource {
	private final String TAG = "InMemoryKidsDataSource";
	private final String delimiter = ", ";
	private final String defaultFileName = "Kids";

	private ArrayList<Kid> _kidsArray;

	public InMemoryKidsDataSource(){
		_kidsArray = new ArrayList<Kid>();
		
	}

	@Override
	public void addKid(Kid newKid) throws Exception{
		//verify name unique
		Kid found = getKid(newKid.getName());
		if (found != null){
			throw new Exception("Kid's name is not unique");
		}
		_kidsArray.add(newKid);

	}

	@Override
	public void removeKid(String name) throws Exception{
		for (int i=0; i<_kidsArray.size(); i++){
			if (_kidsArray.get(i).getName().equalsIgnoreCase(name)){
				_kidsArray.remove(i);		
			}
		}
	}

	@Override
	public ArrayList<Kid> getKids() {
		ArrayList<Kid> kids  = new ArrayList<Kid>();
		for (int i=0; i<_kidsArray.size(); i++){
			kids.add(_kidsArray.get(i));
		}
		
		return kids;
	}

	@Override
	public Kid getKid(String name) {
		for (int i=0; i<_kidsArray.size(); i++){
			if (_kidsArray.get(i).getName().equalsIgnoreCase(name)){
				return 	_kidsArray.get(i);		
			}
		}
		// not found
		return null;
	}

	public void load() throws Exception{
		//verify storage card exits
		if (!DataSourceUtilities.isExternalStorageWritable()){
			throw new Exception("Storage card must be inserted befor saving.");
		}

		//get path to file
		File kidsFile = DataSourceUtilities.getDataStorageFile(defaultFileName);
		String path = kidsFile.getAbsolutePath();

		//read comma separated string
		if (kidsFile.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String kidsString = reader.readLine();
			reader.close();

			//populate this data source from the string
			this.fromString(kidsString);
		}
	}

	public void clear() throws Exception{
		//verify storage card exits
		if (!DataSourceUtilities.isExternalStorageWritable()){
			throw new Exception("Storage card must be inserted befor saving.");
		}

		//remove file
		File kidsFile = DataSourceUtilities.getDataStorageFile(defaultFileName);
		kidsFile.delete();

		//reload to create new empty file;
		this.load();
	}


	public void save() throws Exception{
		//verify storage card exits
		if (!DataSourceUtilities.isExternalStorageWritable()){
			throw new Exception("Storage card must be inserted befor saving.");
		}

		//get path to file
		File kidsFile = DataSourceUtilities.getDataStorageFile(defaultFileName);

		//create comma separated string
		String kidsString = toString();

		//write file
		FileWriter writer = new FileWriter(kidsFile);
		writer.write(kidsString);
		writer.close();
	}

	public void close() throws Exception{
		Log.d(TAG, "close");
	}

	public String toString()
	{
		Log.d(TAG, "writeObject");
		String output = "";
		for (int i=0; i<_kidsArray.size(); i++){
			String nextKid = _kidsArray.get(i).toString();
			output = output +  nextKid + delimiter;
		}
		return output;
	}

	//populates contents from a comma separated string
	public void fromString(String input) throws Exception
	{
		Log.d(TAG, "readObject");
		_kidsArray = new ArrayList<Kid>();

		int nextStart = 0;
		for (int i=0; nextStart<input.length(); i++){
			//get name from comma separated list
			int nextEnd = input.indexOf(delimiter,nextStart);
			if (nextEnd>nextStart) {
				String nextEntry = input.substring(nextStart, nextEnd);

				//add kid to array
				Kid nextKid = new Kid(nextEntry);
				_kidsArray.add(nextKid);
			}

			//increment to next start
			nextStart = nextEnd+2;
		}


	}

}
