package com.nexm.iupacnomenclatureclassxii.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.iupacnomenclatureclassxii.R;

public class ReactionsTopicsAdapter extends RecyclerView.Adapter<ReactionsTopicsAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private final String Unit;
    private final String Topic;
    private int i ;
    private static OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String unitTable, int explNo, String eTable, String qTable, String unit, String topicTitle);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        ReactionsTopicsAdapter.listener = listener;
    }


    public  ReactionsTopicsAdapter(Cursor mcursor,String unit,String topic){
        this.cursor = mcursor;
        this.Topic = topic;
        this.Unit = unit;
    }
    @Override
    public ReactionsTopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reactions_topics_recycler_item,parent,false);
        return new ReactionsTopicsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReactionsTopicsAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.title.setText(cursor.getString(cursor.getColumnIndex("title")));
        holder.sub.setText(cursor.getString(cursor.getColumnIndex("subtitle")));
        int q = cursor.getInt(cursor.getColumnIndex("noofq"));
        int a = cursor.getInt(cursor.getColumnIndex("noofa"));
        holder.q.setText("Questions :"+ q);
        holder.a.setText("Answers : "+a);
        String status = q ==a ? "Complete":"In complete";
        holder.c.setText("Status : "+status);
        if(q==a){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellow));
            holder.c.setCompoundDrawablesWithIntrinsicBounds(null, AppCompatResources.getDrawable(context,R.drawable.ic_done_black_24dp),null,null);
            holder.c.setTextColor(ContextCompat.getColor(context,R.color.right));
        }else{
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,android.R.color.white));
            holder.c.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            holder.c.setTextColor(ContextCompat.getColor(context,R.color.tab_default));
        }
        byte[] b = cursor.getBlob(cursor.getColumnIndex("image"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        holder.icon.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title,sub,q,a,c;
        private final ImageView icon;
        private final CardView cardView;
        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reaction_topics_title);
            sub = itemView.findViewById(R.id.reaction_topics_subtitle);
            q = itemView.findViewById(R.id.reactions_topics_questions);
            a = itemView.findViewById(R.id.reactions_topics_answers);
            c = itemView.findViewById(R.id.reactions_topics_correct);
            icon = itemView.findViewById(R.id.reaction_topics_icon);
            cardView = itemView.findViewById(R.id.reactions_topics_item_cardView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     i = getAdapterPosition();
                    cursor.moveToPosition(i);
                    if(listener!= null){
                        if(Unit.matches("Haloalkane")){
                            listener.onItemClick(Unit+Topic,
                                    cursor.getInt(cursor.getColumnIndex("number")),
                                    cursor.getString(cursor.getColumnIndex("etableID")),
                                    cursor.getString(cursor.getColumnIndex("qtableID")),Unit,
                                    cursor.getString(cursor.getColumnIndex("title")));
                        }else{
                            listener.onItemClick(Unit+Topic,
                                    cursor.getInt(cursor.getColumnIndex("number")),
                                    Integer.toString(cursor.getInt(cursor.getColumnIndex("e_start"))),
                                    Integer.toString(cursor.getInt(cursor.getColumnIndex("e_end"))),Unit,
                                    cursor.getString(cursor.getColumnIndex("title")));
                        }

                    }


                }
            });
        }
    }
    public void swapCursors(Cursor newCursor){
        this.cursor = newCursor;
        //this.notifyDataSetChanged();
        this.notifyItemChanged(i);

    }
}
