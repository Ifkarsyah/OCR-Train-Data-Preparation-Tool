package com.ppl.photoapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ppl.photoapp.R;

import java.util.ArrayList;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.MyHolder>
{
    Context context ;
    ArrayList<Bitmap> arrayList ;

    public ViewAllAdapter(Context context, ArrayList<Bitmap> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_view_all,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.ivImage.setImageBitmap(arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage) ;
        }
    }
}
