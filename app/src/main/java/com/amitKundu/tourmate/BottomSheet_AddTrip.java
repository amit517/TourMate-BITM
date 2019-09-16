package com.amitKundu.tourmate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.amitKundu.tourmate.Classes.IndividualTrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BottomSheet_AddTrip extends BottomSheetDialogFragment {

    private EditText addTriptitle, addTripDiscription, addTripStartPlace, addTripBudget;
    private Button addtrip;
    private ImageView fromDateIv, toDateIv;
    private TextView DateTv, toDateTv,addEventTv;
    private long selectedFromDateinMS;
    private long selectedToDateinMS;

    private RelativeLayout datePicker, timePicker;
    String addTripFromDate, addTripToDate;
    private FirebaseAuth firebaseAuth;
    String currentuser;
    private FirebaseDatabase firebaseDatabase;

    private List<IndividualTrip> tripList;

    /////////////////

    private String triptitle;
    private String tripDescription;

    private String tripStart;
    private String tripBudget;
    private String eventID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_add_trip, container, false);

        tripList = new ArrayList<>();
        datePicker = view.findViewById(R.id.datepicklayoutid);
        timePicker = view.findViewById(R.id.todatepicklayoutid);
        DateTv = view.findViewById(R.id.opendatepickerTvID);
        toDateTv = view.findViewById(R.id.toopendatepickerTvID);
        addTriptitle = view.findViewById(R.id.tripNameId);
        addTripDiscription = view.findViewById(R.id.tripDescriptionId);
        addtrip = view.findViewById(R.id.addTrip);
        addTripStartPlace = view.findViewById(R.id.tripStartingPlaceEtId);
        addTripBudget = view.findViewById(R.id.tripBudgetEtId);
        addEventTv = view.findViewById(R.id.addEventTvId);


        if (eventID != null) {
            addTriptitle.setText(triptitle);
            addTripDiscription.setText(tripDescription);
            addTripStartPlace.setText(tripStart);
            addTripBudget.setText(tripBudget);
            //Toast.makeText(getContext(), "date" + selectedFromDateinMS, Toast.LENGTH_SHORT).show();
            Date date = new Date();
            SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
            date.setTime(selectedFromDateinMS);
            DateTv.setText(dateSDF.format(date));
            date.setTime(selectedToDateinMS);
            toDateTv.setText(dateSDF.format(date));
            addEventTv.setText("Update Event");
            addtrip.setText("Update");

        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").push();
        //postRef = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events");

        addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triptitle = addTriptitle.getText().toString();
                tripDescription = addTripDiscription.getText().toString();
                addTripFromDate = String.valueOf(selectedFromDateinMS);
                addTripToDate = String.valueOf(selectedToDateinMS);
                tripStart = addTripStartPlace.getText().toString();
                tripBudget = addTripBudget.getText().toString();

                if (triptitle.equals("")) {
                    Toast.makeText(getContext(), "Please Enter your Event Title", Toast.LENGTH_SHORT).show();
                } else if (tripDescription.equals("")) {
                    Toast.makeText(getContext(), "Please Enter a description for your event", Toast.LENGTH_SHORT).show();
                } else {
                    saveToDB(new IndividualTrip(triptitle, tripDescription, addTripFromDate, addTripToDate, tripStart, tripBudget));
                }

            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDatePicker();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openToDatePicker();

            }
        });


        return view;
    }


    private void saveToDB(IndividualTrip individualTrip) {


        if (eventID != null) {

            DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser);

            //String userId = userDB.push().getKey();
            individualTrip.setTrip_id(eventID);

            userDB.child("Events").child(eventID).child("info").setValue(individualTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });
        }

        else {
            DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser);

            String userId = userDB.push().getKey();
            individualTrip.setTrip_id(userId);

            userDB.child("Events").child(userId).child("info").setValue(individualTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });
        }

    }


    //////////fromdate picker method
    private void openDatePicker() {

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
                DateTv.setText(dateSDF.format(date));


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

    ///to date///
    private void openToDatePicker() {

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

                selectedToDateinMS = date.getTime();
                toDateTv.setText(dateSDF.format(date));


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


    public long getSelectedFromDateinMS() {
        return selectedFromDateinMS;
    }

    public void setSelectedFromDateinMS(long selectedFromDateinMS) {
        this.selectedFromDateinMS = selectedFromDateinMS;
    }

    public long getSelectedToDateinMS() {
        return selectedToDateinMS;
    }

    public void setSelectedToDateinMS(long selectedToDateinMS) {
        this.selectedToDateinMS = selectedToDateinMS;
    }

    public String getTriptitle() {
        return triptitle;
    }

    public void setTriptitle(String triptitle) {
        this.triptitle = triptitle;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public String getTripStart() {
        return tripStart;
    }

    public void setTripStart(String tripStart) {
        this.tripStart = tripStart;
    }

    public String getTripBudget() {
        return tripBudget;
    }

    public void setTripBudget(String tripBudget) {
        this.tripBudget = tripBudget;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
