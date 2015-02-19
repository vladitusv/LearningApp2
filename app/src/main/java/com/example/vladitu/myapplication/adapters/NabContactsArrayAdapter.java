package com.example.vladitu.myapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladitu.myapplication.R;
import com.example.vladitu.myapplication.fragments.ContactsFragment;
import com.example.vladitu.myapplication.models.NabContactModel;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Created by vlad.itu on 19-Feb-15.
 */
public class NabContactsArrayAdapter extends CursorAdapter{

    private Context context;
    private Cursor cursor;
    private LayoutInflater mInflater;
    private String mSearchString;

    public NabContactsArrayAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        this.cursor = c;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String contactDisplayName = cursor.getString(cursor.getColumnIndex(ContactsFragment.PROJECTION[2]));
        CharSequence highlightedDisplayName = highlightString(mSearchString, contactDisplayName);

        TextView tvContactName = (TextView) view.findViewById(R.id.tvContactName);
        tvContactName.setText(highlightedDisplayName);

        TextView tvPhoneNumber = (TextView) view.findViewById(R.id.tvContactPhoneNumber);
        tvPhoneNumber.setText(cursor.getString(cursor.getColumnIndex(ContactsFragment.PROJECTION[3])));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.contacts_list_item, parent, false);
    }

    public CharSequence highlightString(String search, String originalText) {
        String lowercaseText = originalText.toLowerCase();
        int start = lowercaseText.indexOf(search);
        if (start < 0) {
            return originalText;
        } else {
            Spannable highlighted = new SpannableString(originalText);
            highlighted.setSpan(new ForegroundColorSpan(Color.argb(255, 0, 0, 0)), start, start + mSearchString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlighted.setSpan(new StyleSpan(Typeface.BOLD), start, start + mSearchString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return highlighted;
        }
    }

    public String getSearchString() {
        return mSearchString;
    }

    public void setSearchString(String searchString) {
        this.mSearchString = searchString;
    }
}
