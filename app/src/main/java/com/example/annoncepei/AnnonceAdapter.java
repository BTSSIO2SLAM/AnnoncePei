package com.example.annoncepei;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.annoncepei.Models.Annonce;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


/**
 * Created by ankit on 27/10/17.
 */

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.ViewHolder> {

    private Context context;
    private List<Annonce> list;

    public AnnonceAdapter(Context context, List<Annonce> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Annonce annonce = list.get(position);

        holder.textTitre.setText(annonce.getTitre());
        holder.textDetails.setText(annonce.getDetails());
        holder.textPrix.setText(String.valueOf(annonce.getPrix()));
        String urlOfImage = annonce.getImage().replaceAll("localhost","10.0.2.2");
        Picasso.with(context)
                .load(urlOfImage)
                .into(holder.imageView);
        //ew DownLoadImageTask(holder.imageView).execute(annonce.getImage());

        holder.annonce = annonce;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitre, textDetails, textPrix;
        public ImageView imageView;
        public Annonce annonce;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitre = itemView.findViewById(R.id.main_titre);
            textDetails = itemView.findViewById(R.id.main_details);
            textPrix = itemView.findViewById(R.id.main_prix);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, ViewAnnonceActivity.class);
                    update.putExtra("id",annonce.getId());
                    update.putExtra("titre",annonce.getTitre());
                    update.putExtra("details",annonce.getDetails());
                    update.putExtra("prix",annonce.getPrix());
                    update.putExtra("urlphoto",annonce.getImage());
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                }
            });
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                urlOfImage = urlOfImage.replaceAll("localhost","10.0.2.2");
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

}
