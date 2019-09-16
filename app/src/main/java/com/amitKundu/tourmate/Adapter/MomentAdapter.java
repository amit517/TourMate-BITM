package com.amitKundu.tourmate.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.amitKundu.tourmate.Classes.MemoryClass;
import com.amitKundu.tourmate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String currentuser;
    Task<Void> database;

    private List<MemoryClass> memoryClasses;
    Context context;

    Uri myUri;



    private Picasso picasso;

    public MomentAdapter(List<MemoryClass> memoryClasses, Context context) {
        this.memoryClasses = memoryClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memory_item_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final MemoryClass memoryList = memoryClasses.get(i);

         myUri = Uri.parse(memoryList.getPostimages());
        Picasso.get().load(myUri).into(viewHolder.memoryImage);
        viewHolder.imageCaption.setText(memoryList.getCaption());




        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.alart_dialog_details_memories, null);

                builder.setView(view);
                final Dialog dialog = builder.create();
                TextView captionTv,descriptionTv,dateTv,timeTv,cancelMTv;

                captionTv=view.findViewById(R.id.alartMemoriesCaptionTvId);
                descriptionTv=view.findViewById(R.id.alartMemoriesDescriptionTvId);
                dateTv=view.findViewById(R.id.alartMemriesDate);
                timeTv=view.findViewById(R.id.alartMemoriesTimeTvId);
                cancelMTv=view.findViewById(R.id.alartMemoriesCloseTvID);

                captionTv.setText(memoryList.getCaption());
                descriptionTv.setText(memoryList.getCaptionDescription());
                dateTv.setText(memoryList.getCurrentMemoryDate());
                timeTv.setText(memoryList.getCurrentMemoryTime());

                dialog.show();

                cancelMTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                return false;
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.alart_dialog_zoom_image, null);

                builder.setView(view);
                final Dialog dialog = builder.create();

                TextView zoomCloseTv;
                zoomCloseTv=view.findViewById(R.id.zoomcloseTvId);

                PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
                myUri = Uri.parse(memoryList.getPostimages());
                Picasso.get().load(myUri).into(photoView);
               // photoView.setImageResource(R.drawable.image);

                dialog.show();

                 zoomCloseTv.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         dialog.dismiss();
                     }
                 });


            }
        });

    }

    @Override
    public int getItemCount() {
        return memoryClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView imageCaption;
        private ImageView memoryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoryImage = itemView.findViewById(R.id.memoryImageListIvId);
            imageCaption = itemView.findViewById(R.id.memoryCaptionListIvId);

        }
    }
}
