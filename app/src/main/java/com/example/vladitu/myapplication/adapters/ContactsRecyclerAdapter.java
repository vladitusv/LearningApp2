package com.example.vladitu.myapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladitu.myapplication.R;
import com.example.vladitu.myapplication.fragments.ContactsFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by vlad.itu on 19-Feb-15.
 */
public class ContactsRecyclerAdapter extends CursorItemClickRecyclerAdapter {

    private String mSearchString;
    private LayoutInflater mInflater;
    private Context mContext;

    public ContactsRecyclerAdapter(Context context, Cursor cursor) {
        super(cursor);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    protected AbstractViewHolder createClickableViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mInflater.inflate(R.layout.contacts_list_item, viewGroup, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(v);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolderCursor(AbstractViewHolder holder, Cursor cursor, int position) {
        if(cursor == null)
            return;

        cursor.moveToPosition(position);

        String contactDisplayName = cursor.getString(cursor.getColumnIndex(ContactsFragment.PROJECTION[2]));
        CharSequence highlightedDisplayName = highlightString(mSearchString, contactDisplayName);

        TextView tvContactName = (TextView) holder.itemView.findViewById(R.id.tvContactName);
        tvContactName.setText(highlightedDisplayName);

        TextView tvPhoneNumber = (TextView) holder.itemView.findViewById(R.id.tvContactPhoneNumber);
        tvPhoneNumber.setText(cursor.getString(cursor.getColumnIndex(ContactsFragment.PROJECTION[3])));

        ImageView ivContactPicture = (ImageView) holder.itemView.findViewById(R.id.ivContactPicture);
        String contactPicture = cursor.getString(cursor.getColumnIndex(ContactsFragment.PROJECTION[4]));
        if(contactPicture != null) {
            Picasso.with(mContext).load(contactPicture).into(ivContactPicture);
        }else{
            Picasso.with(mContext).load(R.drawable.ic_action_user).into(ivContactPicture);
        }

        cursor.moveToPrevious();
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

    public void setSearchString(String searchString) {
        this.mSearchString = searchString;
    }

    public static class ContactsViewHolder extends AbstractViewHolder {
        public TextView contactName;
        public TextView contactNumber;
        public ImageView contactPicture;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.tvContactName);
            contactNumber = (TextView) itemView.findViewById(R.id.tvContactPhoneNumber);
            contactPicture = (ImageView) itemView.findViewById(R.id.ivContactPicture);
        }
    }
}
