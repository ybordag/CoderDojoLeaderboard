package com.example.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import dataSources.KidsDataSource;

public class LeaderBoardActivity extends Activity {
	private final String TAG = "LightningRoundActivity";

	KidsDataSource mKidsData;
	ArrayList<Kid> mKidsList;

	private TableLayout mKidsTable;
	private EditText mNewNameTextBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);

		ApplicationFactory factory = ApplicationFactory.getInstance();
		mKidsData = factory.getKidsDataSource();
		mKidsList = mKidsData.getKids();

		mKidsTable = (TableLayout)findViewById(R.id.leaderBoardTable);
		populateKidsTable();
	}


	private void populateKidsTable(){

		//ArrayList<Kid> kids = MainActivity.KidsData.getKids();
		for (int i=0; i<mKidsList.size(); i++){
			String name = mKidsList.get(i).getName();
			addNameToTable(i, name);
		}

		//append row at bottom to permit adding new kid
		appendEditRow();
	}

	private void addNameToTable(int i, String name) {
		//add row to table
		TableRow nextRow = new TableRow(this);
		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);

		//show Kid name in text view in new row
		TextView nextText = new TextView(this);
		nextText.setText(name);
		nextRow.addView(nextText);

		mKidsTable.addView(nextRow, i);
	}


	private void appendNameToTable(String name) {
		int count = mKidsTable.getChildCount();
		addNameToTable(count, name);
	}

	private void appendEditRow(){
		//add row to table
		TableRow nextRow = new TableRow(this);
		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);

		//add row with edit Text
		mNewNameTextBox = new EditText(this);
		mNewNameTextBox.setHint("new name");
		nextRow.addView(mNewNameTextBox);

		mKidsTable.addView(nextRow, mKidsTable.getChildCount());
	}

	private void addKidToTable(String name) {
		//add row to table
		TableRow nextRow = new TableRow(this);
		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);

		//show Kid name in text view in new row
		TextView nextText = new TextView(this);
		nextText.setText(name);
		nextRow.addView(nextText);

		//push down the edit text row at the bottom
		int bottom = mKidsTable.getChildCount();
		mKidsTable.addView(nextRow,bottom-1);

		//clear the edit text box
		mNewNameTextBox.setText("");
	}

	public void onClickAdd(View view){
		try{
			String newName = mNewNameTextBox.getText().toString();
			//remove leading/trailing spaces
			newName = newName.trim();

			//check name is not empty
			if(newName.isEmpty()){
				//ignore click
				//clear the edit box
				mNewNameTextBox.setText("");
				return;
			}

			//check name is not already in database
			Kid found = mKidsData.getKid(newName);
			if (found != null){
				ApplicationUtilities.showMessage(this, "This Kid is already in the Leader board");
				//clear the edit box
				mNewNameTextBox.setText("");
				return;
			}

			//add the kid to the database
			Kid newKid = new Kid(newName);
			mKidsData.addKid(newKid);

			//show the kid in the table
			addKidToTable(newName);
		}
		catch(Exception ex){
			ApplicationUtilities.showMessage(this,"Unable to add:  " + ex.getMessage());
		}

	}

}
