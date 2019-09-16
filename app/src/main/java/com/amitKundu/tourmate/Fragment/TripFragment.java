package com.amitKundu.tourmate.Fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.amitKundu.tourmate.Adapter.TripAdapter;
import com.amitKundu.tourmate.BottomSheet_AddTrip;
import com.amitKundu.tourmate.Classes.IndividualTrip;
import com.amitKundu.tourmate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripFragment extends Fragment {


    public TripFragment() {
    }

    private BottomSheet_AddTrip bottomSheet_addTrip;
    private FloatingActionButton fab;
    private RecyclerView triprecyclerView;
    DatabaseReference database;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private List<IndividualTrip> list;
    private List<IndividualTrip> filterList;
    private TripAdapter tripAdapter;
    //private Context context;
    private FirebaseAuth firebaseAuth;
    String currentuser;

    private long fromdateMs;
    private long fromdateMs1;
    private long todateMs;
    private long todateMss = Long.valueOf("2592000000");
    ////////////////

    private TextView fromDateTv, toDateTv;
    private LinearLayout fromDatepicked, toDatepicked;
    private long selectedFromDateinMS;
    private long selectedToDateinMS;
    private TextView viewAllTripsTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();
        list = new ArrayList<>();
        filterList = new ArrayList<>();

        ///////////////////////

        fromDateTv = view.findViewById(R.id.fromDateTV);
        toDateTv = view.findViewById(R.id.toDateTV);

        fromDatepicked = view.findViewById(R.id.fromDatePickDashboadLayoutId);
        toDatepicked = view.findViewById(R.id.toDatePickDashboardID);

        //  viewAllTripsTv = view.findViewById(R.id.viewAllTrips);
        ///////////////


        triprecyclerView = view.findViewById(R.id.trip_recycler_view);
        triprecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_addTrip = new BottomSheet_AddTrip();
                bottomSheet_addTrip.show(getFragmentManager(), "BootmSheet_addtrip");

            }
        });


        ///////////////////////////////////////

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        month = month + 1;

        String selectedDate = year + "/" + month + "/" + day + " 00:00:00";

        SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");

        Date date = null;
        try {
            date = dateandTimeSDF.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fromdateMs = date.getTime();
        // fromDateTv.setText(dateSDF.format(date));


        int year1 = calendar.get(calendar.YEAR);
        int month1 = calendar.get(calendar.MONTH);
        int day1 = calendar.get(calendar.DAY_OF_MONTH);
        month1 = month1 + 1;
        day1 = day1 + 30;
        String selectedtoDate = year1 + "/" + month1 + "/" + day1 + " 23:59:59";

        SimpleDateFormat todateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        Date date1 = new Date();

        date1 = null;
        try {
            date1 = todateandTimeSDF.parse(selectedtoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // long days = (int) TimeUnit.DAYS.convert(2592000000, TimeUnit.MILLISECONDS);
        todateMs = date1.getTime();
        // todateMs=todateMs+todateMss;

        //toDateTv.setText(dateSDF.format(date1));

        /////////


        viewAllTrip();


//
//        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser);
//        database.child("Events").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    filterList.clear();
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        String fromDatetrip = data.child("info").getValue(IndividualTrip.class).getTrip_fromDate();
//                        String toDatetrip = data.child("info").getValue(IndividualTrip.class).getTrip_toDate();
//                        // IndividualTrip trip = data.child("info").getValue(IndividualTrip.class);
//                        //String toDatetrip = data.getValue(IndividualTrip.class).getTrip_toDate();
//                        //list.add(trip);
//                        //if()
//
//                        Long flong = Long.valueOf(fromDatetrip);
//                        Long tlong = Long.valueOf(toDatetrip);
//                        // Long tlong = Long.valueOf(toDatetrip);
//
////                        if(fromdateMs<=tlong&& todateMs>=tlong)
////                        {
////                            IndividualTrip trip =data.child("info").getValue(IndividualTrip.class);
////                            filterList.add(trip);
////                        }
//
//                        Calendar calendar = Calendar.getInstance();
//
//                        int year = calendar.get(calendar.YEAR);
//                        int month = calendar.get(calendar.MONTH);
//                        int day = calendar.get(calendar.DAY_OF_MONTH);
//                        month = month + 1;
//                        String selectedDate = year + "/" + month + "/" + day + " 23:59:59";
//
//                        SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                        SimpleDateFormat dateSDFF = new SimpleDateFormat("dd MMM yyyy");
//
//
//                        Date date1 = null;
//                        try {
//                            date1 = dateandTimeSDF.parse(selectedDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        fromdateMs1 = date1.getTime();
//
//
//                        if(flong <= fromdateMs1 && tlong>=fromdateMs1)
//                        {
//                            IndividualTrip trip =data.child("info").getValue(IndividualTrip.class);
//                            filterList.add(trip);
//
//                        }
//
//                    }
//                   // Toast.makeText(getContext(), ""+filterList.size(), Toast.LENGTH_SHORT).show();
//                    if(filterList.size()==0)
//                    {
//                        viewAllTrip();
//                        return;
//
//                    }
//                    tripAdapter = new TripAdapter(filterList, getContext());
//                    triprecyclerView.setAdapter(tripAdapter);
//                    tripAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getActivity(), "Empty database", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


//viewAllTripsTv.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//
//    viewAllTrip();
//
//
//    }
//});


///////////////////////////date pick action///////////////////////

        fromDatepicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFromDatePicker();
            }
        });

        toDatepicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openToDatePicker();

            }
        });


        return view;

    }

    private void viewAllTrip() {

        fromDateTv.setText("Chose from Date");
        toDateTv.setText("Chose To Date");


        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser);
        database.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        IndividualTrip trip = data.child("info").getValue(IndividualTrip.class);

                        list.add(trip);

                    }
                    tripAdapter = new TripAdapter(list, getContext());
                    triprecyclerView.setAdapter(tripAdapter);
                    tripAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Empty database", Toast.LENGTH_SHORT).show();
                    tripAdapter = new TripAdapter(filterList, getContext());
                    triprecyclerView.setAdapter(tripAdapter);
                    tripAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //////////fromdate picker method
    private void openFromDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String selectedDate = year + "/" + month + "/" + dayOfMonth + " 00:00:00";

                SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
                Date date = null;
                try {
                    date = dateandTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                selectedFromDateinMS = date.getTime();
                fromDateTv.setText(dateSDF.format(date));

            }
        };
        /////getcurntdate
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePickerDialog.show();
    }


    ////Todate picker method/////
    private void openToDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String selectedDate = year + "/" + month + "/" + dayOfMonth + " 23:59:59";

                SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
                Date date = null;
                try {
                    date = dateandTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                selectedToDateinMS = date.getTime();
                toDateTv.setText(dateSDF.format(date));

                //////databaase action////


                database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser);
                database.child("Events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            filterList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String fromDatetrip = data.child("info").getValue(IndividualTrip.class).getTrip_fromDate();
                                // IndividualTrip trip = data.child("info").getValue(IndividualTrip.class);
                                //String toDatetrip = data.getValue(IndividualTrip.class).getTrip_toDate();
                                //list.add(trip);
                                //if()

                                Long flong = Long.valueOf(fromDatetrip);
                                // Long tlong = Long.valueOf(toDatetrip);

                                if (selectedFromDateinMS <= flong && selectedToDateinMS >= flong) {
                                    IndividualTrip trip = data.child("info").getValue(IndividualTrip.class);
                                    filterList.add(trip);
                                }

                            }
                            // Toast.makeText(getContext(), ""+filterList.size(), Toast.LENGTH_SHORT).show();
                            tripAdapter = new TripAdapter(filterList, getContext());
                            triprecyclerView.setAdapter(tripAdapter);
                            tripAdapter.notifyDataSetChanged();
                        } else {
                           // Toast.makeText(getActivity(), "Empty database1", Toast.LENGTH_SHORT).show();
                            tripAdapter = new TripAdapter(filterList, getContext());
                            triprecyclerView.setAdapter(tripAdapter);
                            tripAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        /////getcurntdate
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePickerDialog.show();

    }


}
