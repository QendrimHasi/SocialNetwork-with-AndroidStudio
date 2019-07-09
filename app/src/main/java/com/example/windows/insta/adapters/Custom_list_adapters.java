package com.example.windows.insta.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.windows.insta.R;
import com.example.windows.insta.model.ImageUrl;
import com.example.windows.insta.model.PostOfFollowers;

import java.util.ArrayList;
import java.util.List;

public class Custom_list_adapters extends RecyclerView.Adapter<Custom_list_adapters.ViewHolderlist> {
    private ArrayList<ImageUrl> imageUrls;
    private Context context;

    public Custom_list_adapters(Context context, ArrayList<ImageUrl> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;

    }

    @Override
    public Custom_list_adapters.ViewHolderlist onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_items, viewGroup, false);
        return new Custom_list_adapters.ViewHolderlist(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(Custom_list_adapters.ViewHolderlist viewHolder, int i) {
        viewHolder.txt.setText(imageUrls.get(i).getDescription());
        // Glide.with(context).load(imageUrls.get(i).getDescription()).into(viewHolder.txt);
        Glide.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);


    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolderlist extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt;

        public ViewHolderlist(View view) {
            super(view);
            img = view.findViewById(R.id.imageView_home);
            txt = view.findViewById(R.id.photos_title_home);


        }
    }
}

