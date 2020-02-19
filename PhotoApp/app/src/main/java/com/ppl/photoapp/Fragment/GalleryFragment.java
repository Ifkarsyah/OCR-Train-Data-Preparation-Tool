package com.ppl.photoapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ppl.photoapp.Adapter.NumberAdapter;
import com.ppl.photoapp.R;

public class GalleryFragment extends Fragment {

    public GalleryFragment() {

    }

    RecyclerView recyclerViewNumber ;
    NumberAdapter numberAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        SetFilterNumberView(view) ;

        return view ;
    }

    void SetFilterNumberView(View view){
        recyclerViewNumber = view.findViewById(R.id.recyclerViewNumber) ;
        recyclerViewNumber.setHasFixedSize(true);
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        numberAdapter = new NumberAdapter(getContext(),new int[]{-1,0,1,2,3,4,5,6,7,8,9},this) ;
        numberAdapter.notifyDataSetChanged();
        recyclerViewNumber.setAdapter(numberAdapter);
    }

}
