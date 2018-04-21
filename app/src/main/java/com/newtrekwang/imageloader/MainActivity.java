package com.newtrekwang.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.newtrekwang.library.NewtrekLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        iv = findViewById(R.id.main_iv);
     findViewById(R.id.main_btn_loadUrl).setOnClickListener(this);
     findViewById(R.id.main_btn_loadRes).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn_loadUrl:
                NewtrekLoader.getInstance()
                        .displayImage("http://upload.techweb.com.cn/s/640/2018/0420/1524192529223.jpeg",iv);
                break;
            case R.id.main_btn_loadRes:
                NewtrekLoader.getInstance()
                        .displayImage(getApplication(),R.mipmap.ic_launcher,iv);
                break;
                default:
                    break;
        }
    }
}
