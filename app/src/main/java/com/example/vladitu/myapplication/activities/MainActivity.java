package com.example.vladitu.myapplication.activities;

import android.app.SearchManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import com.example.vladitu.myapplication.R;
import com.example.vladitu.myapplication.fragments.ContactsFragment;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{
    private ContactsFragment mContactsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactsFragment = (ContactsFragment) getFragmentManager().findFragmentById(R.id.contacts_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        checkAndSearch(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        checkAndSearch(s);
        return false;
    }

    String previousText = null;
    private void checkAndSearch(String searchText){
        searchText = searchText.trim();
        if(previousText == null) {
            previousText = searchText;
        }
        if (filterLongEnough(searchText)) {
            mContactsFragment.search(searchText);
        }
        previousText = searchText;
    }

    private boolean filterLongEnough(String text) {
        if(previousText.length() < text.length()){
            return text.length()  > 2;
        }
        else if(previousText.length() > text.length()){
            return true;
        }
        return false;
    }
}
