package com.ppl.photoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ppl.photoapp.Model.LabeledBitmapArray;
import com.ppl.photoapp.R;
import com.ppl.photoapp.SplittingActivity;

import java.util.ArrayList;

public class SplittingVerticalAdapter extends RecyclerView.Adapter<SplittingVerticalAdapter.MyHolder> {
    Context context ;
    ArrayList<LabeledBitmapArray> arrLabeledBitmap;
    SplittingActivity splittingActivity;

    public SplittingVerticalAdapter(Context context, ArrayList<LabeledBitmapArray> arrLabeledBitmap, SplittingActivity splittingActivity) {
        this.context = context;
        this.arrLabeledBitmap = arrLabeledBitmap;
        this.splittingActivity = splittingActivity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_spliting,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {

        final LabeledBitmapArray labeledBitmapArray = arrLabeledBitmap.get(i) ;
        Bitmap[] bitmaps = labeledBitmapArray.getBitmap() ;
        myHolder.tvLabel.setText(labeledBitmapArray.getLabel()+"");

        SplittingHorizontalAdapter splittingHorizontalAdapter = new SplittingHorizontalAdapter(context,bitmaps, splittingActivity,i) ;
        splittingHorizontalAdapter.notifyDataSetChanged() ;
        myHolder.recyclerViewHorizontal.setAdapter(splittingHorizontalAdapter);

        //Delete
        myHolder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogDelete(i);
            }
        });

        //Edit
        myHolder.lnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogEdit(i) ;
            }
        });
    }

    void ShowDialogDelete(final int position){
        final Dialog dialog = new Dialog(context) ;
        dialog.setContentView(R.layout.dialog_delete_row) ;
        dialog.setCancelable(true) ;

        Button btnAccept = dialog.findViewById(R.id.btnAccept) ;
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteRow(position) ;
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

    void DeleteRow(final int position){
        splittingActivity.DeleteRow(position);
    }

    void ShowDialogEdit(final int position){
        final Dialog dialog = new Dialog(context) ;
        dialog.setContentView(R.layout.dialog_put_number) ;
        dialog.setCancelable(true) ;

        LinearLayout btnZero = dialog.findViewById(R.id.btnZero) ;
        LinearLayout btnOne = dialog.findViewById(R.id.btnOne) ;
        LinearLayout btnTwo = dialog.findViewById(R.id.btnTwo) ;
        LinearLayout btnThree = dialog.findViewById(R.id.btnThree) ;
        LinearLayout btnFour = dialog.findViewById(R.id.btnFour) ;
        LinearLayout btnFive = dialog.findViewById(R.id.btnFive) ;
        LinearLayout btnSix = dialog.findViewById(R.id.btnSix) ;
        LinearLayout btnSeven = dialog.findViewById(R.id.btnSeven) ;
        LinearLayout btnEight = dialog.findViewById(R.id.btnEight) ;
        LinearLayout btnNine = dialog.findViewById(R.id.btnNine) ;

        btnOne.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,1); dialog.dismiss();}});
        btnTwo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,2);  dialog.dismiss();}});
        btnThree.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,3);  dialog.dismiss();}});
        btnFour.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,4);  dialog.dismiss();}});
        btnFive.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,5);  dialog.dismiss();}});
        btnSix.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,6);  dialog.dismiss();}});
        btnSeven.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,7);  dialog.dismiss();}});
        btnEight.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,8); dialog.dismiss(); }});
        btnNine.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,9);  dialog.dismiss();}});
        btnZero.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ChangeLabel(position,0); dialog.dismiss(); }});

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)) ;
        dialog.show();
    }

    void ChangeLabel(int position,int label){
        arrLabeledBitmap.get(position).setLabel(label) ;
        splittingActivity.UpdateSplittingView();
    }

    @Override
    public int getItemCount() {
        return arrLabeledBitmap.size() ;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public LinearLayout lnDelete , lnEdit ;
        public TextView tvLabel ;
        public RecyclerView recyclerViewHorizontal ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            lnDelete = itemView.findViewById(R.id.lnDelete) ;
            lnEdit = itemView.findViewById(R.id.lnEdit) ;
            tvLabel = itemView.findViewById(R.id.tvLabel) ;
            recyclerViewHorizontal = itemView.findViewById(R.id.recyclerViewSplittingHorizontal) ;

            //Set RecyclerView Horizontal
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewHorizontal.setNestedScrollingEnabled(false) ;
        }
    }
}
