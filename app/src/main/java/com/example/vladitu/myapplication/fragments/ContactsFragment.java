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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.vladitu.myapplication.R;
import com.example.vladitu.myapplication.adapters.AbstractRecyclerAdapter;
import com.example.vladitu.myapplication.adapters.ContactsRecyclerAdapter;
import com.example.vladitu.myapplication.adapters.NabContactsArrayAdapter;

/**
 * Created by vlad.itu on 18-Feb-15.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AbstractRecyclerAdapter.OnItemClickListener {
    public static final String[] PROJECTION =
            {
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

    private static final String SELECTION = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
    private String[] mSelectionArgs = {"%%"};

    private RecyclerView mContactsList;
    private ContactsRecyclerAdapter mRecyclerAdapter;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContactsList = (RecyclerView) getActivity().findViewById(R.id.contacts_list);
        mContactsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactsList.setHasFixedSize(true);

        mRecyclerAdapter = new ContactsRecyclerAdapter(getActivity(), null);
        mRecyclerAdapter.setOnItemClickListener(this);
        mContactsList.setAdapter(mRecyclerAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, SELECTION, mSelectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerAdapter.setSearchString(mSelectionArgs[0].replace("%",""));
        mRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerAdapter.swapCursor(null);
    }

    public void search(String searchText){
        mSelectionArgs[0] = "%" + searchText + "%";
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onItemClicked(View view, int position) {
        Cursor cursor = mRecyclerAdapter.getCursor();
        cursor.moveToPosition(position);
        int contactId = cursor.getInt(cursor.getColumnIndex(PROJECTION[1]));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
        intent.setData(uri);
        getActivity().startActivity(intent);
    }
}
