package com.ada.nytimes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.ada.nytimes.R;
import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.utils.Constants;

import org.parceler.Parcels;


public class SearchAdvancedFragment extends DialogFragment {

    private ArticleSearchParam mItem;
    private SearchAdvancedDialogListener mListener;
    
    private DatePicker dpDue;

    public SearchAdvancedFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface SearchAdvancedDialogListener {
        void onDataChanged();
    }

    public static SearchAdvancedFragment newInstance(SearchAdvancedDialogListener listener, ArticleSearchParam params) {
        SearchAdvancedFragment frag = new SearchAdvancedFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_ITEM, Parcels.wrap(params));
        frag.setArguments(args);
        frag.setListener(listener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_advanced, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("TODO");

        mItem = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));
        /*
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvDue = (TextView) view.findViewById(R.id.tvDue);
        tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        */

        refreshUiFromItem();
/*
        Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
        View.OnClickListener onEdit = getOnEditListener();
        btnEdit.setOnClickListener(onEdit);

        Button btnCancel = (Button) view.findViewById(R.id.btnDelete);
        View.OnClickListener onCancel = getOnDeleteListener();
        btnCancel.setOnClickListener(onCancel);

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        View.OnClickListener onBack = getOnBackListener();
        btnBack.setOnClickListener(onBack);
        */
    }

    private void refreshUiFromItem() {
        /*
        tvName.setText(mItem.getName());
        tvDue.setText(Utils.formatDay(mItem.getDueDate()));
        tvNotes.setText(mItem.getNotes());
        tvPriority.setText(mPriorities[mItem.getPriority()]);
        tvStatus.setText(mItem.getStatus());
        */
    }
/*
    private View.OnClickListener getOnEditListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(mItem, getString(R.string.title_item_edit));
            }
        };
    }

    private View.OnClickListener getOnDeleteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO alert
                deleteItem(mItem);
                mListener.onDataChanged();
                dismiss();
            }
        };
    }

    private View.OnClickListener getOnBackListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }
    */
    public void setListener(SearchAdvancedDialogListener listener) {
    mListener = listener;
}
}
