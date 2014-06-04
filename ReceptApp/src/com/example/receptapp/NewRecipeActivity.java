package com.example.receptapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;

public class NewRecipeActivity extends ActionBarActivity {

	public final static String EXTRA_NAME = "com.example.receptapp.NAME";
	public final static String EXTRA_INGREDIENTS = "com.example.receptapp.INGREDIENTS";
	public final static String EXTRA_INSTRUCTIONS = "com.example.receptapp.INSTRUCTIONS";
	public final static String EXTRA_AMOUNT = "com.example.receptapp.AMOUNT";
	public final static String EXTRA_RECIPENAME = "com.example.receptapp.RECIPENAME";
	static int i = 0;
	public static String SPINNER = "com.example.receptapp.SPINNER";
	public static ArrayList<EditText> newIngredients = new ArrayList<EditText>();
	public static ArrayList<EditText> newAmounts = new ArrayList<EditText>();
	public static ArrayList<Spinner> newUnits = new ArrayList<Spinner>();
	public static ArrayList<String> newUnitsString = new ArrayList<String>();
	//public static String[] ingredientArr;
	//public static String[] amountArr;
	//public static String[] spinnerArr;
	//public static String units;
	String unitString;
	int unfinished = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
		
		newIngredients.clear();
		newAmounts.clear();
		newUnits.clear();
		newUnitsString.clear();
		
//		Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units_array, android.R.layout.simple_spinner_item);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		unitSpinner.setAdapter(adapter);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
	}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_recipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Intent main = new Intent(this, MainActivity.class);
	        startActivity(main);
	        finish();
	    }
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_new_recipe,
					container, false);
			
			// Skapar ny LinearLayout och lägger in fält för ny ingrediens
			Button b = (Button) rootView.findViewById(R.id.add_ingredient);
			final LinearLayout ingredientll = (LinearLayout) rootView.findViewById(R.id.newLayout);
			final LinearLayout amountll = (LinearLayout) rootView.findViewById(R.id.amount_layout);
			final LinearLayout spinnerll = (LinearLayout) rootView.findViewById(R.id.spinnerLayout);
			final android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			final android.widget.LinearLayout.LayoutParams amountLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			final android.widget.LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			// Spinner
			Spinner spinner = (Spinner) rootView.findViewById(R.id.unitSpinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.units_array, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
                {
                	SPINNER = parent.getItemAtPosition(pos).toString();
                	//SPINNER = selected;

                }

                public void onNothingSelected(AdapterView<?> parent) 
                {

                }
            });
			
			// Lägg till ny ingrediensrad på plusknappens onClick
			b.setOnClickListener(new OnClickListener(){
			
				@Override
				public void onClick(View v) {
					EditText newIngredient = new EditText(getActivity());
					EditText newAmount = new EditText(getActivity());
					Spinner newSpinner = new Spinner(getActivity());
					ArrayAdapter<CharSequence> newAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.units_array, android.R.layout.simple_spinner_item);
					newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					newSpinner.setAdapter(newAdapter);
					newSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		            {
		                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
		                {
		                	String selected = parent.getItemAtPosition(pos).toString();
		                	System.out.println(selected);
		                }

		                public void onNothingSelected(AdapterView<?> parent) 
		                {

		                }
		            });
					
					//ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, R.array.units_array);
					final float scale = getActivity().getResources().getDisplayMetrics().density;
					int dpsIngr = 170;
					int dpsAmount = 70;
					int pixelsIngr = (int) (dpsIngr * scale + 0.5f);
					int pixelsAmount = (int) (dpsAmount * scale + 0.5f);
					
					//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					layoutParams.bottomMargin = 20;
					amountLayoutParams.bottomMargin = 20;
					spinnerLayoutParams.bottomMargin = 25;
					newIngredient.setLayoutParams(layoutParams);
					newIngredient.setHint("Ange ingrediens");
					newIngredient.setWidth(pixelsIngr);
					newIngredient.setMaxWidth(pixelsIngr);
					newIngredient.setSingleLine(true);
					newIngredient.setBackgroundColor(getResources().getColor(android.R.color.white));
					
					newAmount.setHint("Antal");
					newAmount.setBackgroundColor(getResources().getColor(android.R.color.white));
					newAmount.setWidth(pixelsAmount);
					newAmount.setMaxWidth(pixelsAmount);
					newAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					//newAmount.setTransformationMethod();
					System.out.println("Värde av i: " + i);
					try{
						newIngredients.add(newIngredient);
						System.out.println("Ny ingrediens: " + newIngredients.get(0).getText());
						}
					catch (Exception e) { System.out.println("Gick inte lägga till i ingredientslistan"); }
					try{
						newAmounts.add(newAmount);
					}
					catch (Exception e) { System.out.println("Gick inte lägga till i amountlistan"); }
					try{
						System.out.println("newSpinner: " + newSpinner);
						System.out.println("Get selected item: " + newSpinner.getSelectedItem());
						newUnits.add(newSpinner);
					}
					catch(Exception e) { System.out.println("Gick inte lägga till i spinnerlistan"); }
					
					newIngredient.setLayoutParams(layoutParams);
					newAmount.setLayoutParams(amountLayoutParams);
					newSpinner.setLayoutParams(spinnerLayoutParams);
					newSpinner.setAdapter(newAdapter);
					ingredientll.addView(newIngredient);
					amountll.addView(newAmount);
					spinnerll.addView(newSpinner);
				}
			});
				
			return rootView;
		}
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();   
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();

    }
	
	public void writeToFile(View view) {
		try{
			//Sparar infon till en ny fil
			
			EditText editName = (EditText) findViewById(R.id.edit_name);
			String recipename = editName.getText().toString();
			EditText editAmount = (EditText) findViewById(R.id.edit_amount);
			String amount = editAmount.getText().toString();
			EditText editIngredient = (EditText) findViewById(R.id.edit_ingredient);
			String ingredient = editIngredient.getText().toString();
			EditText editInstructions = (EditText) findViewById(R.id.edit_instructions);
			String instructions = editInstructions.getText().toString();
			String filename = recipename.toLowerCase() + ".txt";
			filename = filename.replace(" ", "_");
			
        	File testFile = new File(this.getFilesDir() + "/" + filename);
        	String path = testFile.getAbsolutePath();
        	System.out.println("1 " + path);
        	
        	if(testFile.exists() && !testFile.isDirectory()) {
        		Toast.makeText(getApplicationContext(), "Receptnamnet finns redan!", Toast.LENGTH_SHORT).show();
        		}
        	
        	else{
			
				if(recipename != null && amount != null &&
					ingredient != null && ingredient.length() != 0
					&& amount.length() != 0 && recipename.length() != 0){
						String newIngredientsStr = "";
						for(int j = 0; j < newIngredients.size(); j++){
							if (newIngredients.get(j).getText() != null && newIngredients.get(j).getText().length() != 0
									&& newAmounts.get(j).getText() != null && newAmounts.get(j).getText().length() != 0){
								newIngredientsStr += "\n" + newAmounts.get(j).getText().toString() + "#" +
													newUnits.get(j).getSelectedItem() + "#" +
													newIngredients.get(j).getText().toString();
							}
							else{
								unfinished ++;
							}
						}

						if(unfinished > 0){
							Toast.makeText(getApplicationContext(), "Du har ofärdiga ingrediensrader!", Toast.LENGTH_SHORT).show();
						}
						else{
						String recipe = recipename + "\n\nIngredienser: \n" + amount + "#" + SPINNER + "#" + ingredient + newIngredientsStr + "\n\nInstruktioner: \n" + instructions;
						FileOutputStream fOut = openFileOutput(filename, MODE_WORLD_READABLE);
						OutputStreamWriter osw = new OutputStreamWriter(fOut);
						osw.write(recipe);
						osw.flush();
						osw.close();
						Toast.makeText(getApplicationContext(), "Receptet sparat!" + filename, Toast.LENGTH_SHORT).show();
						
						for(int i = 0; i < newUnits.size(); i++){
							System.out.println("SELECTED ITEMS" + newUnits.get(i).getSelectedItem());
						}
						
				    	Intent viewRecipe = new Intent(this, ViewRecipe.class);
				    	viewRecipe.putExtra(EXTRA_RECIPENAME, filename);
				    	startActivity(viewRecipe);
				    	finish();
						

						}
					}
				else{
					Toast.makeText(getApplicationContext(), "Fyll i alla fält!", Toast.LENGTH_SHORT).show();
				}
			}
			
		} catch (Exception ex)
		{ Toast.makeText(getApplicationContext(), "Kunde inte spara receptet!", Toast.LENGTH_SHORT).show(); }
	}
}