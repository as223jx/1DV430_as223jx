package com.example.receptapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ViewRecipe extends ActionBarActivity {
	static String openRecipe;
	static String recipeName;
	static String recipeNameTwo;
	public final static String EXTRA_FILENAME = "com.example.receptapp.FILENAME";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		Intent viewRecipe = getIntent();
		openRecipe = viewRecipe.getStringExtra(MainActivity.EXTRA_CLICKED);
		recipeName = viewRecipe.getStringExtra(NewRecipeActivity.EXTRA_RECIPENAME);
		recipeNameTwo = viewRecipe.getStringExtra(EditRecipe.EXTRA_RECIPENAME);
		
		System.out.println("Klick: " + openRecipe);
		System.out.println("Recipename: " + recipeName);
		System.out.println("RecipenameTwo: " + recipeNameTwo);
		
		if(openRecipe == null && recipeName == null){
			System.out.println("openRecipe == null");
			openRecipe = recipeNameTwo;
		}
		else if(openRecipe == null){
			openRecipe = recipeName;
		}
		
		System.out.println("Klick: " + openRecipe);
		System.out.println("Recipename: " + recipeName);
		System.out.println("RecipenameTwo: " + recipeNameTwo);
		

		System.out.println("OpenRecipe: " + openRecipe);
		
//		Intent intent = getIntent();
//		String name = intent.getStringExtra(NewRecipeActivity.EXTRA_NAME);
//		String ingredients = intent.getStringExtra(NewRecipeActivity.EXTRA_INGREDIENTS);
//		String instructions = intent.getStringExtra(NewRecipeActivity.EXTRA_INSTRUCTIONS);
//		String amount = intent.getStringExtra(NewRecipeActivity.EXTRA_AMOUNT);
//		
//		TextView viewName = new TextView(this);
//		viewName.setText("Namn:\n" + name);
//		
//		TextView viewIngredients = new TextView(this);
//		viewIngredients.setText("\nIngredienser:\n" + amount + " " + ingredients);
//		
//		TextView viewInstructions = new TextView(this);
//		viewInstructions.setText("\nInstruktioner:\n" + instructions);
//		
//		LinearLayout viewLayout = new LinearLayout(this);
//		viewLayout.setOrientation(LinearLayout.VERTICAL);
//		viewLayout.addView(viewName);
//		viewLayout.addView(viewIngredients);
//		viewLayout.addView(viewInstructions);
//		
//		setContentView(viewLayout);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_recipe, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_view_recipe,
					container, false);
			Button editRecipeButton = (Button) rootView.findViewById(R.id.edit_btn);
			//Button deleteRecipeButton = (Button) rootView.findViewById(R.id.delete_btn);
			
			editRecipeButton.setOnClickListener(new OnClickListener() {
            	public void onClick(View v){
            		System.out.println(openRecipe.toLowerCase());
            		
            		Intent editRecipeInt = new Intent(getActivity(), EditRecipe.class);
            		editRecipeInt.putExtra(EXTRA_FILENAME, openRecipe.toLowerCase());
                	startActivity(editRecipeInt);
                	getActivity().finish();
            	}
            });
			
//			deleteRecipeButton.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					
//            		File dir = getActivity().getFilesDir();
//            		File file = new File(dir, openRecipe.toLowerCase());
//            		boolean deleted = file.delete();
//            		
//            		Intent mainActivity = new Intent(getActivity(), MainActivity.class);
//            		startActivity(mainActivity);
//            		getActivity().finish();
//				}
//			});
			
			ViewRecipe read = (ViewRecipe) getActivity();

	    	TextView viewRecipe = (TextView) rootView.findViewById(R.id.view_recipe);
			read.displayRecipe(viewRecipe, openRecipe.toLowerCase());
			
			return rootView;
		}
	}
	
	public void deleteRecipe(){
		File dir = this.getFilesDir();
		File file = new File(dir, openRecipe.toLowerCase());
		boolean deleted = file.delete();
		
		Intent mainActivity = new Intent(this, MainActivity.class);
		startActivity(mainActivity);
		this.finish();
	}
	
	public boolean confirm(View v) {
	        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	        alertbox.setTitle("Bekräfta");
	        alertbox.setMessage("Vill du verkligen ta bort receptet?");

	        alertbox.setPositiveButton("Ja",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface arg0, int arg1) {
	                    	deleteRecipe();
	                    }
	                });

	        alertbox.setNeutralButton("Nej",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface arg0, int arg1) {
	                    	
	                    }
	                });

	        alertbox.show();
	        return false;
	    } 
	
	  
	@Override
    public void onBackPressed() {
        super.onBackPressed();   
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();

    }
	
    private String displayRecipe(TextView viewRecipe, String fileName){
    	
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
        		line = line.replace("#", " ");
        		stringBuilder.append(line);
        		stringBuilder.append("\n");
        		if(line.toLowerCase().contains("ingrediens")){
        			System.out.println("ingrediens");
        		}
        		if(line.contains("#")){
        			System.out.println("#");
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
}
