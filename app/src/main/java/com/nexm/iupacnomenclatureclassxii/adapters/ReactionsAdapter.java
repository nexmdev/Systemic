package com.nexm.iupacnomenclatureclassxii.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexm.iupacnomenclatureclassxii.R;

public class ReactionsAdapter extends RecyclerView.Adapter<ReactionsAdapter.ViewHolder> {
    private final String[] names;
    private final String[] colors = {"#2D00A1","#9A0151","#008C09","#EA7410","#666181","#9F00C0","#BE7457"};
    private Context context;
    private static OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String unitName,String topicName);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        ReactionsAdapter.listener = listener;
    }

    public ReactionsAdapter(String[] data){names=data;}
    @Override
    public ReactionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reactions_main_recycler_item,parent,false);

        return new ReactionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReactionsAdapter.ViewHolder holder, int position) {
        holder.title.setText(names[position]);

            Drawable[] i = holder.preparation.getCompoundDrawables();
            Drawable wrappedDrawable = DrawableCompat.wrap(i[1]);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(colors[position]));
            Drawable[] j = holder.reactions.getCompoundDrawables();
            Drawable wrappedDrawable1 = DrawableCompat.wrap(j[1]);
            DrawableCompat.setTint(wrappedDrawable1, Color.parseColor(colors[position]));
            Drawable[] k = holder.mechanisms.getCompoundDrawables();
            Drawable wrappedDrawable2 = DrawableCompat.wrap(k[1]);
            DrawableCompat.setTint(wrappedDrawable2, Color.parseColor(colors[position]));


        holder.preparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"preparation",Toast.LENGTH_SHORT).show();
                int i = holder.getAdapterPosition();
                if(listener != null){
                    listener.onItemClick(names[i],"Preparation");
                }
            }
        });
        holder.reactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"reactions",Toast.LENGTH_SHORT).show();
                int i = holder.getAdapterPosition();
                if(listener != null){
                    listener.onItemClick(names[i],"Reactions");
                }
            }
        });
        holder.mechanisms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"mechanisms",Toast.LENGTH_SHORT).show();
                int i = holder.getAdapterPosition();
                if(listener != null){
                    listener.onItemClick(names[i],"Mechanism");
                }
            }
        });
        if(position == 0 || position == 1 || position == 2||position==3||position==4||position==5){
            holder.preparation.setBackgroundResource(android.R.color.white);
            holder.reactions.setBackgroundResource(android.R.color.white);
            holder.mechanisms.setBackgroundResource(android.R.color.white);
        }else{
            holder.preparation.setBackgroundResource(R.color.darkbackground);
            holder.reactions.setBackgroundResource(R.color.darkbackground);
            holder.mechanisms.setBackgroundResource(R.color.darkbackground);
        }
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,reactions,preparation,mechanisms;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reactions_main_recycler_title);
            preparation = itemView.findViewById(R.id.reactions_main_recycler_preparaton);
            reactions = itemView.findViewById(R.id.reactions_main_recycler_reactions);
            mechanisms = itemView.findViewById(R.id.reactions_main_recycler_mechanisms);
            preparation.setCompoundDrawablesWithIntrinsicBounds(null,AppCompatResources.getDrawable(context,R.drawable.ic_action_process_start),null,null);
            reactions.setCompoundDrawablesWithIntrinsicBounds(null,AppCompatResources.getDrawable(context,R.drawable.ic_action_process_end),null,null);
            mechanisms.setCompoundDrawablesWithIntrinsicBounds(null,AppCompatResources.getDrawable(context,R.drawable.ic_action_process_save),null,null);
        }
    }
}
