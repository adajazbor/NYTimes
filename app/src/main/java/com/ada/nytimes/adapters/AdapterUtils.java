package com.ada.nytimes.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.ada.nytimes.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by ada on 10/22/16.
 */
public class AdapterUtils {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        //TODO placeholder/error
        int fullWidth = Utils.getDisplayWidth(view.getContext());
        double percent = 1.0 / getColNm(view.getContext()) - 0.01;
        Log.d("CALCULATE WIDHT", "fullWidth=" + fullWidth + ", cols=" + getColNm(view.getContext())+ ", result=" + fullWidth * percent);
        Log.d("CALCULATE WIDHT", "url=" + url);
        Picasso.with(view.getContext()).load(url).resize((int) Math.floor(fullWidth * percent), 0).into(view);
    }

    public static int getColNm(Context context) {
        return (Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation ? 4 : 2);
    }
}
