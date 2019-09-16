package com.amitKundu.tourmate.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.amitKundu.tourmate.BottomSheet_AddTrip;
import com.amitKundu.tourmate.Classes.IndividualTrip;
import com.amitKundu.tourmate.Fragment.MemoryFragment;
import com.amitKundu.tourmate.Fragment.WalletFragment;
import com.amitKundu.tourmate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private List<IndividualTrip> individualTrips;
    Context context;

    Task<Void> database;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String currentuser;

    private long fromdateMs;


    public TripAdapter(List<IndividualTrip> individualTrips, Context context) {
        this.individualTrips = individualTrips;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_trip_sample_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final IndividualTrip mylist = individualTrips.get(position);
        viewHolder.trip_title.setText("" + mylist.getTrip_Name());
        //viewHolder.trip_description.setText("" + mylist.getTrip_Description());
       // viewHolder.fromdate.setText("" + mylist.getTrip_fromDate());

        SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
       /// SimpleDateFormat dateSDF = new SimpleDateFormat("EEE, d MMM yyyy");

       Long longfrmDate=Long.valueOf( mylist.getTrip_fromDate());
       Long longToDate=Long.valueOf( mylist.getTrip_toDate());
       Date date = new Date();
      //  date.setTime(longfrmDate);




        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        month = month + 1;
        String selectedDate = year + "/" + month + "/" + day + " 23:59:59";

        SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat dateSDFF = new SimpleDateFormat("dd MMM yyyy");


        Date date1 = null;
        try {
            date1 = dateandTimeSDF.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fromdateMs = date1.getTime();


        if(longfrmDate <= fromdateMs && longToDate>=fromdateMs)
        {
            viewHolder.dayleftTv.setText(" In Tour");
        }


       else if (longfrmDate > fromdateMs)
        {
            long diff =longfrmDate -fromdateMs;
           long days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            //days = days;
            if (days == 0)
            {
                viewHolder.dayleftTv.setText(" Tomorrow");
            }
            else
                viewHolder.dayleftTv.setText(days + " days left");
        }



        else if(longToDate<fromdateMs)
        {
            viewHolder.dayleftTv.setTextColor(context.getResources().getColor(R.color.darkRed));
            viewHolder.dayleftTv.setText("Expired");

        }

        date.setTime(longfrmDate);

        viewHolder.fromdate.setText(dateSDF.format(date));


        viewHolder.memoryEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoryFragment memoryFragment = new MemoryFragment();

                Bundle bundle = new Bundle();
                bundle.putString("message", mylist.getTrip_id());
                memoryFragment.setArguments(bundle);
                FragmentManager fragmentManager2 = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.frame_layout_id, memoryFragment);
                fragmentTransaction2.addToBackStack("dashboard");
                fragmentTransaction2.commit();


            }
        });

        viewHolder.walletEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletFragment walletFragment = new WalletFragment();
                //String myMessage = "Stackoverflow is cool!";
                Bundle bundle = new Bundle();
                bundle.putString("message", mylist.getTrip_id());
                //FragmentClass fragInfo = new FragmentClass();
                // fragInfo.setArguments(bundle);
                walletFragment.setArguments(bundle);
                FragmentManager fragmentManager3 = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.frame_layout_id, walletFragment);
                fragmentTransaction3.addToBackStack("dashboard");
                fragmentTransaction3.commit();

            }
        });

        viewHolder.detailsEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.alartdialog_details_event, null);

                builder.setView(view);
                final Dialog dialog = builder.create();
                TextView tripTitleTv,tripDescriptionTv,tripStartPlaceTv,tripBudgetTv,tripStartDateTv,tripEndDateTv,alartCloseTv;

                tripTitleTv=view.findViewById(R.id.alartTripTitleTvId);
                tripDescriptionTv=view.findViewById(R.id.alartTripDescriptionTvId);
                tripStartPlaceTv=view.findViewById(R.id.alartTripStartPlaceTvId);
                tripBudgetTv=view.findViewById(R.id.alartTripBudgetTvId);
                tripStartDateTv=view.findViewById(R.id.alartTripSDateTvId);
                tripEndDateTv=view.findViewById(R.id.alartTripEDateTvId);
                alartCloseTv = view.findViewById(R.id.alartCloseTvID);

                SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
                /// SimpleDateFormat dateSDF = new SimpleDateFormat("EEE, d MMM yyyy");

                Long longfrmDate1=Long.valueOf( mylist.getTrip_fromDate());
                Long longtoDate1=Long.valueOf( mylist.getTrip_toDate());
                Date date0 = new Date();


