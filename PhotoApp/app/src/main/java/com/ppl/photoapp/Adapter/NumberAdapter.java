package com.ppl.photoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ppl.photoapp.Fragment.GalleryFragment;
import com.ppl.photoapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.MyHolder>
{
    Context context ;
    int[] number ;
    public int checkedNumber ;
    GalleryFragment galleryFragment ;

    public NumberAdapter(Context context, int[] number, GalleryFragment galleryFragment,int checkedNumber) {
        this.context = context;
        this.number = number;
        this.galleryFragment = galleryFragment;
        this.checkedNumber = checkedNumber ;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_number,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder,final int i) {
        switch (number[i]){
            case 0 :  myHolder.btnNumber.setText(R.string.numZero); break;
            case 1 :  myHolder.btnNumber.setText(R.string.numOne); break;
            case 2 :  myHolder.btnNumber.setText(R.string.numTwo); break;
            case 3 :  myHolder.btnNumber.setText(R.string.numThree); break;
            case 4 :  myHolder.btnNumber.setText(R.string.numFour); break;
            case 5 :  myHolder.btnNumber.setText(R.string.numFive); break;
            case 6 :  myHolder.btnNumber.setText(R.string.numSix); break;
            case 7 :  myHolder.btnNumber.setText(R.string.numSeven); break;
            case 8 :  myHolder.btnNumber.setText(R.string.numEight); break;
            case 9 :  myHolder.btnNumber.setText(R.string.numNine); break;
            case -1 :  myHolder.btnNumber.setText(R.string.numAll); break;
        }

        if (number[i] == checkedNumber){
            myHolder.btnNumber.setBackgroundResource(R.drawable.button_clicked) ;
        }else {
            myHolder.btnNumber.setBackgroundResource(R.drawable.button_controller) ;
        }

        myHolder.btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedNumber = number[i] ;
                galleryFragment.UpdateRecylerViewNumber();
                galleryFragment.NumberChanged();
                galleryFragment.SetCheckedNumber() ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return number.length;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public Button btnNumber ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            btnNumber = itemView.findViewById(R.id.btnNumber) ;
        }
    }

}
