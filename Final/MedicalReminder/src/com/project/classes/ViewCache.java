package com.project.classes;

import com.project.medicalreminder.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ahmedrashad on 3/10/15.
 */
public class ViewCache {
    private View baseView;
    private ImageView imageView;
    private TextView headerTextView;
    private TextView descriptionTextView;

    public ViewCache(View baseView){
        this.baseView = baseView;
    }

    public View getBaseView() {
        return baseView;
    }
    public ImageView getImageView() {
        if(imageView == null){
            imageView = (ImageView) baseView.findViewById(R.id.imageView);
        }
        return imageView;
    }
    public TextView getHeaderTextView() {
        if(headerTextView == null){
            headerTextView = (TextView) baseView.findViewById(R.id.headerTextView);
        }
        return headerTextView;
    }
    
    public TextView getDescriptionTextView() {
        if(descriptionTextView == null){
            descriptionTextView = (TextView) baseView.findViewById(R.id.descriptionTextView);
        }
        return descriptionTextView;
    }

}
