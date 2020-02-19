package com.ppl.photoapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ppl.photoapp.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyHolder>
{
    Context context ;
    ArrayList<String> arrPath ;

    public GalleryAdapter(Context context, ArrayList<String> arrPath) {
        this.context = context;
        this.arrPath = arrPath;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String path = arrPath.get(i) ;
        File imgFile = new File(path);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myHolder.ivImage.setImageBitmap(myBitmap);
    }

    @Override
    public int getItemCount() {
        return arrPath.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage) ;
        }
    }
}
