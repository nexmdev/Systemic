package com.nexm.iupacnomenclatureclassxii.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nexm.iupacnomenclatureclassxii.R;

public class PracticeTopicsAdapter extends RecyclerView.Adapter<PracticeTopicsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private Bitmap bm;
    private static OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int topicNo);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        PracticeTopicsAdapter.listener = listener;
    }

    public PracticeTopicsAdapter(Cursor mcursor){this.cursor = mcursor;}
    @Override
    public PracticeTopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practice_topics_layout,parent,false);

        return new PracticeTopicsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PracticeTopicsAdapter.ViewHolder holder, int position) {
       int i = holder.getAdapterPosition();
       cursor.moveToPosition(i);
       holder.title.setText(cursor.getString(cursor.getColumnIndex("Topic_Name")));
       int noOfQuestions = cursor.getInt(cursor.getColumnIndex("Last_Q")) - cursor.getInt(cursor.getColumnIndex("First_Q"));
       noOfQuestions++;
       holder.questions.setText("Questions : "+noOfQuestions);
       int attempedQuestions = cursor.getInt(cursor.getColumnIndex("Present_Q")) - cursor.getInt(cursor.getColumnIndex("First_Q"));
       String temp = attempedQuestions + "/"+noOfQuestions;
       holder.attempted.setText(temp);
       int percentageProgress = (attempedQuestions*100)/noOfQuestions;
       holder.progressBar.setProgress(percentageProgress);
       byte[] b = cursor.getBlob(cursor.getColumnIndex("Topic_Img"));
       bm = BitmapFactory.decodeByteArray(b, 0, b.length);
       holder.imageView.setImageBitmap(bm);


    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,questions,attempted;
        ImageView imageView;
        ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.practice_recyclerView_item_name);
            questions = itemView.findViewById(R.id.practice_recyclerView_item_no_of_questions);
            attempted = itemView.findViewById(R.id.practice_recyclerView_item_attempted);
            imageView = itemView.findViewById(R.id.practice_recyclerView_item_image);
            progressBar = itemView.findViewById(R.id.practice_recyclerView_item_progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();
                    cursor.moveToPosition(i);
                    if(listener != null){
                        listener.onItemClick(cursor.getInt(cursor.getColumnIndex("Topic_No"))
                        );
                    }
                }
            });
        }
    }
}
