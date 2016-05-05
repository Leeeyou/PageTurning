package com.example.leeeyou.pageturning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leeeyou.pageturning.customeview.FoldView;
import com.example.leeeyou.pageturning.customeview.PageTurnView;

import java.util.ArrayList;
import java.util.List;

public class FoldViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold_view);

        FoldView foldView = (FoldView) findViewById(R.id.foldView);

        List<Bitmap> bitmaps = new ArrayList<>(3);
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu01));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu02));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu03));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu04));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jianzhu05));

        foldView.setBitmaps(bitmaps);
    }

}
