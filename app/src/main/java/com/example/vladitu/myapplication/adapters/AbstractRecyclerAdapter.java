package com.example.vladitu.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public abstract class AbstractRecyclerAdapter<VH extends AbstractRecyclerAdapter.AbstractViewHolder> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        VH holder = createClickableViewHolder(viewGroup, viewType);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    protected abstract VH createClickableViewHolder(ViewGroup viewGroup, int viewType);

    public static class AbstractViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private OnItemClickListener onItemClickListener;

        public AbstractViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        /**
         * Declare this method final to avoid overriding
         */
        @Override
        public final void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(v, getPosition());
            }
        }

    }
}