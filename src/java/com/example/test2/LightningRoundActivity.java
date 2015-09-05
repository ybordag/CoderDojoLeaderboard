package com.example.test2;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dataSources.KidsDataSource;

public class LightningRoundActivity extends Activity{
	private final String TAG = "LightningRoundActivity";

	// OnClickListener
	private View.OnClickListener mCheckBoxListener = new View.OnClickListener() {
		public void onClick(View v) {
			//mark the row grey
			CheckBox checkbox = (CheckBox)v;
			TableRow clickedRow = (TableRow)v.getParent();
			TextView nameText = (TextView)clickedRow.getChildAt(1); //name
			if (checkbox.isChecked()) {
				nameText.setPaintFlags(nameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}
			else{
				nameText.setPaintFlags( nameText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			}

		}
	};



	private KidsDataSource mKidsData;

	private TableLayout mKidsTable;
	private EditText mNewNameTextBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightning_round);

		//get data for table
		mKidsData = ApplicationFactory.getInstance().getKidsDataSource();

		//populate view
		mNewNameTextBox = (EditText)findViewById(R.id.lightningRoundAddNameText);
		mKidsTable = (TableLayout)findViewById(R.id.lightningRoundTable);
		populateKidsTable();
	}
	
	private void populateKidsTable(){
		ArrayList<Kid> kids = mKidsData.getKids();

		//ArrayList<Kid> kids = MainActivity.KidsData.getKids();
		for (int i=0; i<kids.size(); i++){
			String name = kids.get(i).getName();
			addKidToTable(i, name);
		}
	}

	private void addKidToTable(int i, String name) {
		//add row to table
		TableRow nextRow = new TableRow(this);
		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);
		if (i%2==0) {
			nextRow.setBackgroundColor(Color.LTGRAY);
		}
		else{
			nextRow.setBackgroundColor(Color.argb(50,255,0,0));
		}
		RectShape rectangle = new RectShape();
		ShapeDrawable background = new ShapeDrawable(rectangle);
		background.setPadding(4,2,16,2);
		Paint painter = background.getPaint();
		painter.setColor(Color.argb(100,100,0,0));
		//nextRow.setBackground(background); --- annot use unless restrict to higher API levels



		//show Kid name in text view in new row
		TextView nextText = new TextView(this);
		nextText.setText(name);
		nextText.setPadding(2,2,16,2);

		//provide checkbox to indicate has already presented
		CheckBox nextCheckBox = new CheckBox(this);
		nextCheckBox.setPadding(2,2,2,2);
		nextCheckBox.setOnClickListener(this.mCheckBoxListener);

		//setup row with checkbox, name
		nextRow.addView(nextCheckBox);
		nextRow.addView(nextText);
		
		mKidsTable.addView(nextRow, i);
	}

	private void onEditListener(View view){

	}

	private void appendKidToTable(String name) {
		int count = mKidsTable.getChildCount();
		addKidToTable(count,name);
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
				showMessage("This Kid is already in the Lightning Round");
				//clear the edit box
				mNewNameTextBox.setText("");
 				return;
			}
			
			//add the kid to the database
			Kid newKid = new Kid(newName);
			mKidsData.addKid(newKid);

			//add kid to the table
			appendKidToTable(newName);
			
			//clear the edit box
			mNewNameTextBox.setText("");
		}
		catch(Exception ex){
			showMessage("Unable to add:  " + ex.getMessage());
		}
		
	}


    private void showMessage(String message){
		ApplicationUtilities.showMessage(this, message);
    }

}
