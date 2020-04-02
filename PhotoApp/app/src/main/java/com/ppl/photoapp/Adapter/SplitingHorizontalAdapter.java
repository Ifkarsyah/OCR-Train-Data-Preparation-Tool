package com.ppl.photoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.ppl.photoapp.R;
import com.ppl.photoapp.SplitingActivity;

public class SplitingHorizontalAdapter extends RecyclerView.Adapter<SplitingHorizontalAdapter.MyHolder>
{
    Context context ;
    Bitmap[] bitmaps ;
    SplitingActivity splitingActivity ;
    int numberOfVertical ;

    public SplitingHorizontalAdapter(Context context, Bitmap[] bitmaps, SplitingActivity splitingActivity, int numberOfVertical) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.splitingActivity = splitingActivity;
        this.numberOfVertical = numberOfVertical;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_column_spliting,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        final Bitmap bitmap = bitmaps[i] ;
        myHolder.ivImage.setImageBitmap(bitmap);

        myHolder.rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, LookSplitImageActivity.class) ;
//                Global.splitedBitmap = bitmap ;
//                context.startActivity(intent) ;
                ShowDialogDelete(numberOfVertical,i) ;
            }
        });


    }

    void ShowDialogDelete(final int positionVertical,final int positionHorizontal){
        final Dialog dialog = new Dialog(context) ;
        dialog.setContentView(R.layout.dialog_delete_image) ;
        dialog.setCancelable(true) ;

        Button btnAccept = dialog.findViewById(R.id.btnAccept) ;
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSingleItem(positionVertical,positionHorizontal) ;
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

    void DeleteSingleItem(final int positionVertical,final int positionHorizontal){
        splitingActivity.DeleteSingleItem(positionVertical,positionHorizontal);
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
