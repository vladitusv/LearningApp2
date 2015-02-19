package com.example.vladitu.myapplication.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.vladitu.myapplication.R;
import com.example.vladitu.myapplication.fragments.ContactsFragment;


public class MainActivity extends ActionBarActivity {
    private ContactsFragment mContactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactsFragment = (ContactsFragment) getFragmentManager().findFragmentById(R.id.contacts_fragment);
        initSearchFilter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_settings:
                break;
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSearchFilter(){
        final TextView searchTextView = (TextView) findViewById(R.id.etSearchText);

        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            String previousText = null;
            @Override
            public void afterTextChanged(Editable s) {

                if(previousText == null) {
                    previousText = searchTextView.getText().toString();
                }
                if (filterLongEnough()) {
                    mContactsFragment.search(searchTextView.getText().toString().trim());
                }
                previousText = searchTextView.getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private boolean filterLongEnough() {
                if(previousText.length() < searchTextView.getText().toString().length()){
                    return searchTextView.getText().toString().trim().length()  > 2;
                }
                else if(previousText.length() > searchTextView.getText().toString().length()){
                    return true;
                }
                return false;
            }
        };
        searchTextView.addTextChangedListener(fieldValidatorTextWatcher);
    }
}
