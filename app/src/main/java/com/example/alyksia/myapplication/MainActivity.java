package com.example.alyksia.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<TodoItem> items;
    ArrayAdapter<TodoItem> itemsAdapter;
    ListView lvItems;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItemsDB();
        itemsAdapter = new ArrayAdapter<TodoItem>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void hideKeyBoard(TextView tv){
        // Clear keyboard from widget
        InputMethodManager iman = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        iman.hideSoftInputFromWindow(tv.getWindowToken(), 0);

        // Set focus to layout
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.simpleTodoLayout);
        myLayout.requestFocus();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        TodoItem itm = new TodoItem(etNewItem.getText().toString(),0);
        // Don't allow blank entries.
        if(!itm.getBody().isEmpty()) {
            itemsAdapter.add(itm);
            itm.save();
        }
        etNewItem.setText("");
        hideKeyBoard(etNewItem);
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        TodoItem itm = items.get(pos);
                        items.remove(pos);
                        itm.delete();
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    // Start the EditItemActivity
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        // Handle some focus annoyances, if the user is adding a new item then
                        //  goes to edit an item, the keyboard is stuck with focus in MainActivity
                        //  this also tries to fix accidental edits or activations of EditItemActivity
                        if (focusOnEditTextItem()) {
                            defocusNewItemEditText();
                        } else {
                            editAdapterView(adapter, item, pos, id);
                        }
                        return;
                    }
                });
    }

    public void editAdapterView(AdapterView<?> adapter, View item, int pos, long id){
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        String itmTxt = items.get(pos).toString();
        i.putExtra("edit_text", itmTxt);
        i.putExtra("position", pos);
        startActivityForResult(i, REQUEST_CODE);
        return;
    }

    public boolean focusOnEditTextItem(){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        return etNewItem.hasFocus();
    }

    public void defocusNewItemEditText(){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        hideKeyBoard(etNewItem);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editTxt = data.getExtras().getString("edit_text");
            int pos = data.getExtras().getInt("position");

            //if pos=-1 then pos wasn't passed initially through AdapterView.OnItemClickListener or
            // did not make it to EditItemActivity
            if(pos>-1) {
                TodoItem itm = items.get(pos);
                itm.setBody(editTxt);
                itemsAdapter.notifyDataSetChanged();
                itm.save();
            }
        }
    }

    private void readItemsDB(){
        try{
            items = new Select().all().from(TodoItem.class).orderBy("priority ASC").execute();
        }catch (RuntimeException e) {
            items = new ArrayList<>();
        }
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
