package com.ppl.photoapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ppl.photoapp.R;
import com.ppl.photoapp.SettingActivity;

public class CameraFragment extends Fragment {


    public CameraFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        GotoSetttingActivity(view) ;

        return view ;
    }

    void GotoSetttingActivity(View view){
        ImageView ivSetting = view.findViewById(R.id.ivSetting) ;
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class) ;
                startActivity(intent);
            }
        });

    }

}
