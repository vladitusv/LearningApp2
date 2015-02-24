package com.example.vladitu.myapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private int mItemCounter;

    public ContactsRecyclerAdapter(Context context, Cursor cursor) {
        super(cursor);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mItemCounter = 0;
    }

    @Override
    protected AbstractViewHolder createClickableViewHolder(ViewGroup viewGroup, int viewType) {
        mItemCounter++;
        View v = mInflater.inflate(R.layout.contacts_list_item, viewGroup, false);
        final ContactsViewHolder contactsViewHolder = new ContactsViewHolder(v, mItemCounter);

        final GestureDetector gestureDetector = new GestureDetector(mContext, new MyGestureDetector());

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                boolean swipeToRight = gestureDetector.onTouchEvent(event);
                if(swipeToRight) {
                    notifyItemRemoved(contactsViewHolder.getItemPosition());
                }
                return swipeToRight;
            }
        };
        v.setOnTouchListener(gestureListener);
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

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    public void setSearchString(String searchString) {
        this.mSearchString = searchString;
    }

    public static class ContactsViewHolder extends AbstractViewHolder {
        public TextView contactName;
        public TextView contactNumber;
        public ImageView contactPicture;
        private int mPosition;

        public ContactsViewHolder(View itemView, int position) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.tvContactName);
            contactNumber = (TextView) itemView.findViewById(R.id.tvContactPhoneNumber);
            contactPicture = (ImageView) itemView.findViewById(R.id.ivContactPicture);
            mPosition = position;
        }

        public int getItemPosition() {
            return mPosition;
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private static final float DEFAULT_X_DISPLACEMENT = 1000;//HACK (if e1 == null): high value to detect right to left swipes by default; if(e1 != null) no need to worry
        private static final float DEFAULT_Y_DISPLACEMENT = 0;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float x1 = DEFAULT_X_DISPLACEMENT;
                float y1 = DEFAULT_Y_DISPLACEMENT;
                if(e1 != null){
                    x1 = e1.getX();
                    y1 = e1.getY();
                }

                float diffY = e2.getY() - y1;
                float diffX = e2.getX() - x1;
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            //swipe right
                            return false;
                        } else {
                            //swipe left
                            return true;
                        }
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        //swipe bottom
                        return false;
                    } else {
                        //swipe top
                        return false;
                    }
                }
                return false;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    }
}
