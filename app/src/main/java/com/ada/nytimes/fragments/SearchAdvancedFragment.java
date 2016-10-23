package com.ada.nytimes.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ada.nytimes.R;
import com.ada.nytimes.databinding.FragmentSearchAdvancedBinding;
import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.utils.Constants;

import org.parceler.Parcels;


public class SearchAdvancedFragment extends DialogFragment {

    private ArticleSearchParam mParams;
    private SearchAdvancedDialogListener mListener;
    private FragmentSearchAdvancedBinding binding;

    private DatePicker dpDue;
    private EditText etDate;
    private EditText etQ;

    public SearchAdvancedFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface SearchAdvancedDialogListener {
        void onDataChanged(Parcelable params);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_advanced, container, false);
        //View view = binding.getRoot();
        //binding = DataBindingUtil.setContentView(this, R.layout.fragment_search_advanced);

        mParams = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));
        binding.setSParams(mParams);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //etDate = (EditText) view.findViewById(R.id.etDate);
        //etQ = (EditText) view.findViewById(R.id.etQ);
        /*
        tvDue = (TextView) view.findViewById(R.id.tvDue);
        tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        */

        //refreshUiFromItem();

        Button btnDone = (Button) view.findViewById(R.id.btnDone);
        View.OnClickListener onEdit = getOnDoneListener();
        btnDone.setOnClickListener(onEdit);

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        View.OnClickListener onBack = getOnBackListener();
        btnBack.setOnClickListener(onBack);
/*
        Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
        View.OnClickListener onEdit = getOnEditListener();
        btnEdit.setOnClickListener(onEdit);

        Button btnCancel = (Button) view.findViewById(R.id.btnDelete);
        View.OnClickListener onCancel = getOnDeleteListener();
        btnCancel.setOnClickListener(onCancel);


        */
    }

    private void refreshUiFromItem() {
        //etDate.setText(mParams.getBeginDate());
        //etDate.setText(mParams.getQ());
        /*
        tvName.setText(mItem.getName());
        tvDue.setText(Utils.formatDay(mItem.getDueDate()));
        tvNotes.setText(mItem.getNotes());
        tvPriority.setText(mPriorities[mItem.getPriority()]);
        tvStatus.setText(mItem.getStatus());
        */
    }

    private View.OnClickListener getOnDoneListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mParams.setBeginDate(etDate.getText().toString());
               // mParams.setQ(etQ.getText().toString());
                mParams.setQ(binding.etQ.getText().toString());
                mParams.setBeginDate(binding.etDate.getText().toString());
                mParams.resetPage();
             //   mParams.setNewsDesk(binding.etQ.getText().toString());
               // mParams.setSort(binding.etSort.getText().toString());
                mListener.onDataChanged(Parcels.wrap(mParams));
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
/*
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

    */
    public void setListener(SearchAdvancedDialogListener listener) {
    mListener = listener;
}
}
