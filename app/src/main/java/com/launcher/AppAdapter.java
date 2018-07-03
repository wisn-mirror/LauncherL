package com.launcher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.launcher.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wisn on 2018/7/3 上午11:16.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder1> {
    private  Context context;
    private List<PackageInfo> data = new ArrayList<>();

    public AppAdapter(Context context, List<PackageInfo> data) {
        this.data = data;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              return   new MyViewHolder1(View.inflate(context, R.layout.item_app_imageview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        final PackageInfo packageInfo = data.get(position);
        if(packageInfo!=null){
            holder.appname.setText(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            Drawable d=context.getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
            holder.appicon.setImageDrawable(d);
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,""+packageInfo.packageName,Toast.LENGTH_SHORT).show();
                    AppUtils.openApp(packageInfo.packageName);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView appicon;
        public TextView appname;
        public LinearLayout content;

        public MyViewHolder1(View itemView) {
            super(itemView);
            appicon = itemView.findViewById(R.id.appicon);
            appname = itemView.findViewById(R.id.appname);
            content = itemView.findViewById(R.id.content);
        }
    }
}
