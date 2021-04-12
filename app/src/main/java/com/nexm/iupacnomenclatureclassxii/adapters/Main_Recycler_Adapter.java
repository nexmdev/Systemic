package com.nexm.iupacnomenclatureclassxii.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.iupacnomenclatureclassxii.MenuActivity;
import com.nexm.iupacnomenclatureclassxii.R;

/**
 * Created by user on 22-09-2018.
 */

public class Main_Recycler_Adapter extends RecyclerView.Adapter<Main_Recycler_Adapter.Main_ViewHolder> {
    private final String[] names;
    private Context context;


    public Main_Recycler_Adapter(String[] data){
        names = data;
    }
    @Override
    public Main_Recycler_Adapter.Main_ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item,parent,false);

        return new Main_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Main_Recycler_Adapter.Main_ViewHolder holder, final int position) {

        holder.name.setText(names[position]);
        ViewCompat.setBackground(holder.linearLayout, AppCompatResources.getDrawable(context,R.drawable.alc_back));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   Intent intent = new Intent(context,MenuActivity.class);
                   intent.putExtra("TOPIC",names[position]);
                   context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class Main_ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final LinearLayout linearLayout;
        public Main_ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.main_recycler_item_textView);
            linearLayout = itemView.findViewById(R.id.main_recycler_item_linearlayout);
        }
    }
}
