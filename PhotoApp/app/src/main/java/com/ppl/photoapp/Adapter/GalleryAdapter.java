package com.ppl.photoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ppl.photoapp.Fragment.GalleryFragment;
import com.ppl.photoapp.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyHolder>
{
    Context context ;
    ArrayList<String> arrPath ;
    GalleryFragment galleryFragment ;

    public GalleryAdapter(Context context, ArrayList<String> arrPath, GalleryFragment galleryFragment) {
        this.context = context;
        this.arrPath = arrPath;
        this.galleryFragment = galleryFragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String path = arrPath.get(i) ;
        File imgFile = new File(path);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myHolder.ivImage.setImageBitmap(myBitmap);

        myHolder.rlImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowDialogDelete(path) ;
                return false;
            }
        });
    }

    void ShowDialogDelete(final String path) {
        final Dialog dialog = new Dialog(context) ;
        dialog.setContentView(R.layout.dialog_delete_image) ;
        dialog.setCancelable(true) ;

        Button btnAccept = dialog.findViewById(R.id.btnAccept) ;
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File deletedFile = new File(path);
                deletedFile.delete() ;
                galleryFragment.NumberChanged();
                dialog.dismiss();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btnCancel) ;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)) ;
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return arrPath.size();
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
