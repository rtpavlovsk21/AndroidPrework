package com.example.alyksia.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editTxt = getIntent().getStringExtra("edit_text");
        EditText editItem = (EditText) findViewById(R.id.editTextField);
        editItem.setText(editTxt);
    }

    public void hideKeyBoard(TextView tv){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
    }

    public void onSaveBtnClick(View view){
        int pos = getIntent().getIntExtra("position",-1);
        EditText editItem = (EditText) findViewById(R.id.editTextField);
        Intent data = new Intent();
        data.putExtra("edit_text",editItem.getText().toString());
        data.putExtra("position",pos);
        setResult(RESULT_OK, data);
        hideKeyBoard(editItem);
        this.finish();
    }

    public void onCancelBtnClick(View view){
        setResult(RESULT_CANCELED);
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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


