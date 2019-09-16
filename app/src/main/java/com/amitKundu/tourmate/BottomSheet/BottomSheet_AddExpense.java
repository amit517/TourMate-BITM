package com.amitKundu.tourmate.BottomSheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.amitKundu.tourmate.Classes.Expense;
import com.amitKundu.tourmate.Classes.IndividualTrip;
import com.amitKundu.tourmate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BottomSheet_AddExpense extends BottomSheetDialogFragment {

    private String eventId;

    private EditText expenseTypeEt;
    private EditText expenseAmountEt;
    private String expenseTime;
    private Button addExpensebtnl;


    private String currentuser;
    private StorageReference postimagesreference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, postRef;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference userDB;


    String expenseType;
    String expenseAmount;
    int flag = 0;
    String curentExpenseId;


    int expenditure;
    int reducedBudget = 0;
    int budget;
    int consumed;
    int total = 0;
    int cbalance=0;

    int cBudget;
    int cExpense;


    public void setCurentExpenseId(String curentExpenseId) {
        this.curentExpenseId = curentExpenseId;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public void setEventId(String eventId) {

        this.eventId = eventId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_add_expense, container, false);

        expenseTypeEt = view.findViewById(R.id.expenseTypeIdListEtId);
        expenseAmountEt = view.findViewById(R.id.expenseAmountnListEtId);
        addExpensebtnl = view.findViewById(R.id.addExensebtnId);
        TextView addExpenseTv = view.findViewById(R.id.addExpenseTvId);


        firebaseDatabase = FirebaseDatabase.getInstance();
        postimagesreference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
        expenseTime = currenttime.format(callForDate.getTime());

        // Toast.makeText(getContext(), "Flag"+flag, Toast.LENGTH_SHORT).show();
        if (flag == 1) {
            expenseTypeEt.setText(expenseType);
            expenseAmountEt.setText(expenseAmount);
            addExpensebtnl.setText("Update");
            addExpenseTv.setText("Update Expense");

        }


        addExpensebtnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               DatabaseReference dataB = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser);
                dataB.child("Events").child(eventId).child("info").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            budget = Integer.valueOf(dataSnapshot.getValue(IndividualTrip.class).getTrip_Budget());
                        }

                        //   Toast.makeText(getContext(), "Budget" + budget, Toast.LENGTH_SHORT).show();

                        cBudget = budget;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


              DatabaseReference  database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").child(eventId);
                database.child("Wallet").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //int total = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            int number = Integer.valueOf(ds.getValue(Expense.class).getExpenseAmount());
                            total = total + number;
                        }

                        //   Toast.makeText(getContext(), "Total Value"+total, Toast.LENGTH_SHORT).show();
                        expenditure = total;
                        // cExpense = expenditure;

                        if(cbalance==0)
                        {
                            checkBalance(total, budget);
                            cbalance++;
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }


        });


        return view;
    }


    private void checkBalance(int total, int bud) {


        double consumed2 = (Double.valueOf(expenditure) * 100) / Double.valueOf(budget);

        final int cBalance = bud - total;
        expenseType = expenseTypeEt.getText().toString();
        expenseAmount = expenseAmountEt.getText().toString();


        double exAmount = Double.valueOf(expenseAmount);

        double balaneStatus = cBalance - exAmount;
        if (balaneStatus < 0) {
            Toast.makeText(getContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
            return;
        }

        addExpense();

        // Toast.makeText(getContext(), "no balance", Toast.LENGTH_SHORT).show();


    }


    private void addExpense() {
        expenseType = expenseTypeEt.getText().toString();
        expenseAmount = expenseAmountEt.getText().toString();

        if (expenseType == null) {
            Toast.makeText(getContext(), "enter expense Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (expenseAmount == null) {
            Toast.makeText(getContext(), "enter expense amount", Toast.LENGTH_SHORT).show();
            return;
        }


        saveToDB(new Expense(expenseType, expenseAmount, expenseTime));

    }

    private void saveToDB(Expense expense) {

        // Toast.makeText(getContext(), "Flag"+flag, Toast.LENGTH_SHORT).show();

        if (flag == 1) {
            DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser).child("Events").child(eventId);

            //String memoryId = userDB.push().getKey();
            expense.setExpenseId(curentExpenseId);

            userDB.child("Wallet").child(curentExpenseId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });

        } else {

            DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser).child("Events").child(eventId);

            String memoryId = userDB.push().getKey();
            expense.setExpenseId(memoryId);

            userDB.child("Wallet").child(memoryId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });


        }


    }


}
