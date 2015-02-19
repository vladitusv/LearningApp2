package com.example.vladitu.myapplication.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.vladitu.myapplication.R;

/**
 * Created by vlad.itu on 18-Feb-15.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private final static String[] FROM_COLUMNS = {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    private final static int[] TO_IDS = {
            R.id.tvContactName,
            R.id.tvContactPhoneNumber
    };
    private static final String[] PROJECTION =
            {
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

    private static final String SELECTION = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
    private String[] mSelectionArgs = {"%%"};

    private ListView mContactsList;
    private SimpleCursorAdapter mCursorAdapter;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContactsList = (ListView) getActivity().findViewById(R.id.contacts_list);
        mCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contacts_list_item, null, FROM_COLUMNS, TO_IDS, 0);
        mContactsList.setOnItemClickListener(this);
        mContactsList.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, SELECTION, mSelectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) mCursorAdapter.getItem(position);
        int contactId = cursor.getInt(1);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
        intent.setData(uri);
        getActivity().startActivity(intent);
    }

    public void search(String searchText){
        mSelectionArgs[0] = "%" + searchText + "%";
        getLoaderManager().restartLoader(0, null, this);
    }
}
