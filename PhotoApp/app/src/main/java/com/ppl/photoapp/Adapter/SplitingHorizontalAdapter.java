package com.ppl.photoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.LookImageActivity;
import com.ppl.photoapp.LookSplitImageActivity;
import com.ppl.photoapp.Model.LabeledBitmapArray;
import com.ppl.photoapp.R;
import com.ppl.photoapp.SplitingActivity;

import java.util.ArrayList;

public class SplitingHorizontalAdapter extends RecyclerView.Adapter<SplitingHorizontalAdapter.MyHolder>
{
    Context context ;
    Bitmap[] bitmaps ;
    SplitingActivity splitingActivity ;

    public SplitingHorizontalAdapter(Context context, Bitmap[] bitmaps, SplitingActivity splitingActivity) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.splitingActivity = splitingActivity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_column_spliting,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final Bitmap bitmap = bitmaps[i] ;
        myHolder.ivImage.setImageBitmap(bitmap);

        myHolder.rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LookSplitImageActivity.class) ;
                Global.splitedBitmap = bitmap ;
                context.startActivity(intent) ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitmaps.length ;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage ;
        public RelativeLayout rlImage ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage) ;
            rlImage = itemView.findViewById(R.id.rlImage) ;
        }
    }
}
