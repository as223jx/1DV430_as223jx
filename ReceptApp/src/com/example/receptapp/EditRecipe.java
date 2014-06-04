package com.example.receptapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

public class EditRecipe extends ActionBarActivity {

	static String fileName;
	static String recipeName;
	static String ingredients;
	static String instructions;
	static String ingredientsStr = "";
	static String spinnerPos;
	static int count = 0;
	public static ArrayList<String> parts = new ArrayList<String>();
	static int partsCounter;
	public static ArrayList<String> allIngredients = new ArrayList<String>();
	public static ArrayList<String> allAmounts = new ArrayList<String>();
	public static ArrayList<String> allUnits = new ArrayList<String>();
	public static ArrayList<EditText> newIngredients = new ArrayList<EditText>();
	public static ArrayList<EditText> newAmounts = new ArrayList<EditText>();
	public static ArrayList<Spinner> newUnits = new ArrayList<Spinner>();
	public static String SPINNER = "com.example.receptapp.SPINNEREDIT";
	static LinearLayout ingredientll;
	static LinearLayout amountll;
	static LinearLayout spinnerll;
	public final static String EXTRA_RECIPENAME = "com.example.receptapp.EXRECIPENAME";
	int unfinished = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		//Hämtar filnamnet beroende på vart intenten skickades ifrån
		try{
			Intent editRecipe = getIntent();
			fileName = editRecipe.getStringExtra(MainActivity.EXTRA_CLICKED);
		}
		catch(Exception ex){ System.out.println("Gick inte hämta filnamnet!"); }
		
		try{
			Intent editRecipeInt = getIntent();
			fileName = editRecipeInt.getStringExtra(ViewRecipe.EXTRA_FILENAME);
		}
		catch(Exception e){ System.out.println("Gick inte hämta filnamnet!"); }
		
		allIngredients.clear();
		allAmounts.clear();
		allUnits.clear();
		parts.clear();
		newIngredients.clear();
		newAmounts.clear();
		newUnits.clear();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_recipe, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_edit_recipe,
					container, false);
			
			EditRecipe read = (EditRecipe) getActivity();

	    	TextView viewRecipe = (TextView) rootView.findViewById(R.id.abc);
			read.displayRecipe(viewRecipe, fileName);
			
			EditText recipeNameView = (EditText) rootView.findViewById(R.id.edit_edit_name);
			EditText ingredientView = (EditText) rootView.findViewById(R.id.edit_edit_ingredient);
			EditText instructionsView = (EditText) rootView.findViewById(R.id.edit_edit_instructions);
			EditText amountView = (EditText) rootView.findViewById(R.id.edit_edit_amount);
			recipeNameView.setText(recipeName);
			ingredientView.setText(allIngredients.get(0));
			amountView.setText(allAmounts.get(0));
			instructionsView.setText(instructions);
			
			Spinner spinner = (Spinner) rootView.findViewById(R.id.edit_unitSpinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.units_array, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			int spinnerPosition = adapter.getPosition(allUnits.get(0));
			spinner.setSelection(spinnerPosition);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
	        {
	            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	            {
	            	spinnerPos = parent.getItemAtPosition(pos).toString();
	            	SPINNER = parent.getItemAtPosition(pos).toString();
	            	//SPINNER = selected;

	            }

	            public void onNothingSelected(AdapterView<?> parent) 
	            {

	            }
	        });
			
			Button b = (Button) rootView.findViewById(R.id.edit_add_ingredient);
