package com.launcher;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.launcher.utils.AppUtils;

import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView recyclelistview;
    private AppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclelistview = findViewById(R.id.recyclelistview);
        initData();
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclelistview.setLayoutManager(gridLayoutManager);
        List<PackageInfo> allApps = AppUtils.getAllApps();
        if(allApps!=null){
            appAdapter = new AppAdapter(this.getApplicationContext(),allApps);
            recyclelistview.setAdapter(appAdapter);
        }

    }


}
