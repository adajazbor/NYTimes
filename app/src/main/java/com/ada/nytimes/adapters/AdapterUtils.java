package com.ada.nytimes.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.ada.nytimes.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by ada on 10/22/16.
 */
public class AdapterUtils {

    private static final int MIN_COL_WIDTH = 320;

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        //TODO placeholder/error
        int fullWidth = Utils.getDisplayWidth(view.getContext());
        double percent = 1.0 / getColNm(view.getContext()) - 0.01;
        Picasso.with(view.getContext()).load(url).resize((int) Math.floor(fullWidth * percent), 0).into(view);
    }

    public static int getColNm(Context context) {
        int fullWidth = Utils.getDisplayWidth(context);
        int cols = fullWidth / MIN_COL_WIDTH;
        //return (Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation ? 4 : 2);
        return cols;
    }
}
