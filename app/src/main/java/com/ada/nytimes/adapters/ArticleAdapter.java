package com.ada.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ada.nytimes.R;
import com.ada.nytimes.databinding.ArticleItemBinding;
import com.ada.nytimes.databinding.ArticleWithPictureItemBinding;
import com.ada.nytimes.network.dto.articleSearch.Doc;

import java.util.List;

/**
 * Created by ada on 9/13/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_ONLY = 0, PICTURE = 1;
    private List<Doc> mItems;
    private ItemArrayAdapterDelegate mDelegate;
    private Context mContext;

    public interface ItemArrayAdapterDelegate {
        boolean onLongClick(int position);
        void onClick(int position);
    }

    public class ViewHolderPoster extends RecyclerView.ViewHolder {
        ArticleItemBinding binding;

        public ViewHolderPoster(View itemView) {
            super(itemView);

            binding = ArticleItemBinding.bind(itemView);

            itemView.setOnClickListener((v) -> mDelegate.onClick(getAdapterPosition()));
        }
    }


    public class ViewHolderVideo extends RecyclerView.ViewHolder {
        ArticleWithPictureItemBinding binding;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            binding = ArticleWithPictureItemBinding.bind(itemView);

            itemView.setOnClickListener((v) -> mDelegate.onClick(getAdapterPosition()));
        }
    }

    // Pass in the contact array into the constructor
    public ArticleAdapter(Context context, List<Doc> items, ItemArrayAdapterDelegate delegate) {
        mItems = items;
        mContext = context;
        mDelegate = delegate;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case PICTURE:
                viewHolder = new ViewHolderVideo(inflater.inflate(R.layout.article_with_picture_item, parent, false));
                break;
            default:
                viewHolder = new ViewHolderPoster(inflater.inflate(R.layout.article_item, parent, false));
                break;
        }
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case PICTURE:
                ViewHolderVideo vh1 = (ViewHolderVideo) viewHolder;
                configureViewHolderVideo(vh1, position);
                break;
            default:
                ViewHolderPoster vh2 = (ViewHolderPoster) viewHolder;
                configureViewHolderPoster(vh2, position);
                break;
        }
    }

    private void configureViewHolderPoster(ViewHolderPoster viewHolder, int position) {
        Doc item = mItems.get(position);
        viewHolder.binding.setArticle(item);
        viewHolder.binding.executePendingBindings();
    }

    private void configureViewHolderVideo(ViewHolderVideo viewHolder, int position) {
        Doc item = mItems.get(position);
        viewHolder.binding.setArticle(item);
        viewHolder.binding.executePendingBindings();
    }

    /*
    private int getVideoVisibility(Doc item) {
        return (item.getYouTubeTrailerKey() == null ? View.INVISIBLE : View.VISIBLE);
    }
    */


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Doc item = mItems.get(position);

        if (TextUtils.isEmpty(item.getPictureUrl())) {
            return TEXT_ONLY;
        }
        return PICTURE;
    }

    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Doc> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

}