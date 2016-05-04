package com.example.leeeyou.pageturning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PageTurnView pageTurnView = (PageTurnView) findViewById(R.id.pageTurnView);

        List<Bitmap> bitmaps = new ArrayList<>(3);
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu01));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu02));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu03));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu04));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu05));

        pageTurnView.setBitmaps(bitmaps);
    }

}
