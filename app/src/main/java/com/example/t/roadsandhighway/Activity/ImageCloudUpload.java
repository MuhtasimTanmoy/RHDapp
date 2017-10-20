package com.example.t.roadsandhighway.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.t.roadsandhighway.R;

public class ImageCloudUpload extends AppCompatActivity {

    private Button btnUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cloud_upload);


        btnUp=(Button) findViewById(R.id.btnImgCloudUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("image","demo");
                Toast.makeText(getApplicationContext(),"hm",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
