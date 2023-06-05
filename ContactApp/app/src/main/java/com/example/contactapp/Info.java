package com.example.contactapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Info extends AppCompatActivity {
    TextView txtname, txtnumber;
    ImageView imgcall, imgmess, imginfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_friend);
        txtname = findViewById(R.id.txtname);
        txtnumber = findViewById(R.id.txtnumber);
        imgcall = findViewById(R.id.imgcall);
        imgmess = findViewById(R.id.imgmess);
        imginfo = findViewById(R.id.imginfo);
        //Nhận intent
        Intent intent = getIntent();
        //Lấy Bundle khỏi intent
        Bundle mybundle = intent.getBundleExtra("mybundle");
        //Lấy dữ liệu khỏi bundle
        String a = mybundle.getString("name");
        String b = mybundle.getString("number");
        txtname.setText(a+"");
        txtnumber.setText(b+"");
        imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Khai báo intent ẩn
                //Trong intent, dữ liệu đều được đổi thành chuẩn uri
                Intent intentcall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+b));
                //Yêu cầu sự đồng ý của người dùng
                if (ActivityCompat.checkSelfPermission(Info.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Info.this, new String[]{Manifest.permission.CALL_PHONE}
                            ,1);
                    return;
                }
                startActivity(intentcall);
            }
        });
        imgmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Khai báo intent ẩn gọi đến ứng dụng tin nhắn
                Intent intentsms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+b));
                startActivity(intentsms);
            }
        });
    }//llllllll
}
