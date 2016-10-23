package com.ada.nytimes.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.ada.nytimes.R;
import com.ada.nytimes.databinding.FragmentSearchAdvancedBinding;
import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.utils.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class SearchAdvancedFragment extends DialogFragment {

    private ArticleSearchParam mParams;
    private SearchAdvancedDialogListener mListener;
    private FragmentSearchAdvancedBinding binding;

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
        binding.cbNewsDeskArts.setChecked(mParams.getNewDeskList().contains("Arts"));
        binding.cbNewsDeskSports.setChecked(mParams.getNewDeskList().contains("Sports"));
        binding.cbNewsDeskFashion.setChecked(mParams.getNewDeskList().contains("Fashion & Style"));
        dateToDatePicker(mParams.getBeginDate());
        binding.scOrder.setChecked(ArticleSearchParam.SortValues.oldest.name().equals(mParams.getSort()));

        Button btnDone = (Button) view.findViewById(R.id.btnDone);
        View.OnClickListener onEdit = getOnDoneListener();
        btnDone.setOnClickListener(onEdit);

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        View.OnClickListener onBack = getOnBackListener();
        btnBack.setOnClickListener(onBack);

    }

    private void dateToDatePicker(String date) {
        //date format YYYYmmDD
        if (TextUtils.isEmpty(date) || date.length() != 8) {
            binding.dpDate.updateDate(1000, 1, 1);
        } else {
            //yes I know it is not the best practice
            binding.dpDate.updateDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(4, 6)), Integer.valueOf(date.substring(6, 8)));
        }
    }

    private String datePickerToString(DatePicker dp) {
        return String.format("%04d%02d%02d", dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
    }

    private View.OnClickListener getOnDoneListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParams.setBeginDate(datePickerToString(binding.dpDate));
                mParams.setQ(binding.etQ.getText().toString());
                //   mParams.setNewsDesk(binding.etQ.getText().toString());
                mParams.setSort((binding.scOrder.isChecked() ? ArticleSearchParam.SortValues.oldest : ArticleSearchParam.SortValues.newest));
                mParams.setNewsDesk(newsDeckToList());
                mParams.resetPage();
                mListener.onDataChanged(Parcels.wrap(mParams));
                dismiss();
            }
        };
    }

    private List<String> newsDeckToList() {
        List<String> result = new ArrayList<>();
        if (binding.cbNewsDeskArts.isChecked()) {
            result.add("Arts");
        }
        if (binding.cbNewsDeskFashion.isChecked()) {
            result.add("Fashion & Style");
        }
        if (binding.cbNewsDeskSports.isChecked()) {
            result.add("Sports");
        }
        return result;
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
