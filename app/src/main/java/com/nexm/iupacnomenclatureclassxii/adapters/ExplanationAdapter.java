package com.nexm.iupacnomenclatureclassxii.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.nexm.iupacnomenclatureclassxii.R;

import java.util.List;

public class ExplanationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private Bitmap bm;
    private List<UnifiedNativeAd> mNativeAds ;
    private Typeface typeface;
    // A explanation item view type.
    private static final int E_ITEM_VIEW_TYPE = 0;



    public ExplanationAdapter(Cursor mcursor,List<UnifiedNativeAd> mmNativeAds){
        this.cursor = mcursor;
        this.mNativeAds = mmNativeAds;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        typeface = Typeface.createFromAsset(context.getAssets(),"Cabin-Regular.otf");
        switch (viewType) {


            case E_ITEM_VIEW_TYPE:
                // Fall through.
            default:
                View menuItemLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.explanation_recycler_item, parent, false);
                return new ExplanationAdapter.ViewHolder(menuItemLayoutView);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
       // if(i > 3)i--;
        cursor.moveToPosition(i);
        int viewType = getItemViewType(position);
        switch (viewType) {

            case E_ITEM_VIEW_TYPE:
                // fall through
            default:
                ExplanationAdapter.ViewHolder viewHolder = (ExplanationAdapter.ViewHolder)holder;
                viewHolder.explanation.setText(cursor.getString(cursor.getColumnIndex("etext")));
                viewHolder.hint.setText(cursor.getString(cursor.getColumnIndex("tip")));
                //viewHolder.hint.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(context,R.drawable.ic_grade_black_24dp),null,null,null);
                byte[] b = cursor.getBlob(cursor.getColumnIndex("eimage"));
                bm = BitmapFactory.decodeByteArray(b, 0, b.length);
                // Bitmap useThisBitmap = Bitmap.createScaledBitmap(bm,bm.getWidth(),bm.getHeight(), true);

                viewHolder.image.setImageBitmap(bm);
        }
        //bm.recycle();


    }
    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
    @Override
    public int getItemViewType(int position) {


       /* if (position ==3 || position==getItemCount()-1) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }*/
        return E_ITEM_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
       // return cursor.getCount()+mNativeAds.size();
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView explanation,hint;
        final ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            explanation = itemView.findViewById(R.id.explanation_recycler_item_explainationView);
            hint =  itemView.findViewById(R.id.explanation_recycler_item_hintView);
            image =  itemView.findViewById(R.id.explanation_recycler_item_imageView);
            explanation.setTypeface(typeface);
            hint.setTypeface(typeface);

        }
    }
    public void setBitmapNull(){
        bm.recycle();
       bm = null;
    }
}
