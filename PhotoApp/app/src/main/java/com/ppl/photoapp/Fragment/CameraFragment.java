package com.ppl.photoapp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ppl.photoapp.R;
import com.ppl.photoapp.ResultActivity;
import com.ppl.photoapp.SettingBeforeActivity;

import java.io.File;

public class CameraFragment extends Fragment {


    public CameraFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        GotoSettingBeforeActivity(view) ;
        TakePicture(view) ;
        return view ;
    }

    void TakePicture(View view){
        Button btnTakePicture = view.findViewById(R.id.btnTakePicture) ;
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResultActivity.class) ;
                startActivity(intent);
            }
        });
    }

    void GotoSettingBeforeActivity(View view){
        ImageView ivSetting = view.findViewById(R.id.ivSetting) ;
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingBeforeActivity.class) ;
                startActivity(intent);
            }
        });
    }

}