//			final LinearLayout ingredientll = (LinearLayout) rootView.findViewById(R.id.edit_newLayout);
//			final LinearLayout amountll = (LinearLayout) rootView.findViewById(R.id.edit_amount_layout);
//			final LinearLayout spinnerll = (LinearLayout) rootView.findViewById(R.id.edit_spinnerLayout);
			ingredientll = (LinearLayout) rootView.findViewById(R.id.edit_newLayout);
			amountll = (LinearLayout) rootView.findViewById(R.id.edit_amount_layout);
			spinnerll = (LinearLayout) rootView.findViewById(R.id.edit_spinnerLayout);

			final android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			final android.widget.LinearLayout.LayoutParams amountLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			final android.widget.LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			for(int i = 1; i < allIngredients.size(); i++){
				
				EditText newIngredient = new EditText(getActivity());
				EditText newAmount = new EditText(getActivity());
				Spinner newSpinner = new Spinner(getActivity());
				ArrayAdapter<CharSequence> newAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.units_array, android.R.layout.simple_spinner_item);
				newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				//Hämtar spinnerpositionerna för enheterna
				int spinnerPos = newAdapter.getPosition(allUnits.get(i));
				//newSpinner.setAdapter(newAdapter);
				
				newSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
	            {
	                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	                {
	                	String selected = parent.getItemAtPosition(pos).toString();
	                }
	
	                public void onNothingSelected(AdapterView<?> parent) 
	                {
	
	                }
	            });
				
				final float scale = getActivity().getResources().getDisplayMetrics().density;
				int dpsIngr = 175;
				int dpsAmount = 70;
				int pixelsIngr = (int) (dpsIngr * scale + 0.5f);
				int pixelsAmount = (int) (dpsAmount * scale + 0.5f);
				
				newIngredient.setWidth(pixelsIngr);
				newIngredient.setMaxWidth(pixelsIngr);
				newIngredient.setSingleLine(true);
				newAmount.setWidth(pixelsAmount);
				newAmount.setMaxWidth(pixelsAmount);
				newAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				//ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, R.array.units_array);
				newIngredient.setHint("Ange ingrediens");
				newIngredient.setText(allIngredients.get(i));
				newAmount.setHint("Antal");
				newAmount.setText(allAmounts.get(i));
				
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
					newUnits.add(newSpinner);
				}
				catch(Exception e) { System.out.println("Gick inte lägga till i spinnerlistan"); }
				
				newIngredient.setLayoutParams(layoutParams);
				newAmount.setLayoutParams(amountLayoutParams);
				newSpinner.setLayoutParams(spinnerLayoutParams);
				newSpinner.setAdapter(newAdapter);
				newSpinner.setSelection(spinnerPos);
				ingredientll.addView(newIngredient);
				amountll.addView(newAmount);
				spinnerll.addView(newSpinner);
			}
			
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
					newIngredient.setHint("Ange ingrediens");
					newAmount.setHint("Antal");
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
	};
	
	private void exit(){
		Toast.makeText(getApplicationContext(), "Lämnat!", Toast.LENGTH_LONG).show();
	    this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

	        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	        alertbox.setTitle("Message");
	        alertbox.setMessage("Vill du lämna utan att spara?");

	        alertbox.setPositiveButton("Yes",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface arg0, int arg1) {
	                        onLeave();
	                    	//exit();
	                    }
	                });

	        alertbox.setNeutralButton("No",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface arg0, int arg1) {
	                    }
	                });

	        alertbox.show();

	        return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	private String displayRecipe(TextView viewRecipe, String fileName){
    	partsCounter = 0;
    	String line = "";
        String recipe = "";
        try{
        	File testFile = new File(this.getFilesDir() + "/" + fileName);
        	String path = testFile.getAbsolutePath();
        	
        	InputStream inputStream = new FileInputStream(path);

        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        	StringBuilder stringBuilder = new StringBuilder();
    
        	while((line = bufferedReader.readLine()) != null){
        		stringBuilder.append(line);
        		stringBuilder.append("\n");
        		
        		//Receptnamnet
        		if(line.toLowerCase().contains(fileName.replace(".txt", ""))){
        			recipeName = line;
        			count = 1;
        		}
        		// Ingrediensrubriken
        		if(count == 1 && line.toLowerCase().contains("ingrediens")){
        			count = 2;
        		}
        		
        		else if(count == 2){
        			// Kollar om raden är Instruktionsrubriken
        			if(line.toLowerCase().contains("instruktioner")){
        				count = 3;
        			}
        			// Lägger till raderna som ingredienser så länge inte instruktionerna börjat
        			else if(line != null && line.length() != 0){
        				
        				try{
        				String partsArr[] = line.split("\\#");
        				for(int i = 0; i < partsArr.length; i++){
        				parts.add(partsArr[i]);
        				}
        				for(int j = 0; j < partsArr.length; j++){
	        				allAmounts.add(parts.get(partsCounter));
	        				allUnits.add(parts.get(partsCounter+1));
	        				allIngredients.add(parts.get(partsCounter+2));
	        				partsCounter = partsCounter + 3;
	        				
	        				for(int k = 0; k < allUnits.size(); k ++){
	        					System.out.println("All units: "+ allUnits.get(k));
	        				}
	        				
        				}
        				}
        				catch (Exception e){ System.out.println("Gick inte dela strängen");}
            			//allIngredients.add(line);
        			}
        		}
        		// Om instruktionsrubriken är skriven läggs resterande rader till i instruktioner
        		else if(count == 3){
        			count = 4;
        			if(line != null && line.length() != 0){
        				instructions = line;
        			}
        		}
        		//partsCounter = +3;
        	}

			for(int j = 0; j < parts.size(); j++){
				if(parts.get(j) != null || parts.get(j).length() != 0){
				}
			}
        	
        	inputStream.close();
        	recipe = stringBuilder.toString();
        	viewRecipe.setText(recipe);
        }
        catch(FileNotFoundException ex) {
        	System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
        	System.out.println("Error reading file '" + fileName + "'");
        }
    	catch (NullPointerException ex){
    		System.out.println("NullPointerException '" + fileName + "'");
    	}
        
        return recipe;
    }
	
	public void writeToFile(View view) {
		try{
			//Sparar infon till en ny fil
			
			EditText editName = (EditText) findViewById(R.id.edit_edit_name);
			String recipename = editName.getText().toString();
			EditText editAmount = (EditText) findViewById(R.id.edit_edit_amount);
			String amount = editAmount.getText().toString();
			EditText editIngredient = (EditText) findViewById(R.id.edit_edit_ingredient);
			String ingredient = editIngredient.getText().toString();
			EditText editInstructions = (EditText) findViewById(R.id.edit_edit_instructions);
			String instructions = editInstructions.getText().toString();
			
			if(new String(recipename + ".txt").equals(fileName)){
				Toast.makeText(getApplicationContext(), "Samma namn!", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Nytt namn!", Toast.LENGTH_SHORT).show();
				File dir = this.getFilesDir();
        		File file = new File(dir, fileName.toLowerCase());
        		boolean deleted = file.delete();
			}
			
			//String filename = recipename.toLowerCase() + ".txt";
			//filename = filename.replace(" ", "_");
			fileName = recipename.toLowerCase() + ".txt";
			fileName = fileName.replace(" ", "_");
			
        	File testFile = new File(this.getFilesDir() + "/" + fileName);
        	String path = testFile.getAbsolutePath();
        	System.out.println("1 " + path);
 
			
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
						FileOutputStream fOut = openFileOutput(fileName, MODE_WORLD_READABLE);
						OutputStreamWriter osw = new OutputStreamWriter(fOut);
						osw.write(recipe);
						osw.flush();
						osw.close();
						
						try{
						Intent viewRecipe = new Intent(this, ViewRecipe.class);
	            		viewRecipe.putExtra(EXTRA_RECIPENAME, fileName.toLowerCase());
	                	startActivity(viewRecipe);
	                	finish();
						}
						catch(Exception ex){System.out.println("Kunde inte starta ViewRecipe.java");}
						Toast.makeText(getApplicationContext(), "Receptet sparat!", Toast.LENGTH_SHORT).show();
						}
					}
				else{
					Toast.makeText(getApplicationContext(), "Fyll i alla fält!", Toast.LENGTH_SHORT).show();
				}
				
		} catch (Exception ex)
		{ Toast.makeText(getApplicationContext(), "Kunde inte spara receptet!", Toast.LENGTH_SHORT).show(); }
	}
	
	public void onLeave() {  
        Intent viewRecipe = new Intent(this, ViewRecipe.class);
        System.out.println("fileName: " + fileName);
		viewRecipe.putExtra(EXTRA_RECIPENAME, fileName.toLowerCase());
    	startActivity(viewRecipe);
    	finish();
    }
}