//
//                viewHolder.fromdate.setText(dateSDF.format(date));

                tripTitleTv.setText(mylist.getTrip_Name());
                tripDescriptionTv.setText(mylist.getTrip_Description());
                tripStartPlaceTv.setText(mylist.getTrip_StartPlace());
                tripBudgetTv.setText(mylist.getTrip_Budget());
                date0.setTime(longfrmDate1);
                tripStartDateTv.setText(dateSDF.format(date0));
                date0.setTime(longtoDate1);
                tripEndDateTv.setText(dateSDF.format(date0));

                dialog.show();

                alartCloseTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });


        viewHolder.popUpMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, viewHolder.popUpMenuBtn);
                popupMenu.getMenuInflater().inflate(R.menu.pupup_trip_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.updateTripMenu:



                                BottomSheet_AddTrip bottomSheet_addTrip = new BottomSheet_AddTrip();
                                FragmentManager fragmentManager3 = ((AppCompatActivity) context).getSupportFragmentManager();
                                bottomSheet_addTrip.setEventID(mylist.getTrip_id());
                                bottomSheet_addTrip.setTriptitle(mylist.getTrip_Name());
                                bottomSheet_addTrip.setTripDescription(mylist.getTrip_Description());
                                bottomSheet_addTrip.setTripStart(mylist.getTrip_StartPlace());
                                bottomSheet_addTrip.setTripBudget(mylist.getTrip_Budget());
                                bottomSheet_addTrip.setSelectedFromDateinMS(Long.valueOf(mylist.getTrip_fromDate()));
                                bottomSheet_addTrip.setSelectedToDateinMS(Long.valueOf(mylist.getTrip_toDate()));
                                bottomSheet_addTrip.show(fragmentManager3,"Bottom");


                                break;
                            case R.id.deleteTripMenu:



                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater layoutInflater = LayoutInflater.from(context);
                                View view = layoutInflater.inflate(R.layout.delete_alartdialog_layout, null);

                                builder.setView(view);
                                final Dialog dialog = builder.create();
                                dialog.show();

                                TextView deleteActin = view.findViewById(R.id.deleteAlartTv);
                                TextView cancel = view.findViewById(R.id.cancelAlartTv);


                                deleteActin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        firebaseAuth = FirebaseAuth.getInstance();
                                        currentuser = firebaseAuth.getCurrentUser().getUid();
                                        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").child(mylist.getTrip_id()).removeValue();
                                        dialog.dismiss();
                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();
                                    }
                                });




                        }


                        return false;
                    }
                });


            }
        });


//
//
//            }
//        });
    }


    @Override
    public int getItemCount() {

        return individualTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView trip_title, trip_description, fromdate, todate, memoryEvent, walletEvent, detailsEvent,dayleftTv;
        private ImageView popUpMenuBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_title = itemView.findViewById(R.id.trip_name_id);
            //trip_description = itemView.findViewById(R.id.trip_description_id);
            fromdate = itemView.findViewById(R.id.trip_From_date);
            //todate = itemView.findViewById(R.id.trip_TO_Date);
            memoryEvent = itemView.findViewById(R.id.memoriesTvClickId);
            walletEvent = itemView.findViewById(R.id.walletTvClickId);
            detailsEvent = itemView.findViewById(R.id.detailsEventTvClickId);
            popUpMenuBtn = itemView.findViewById(R.id.popupTripBtnIvId);
            dayleftTv = itemView.findViewById(R.id.trip_day_left);
        }
    }
}
