package com.kumbh.design.Epost.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.droidninja.imageeditengine.ImageEditor;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.model.ListPostItemFestival;
import com.kumbh.design.Epost.model.ListTemplateItem;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FestivalImageAdapter extends RecyclerView.Adapter<FestivalImageAdapter.MyViewHolder> {

    private Context context;
    private List<ListTemplateItem> images;
    private LayoutInflater inflater;
    private String festival,id;
    private SessionManager pref_manager;
    public FestivalImageAdapter(Context context, List<ListTemplateItem> images, String Fest, String id){
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        festival = Fest;
        id = id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        pref_manager = new SessionManager(context);
        View v = inflater.inflate(R.layout.fesrival_grid,null,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.animationView.setVisibility(View.VISIBLE);
        Picasso.get().load(images.get(position).getTemplateDemoImagePath()).into(holder.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.animationView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
            }
        });



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap returnedBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);

                v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                v.draw(canvas);
                new ImageEditor.Builder(((Activity)context), images.get(position).getTemplateImagePath(),festival,pref_manager.getName()).setStickerAssets("stickers").festivalCall(images.get(position).getTemplateImagePath(),images.get(position).getPostId(),images.get(position).getTemplateId());

            }
        });




//        holder.img_test.setImageDrawable(context.getResources().getDrawable(R.drawable.logo_cicle));


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,img_test;
        FrameLayout relativeLayout;
        LottieAnimationView animationView;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image1);
            relativeLayout = itemView.findViewById(R.id.grid_image);
            animationView = itemView.findViewById(R.id.animation_view);
//            img_test=itemView.findViewById(R.id.img_set);

        }
    }
}
