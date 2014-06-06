package com.example.receptapp;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
		public final static String EXTRA_CLICKED = "com.example.receptapp.CLICKED";
		static String[] SavedFiles;
		static ListView listSavedFiles;
		static TextView emptyListText;
		boolean backFromChild = false;
		String units;
		String aString;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
	    if (savedInstanceState == null) {
	        getSupportFragmentManager().beginTransaction()
	                .add(R.id.container, new PlaceholderFragment())
	                .commit();
	    
 	   }
    }

	public void newRecipe(View view) {
    	Intent newRecipe = new Intent(this, NewRecipeActivity.class);
    	startActivity(newRecipe);
    	finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            emptyListText = (TextView)rootView.findViewById(R.id.textView2);
            listSavedFiles = (ListView)rootView.findViewById(R.id.list);
            
            //Button editRecipeButton = (Button)rootView.findViewById(R.id.edit_recipe_button);
            
            listSavedFiles.setOnItemClickListener(new OnItemClickListener() {
            	@Override
            	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            		String clicked = listSavedFiles.getItemAtPosition(arg2).toString();
            		clicked = clicked.replace(" ", "_");
            		Intent viewRecipe = new Intent(getActivity(), ViewRecipe.class);
            		viewRecipe.putExtra(EXTRA_CLICKED, clicked.toLowerCase() + ".txt");
                	startActivity(viewRecipe);
                	getActivity().finish();
//            		MainActivity read = (MainActivity) getActivity();
//					read.readFromFile(clicked);
            	}
            });
            
            
            //editRecipeButton.setOnClickListener(new OnClickListener() {
            //	public void onClick(View v){
            		//Intent editRecipe = new Intent(getActivity(), EditRecipe.class);
            		//editRecipe.putExtra(EXTRA_CLICKED, "test.txt");
                	//startActivity(editRecipe);
            //	}
            //});
            
            
            MainActivity read = (MainActivity) getActivity();
            read.listRecipes(listSavedFiles);
//            TextView viewList = (TextView) rootView.findViewById(R.id.test_list);
//       	 	String fileName = "recept.txt";
//            String line = "";
//            try{
//            	File testFile = new File(getActivity().getFilesDir() + "/" + fileName);
//            	String path = testFile.getAbsolutePath();
//            	System.out.println("1 " + path);
//            	
//            	InputStream inputStream = new FileInputStream(path);
//
//            	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            	StringBuilder stringBuilder = new StringBuilder();
//        
//            	while((line = bufferedReader.readLine()) != null){
//            		stringBuilder.append(line);
//            		stringBuilder.append("\n");
//            	}
//            	inputStream.close();
//            	String recipe = stringBuilder.toString();
//            	 System.out.println(recipe);
//            	try{
//            	viewList.setText(recipe);
//            	}
//            	catch (Exception e) { System.out.println("Ok");}
//            }
//            catch(FileNotFoundException ex) {
//            	System.out.println("Unable to open file '" + fileName + "'");
//            }
//            catch(IOException ex) {
//            	System.out.println("Error reading file '" + fileName + "'");
//            }
//        	catch (NullPointerException ex){
//        		System.out.println("NullPointerException '" + fileName + "'");
//        	}
            
//            Button load = (Button) rootView.findViewById(R.id.load_recipe);
//			
//			load.setOnClickListener(new View.OnClickListener(){
//				
//				public void onClick(View v) {
//					MainActivity read = (MainActivity) getActivity();
//
//			    	try{
//					read.readFromFile();
//			    	}
//			    	catch (Exception e) { System.out.println("Något gick fel!"); }
//				}
//			});
            
            return rootView;
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
      if (v.getId()==R.id.list) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(SavedFiles[info.position]);
        String[] menuItems = getResources().getStringArray(R.array.menu);
        for (int i = 0; i<menuItems.length; i++) {
          menu.add(Menu.NONE, i, i, menuItems[i]);
        }
      }
    }
    
    private void listRecipes(ListView listSavedFiles){
    	final android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    	try{
	    	SavedFiles = getApplicationContext().fileList();
	    	if (SavedFiles.length == 0){
	    		emptyListText.setPadding(10, 10, 10, 10);
	    		emptyListText.setText("Du har inga recept! Klicka på 'Nytt Recept' för att skapa ett!");
	    		emptyListText.setBackgroundColor(getResources().getColor(android.R.color.white));
	    		emptyListText.setLayoutParams(layoutParams);
	    	}
	    	Arrays.sort(SavedFiles);
	        //String asString = Arrays.toString(SavedFiles);
	        for(int i = 0; i < SavedFiles.length; i++){
	        	String name = SavedFiles[i].toString();
	        	
	        	String output = name.substring(0, 1).toUpperCase() + name.substring(1);
	        	
	        	if(name.endsWith(".txt")){
	        	SavedFiles[i] = output.replace(".txt", "");
	        	}
	        	SavedFiles[i] = SavedFiles[i].replace("_", " ");
	    	}
        }
    	catch(StringIndexOutOfBoundsException ex){ System.out.println("Gick inte ta bort .txt"); }
        ArrayAdapter<String> adapter
        = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SavedFiles);
        try{

        listSavedFiles.setAdapter(adapter);
        registerForContextMenu(listSavedFiles);
        }
        catch(NullPointerException ex){ System.out.println("Fel"); }

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      int menuItemIndex = item.getItemId();
      String[] menuItems = getResources().getStringArray(R.array.menu);
      String menuItemName = menuItems[menuItemIndex];
      String listItemName = SavedFiles[info.position];

      Toast.makeText(getApplicationContext(), "Hejhopp" + " " + menuItemName + " " + listItemName, Toast.LENGTH_SHORT).show();
      return true;
    }
    
//    private String readFromFile(String fileName){
//    	TextView viewList = (TextView) findViewById(R.id.test_list);
//    	System.out.println("Klick 3");
//    	String line = "";
//        String recipe = "";
//        try{
//        	File testFile = new File(this.getFilesDir() + "/" + fileName);
//        	String path = testFile.getAbsolutePath();
//        	System.out.println("Path: " + path);
//        	
//        	InputStream inputStream = new FileInputStream(path);
//
//        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//        	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        	StringBuilder stringBuilder = new StringBuilder();
//    
//        	while((line = bufferedReader.readLine()) != null){
//        		stringBuilder.append(line);
//        		stringBuilder.append("\n");
//        	}
//        	inputStream.close();
//        	recipe = stringBuilder.toString();
//        	System.out.println(recipe);
//        	viewList.setText(recipe);
//        }
//        catch(FileNotFoundException ex) {
//        	System.out.println("Unable to open file '" + fileName + "'");
//        }
//        catch(IOException ex) {
//        	System.out.println("Error reading file '" + fileName + "'");
//        }
//    	catch (NullPointerException ex){
//    		System.out.println("NullPointerException '" + fileName + "'");
//    	}
//        
//        return recipe;
//    }

}
