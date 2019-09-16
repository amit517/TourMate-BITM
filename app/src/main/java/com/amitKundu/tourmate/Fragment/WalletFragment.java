package com.amitKundu.tourmate.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.amitKundu.tourmate.Adapter.ExpenseAdapter;
import com.amitKundu.tourmate.BottomSheet.BottomSheet_AddExpense;
import com.amitKundu.tourmate.BottomSheet_AddTrip;
import com.amitKundu.tourmate.Classes.Expense;
import com.amitKundu.tourmate.Classes.IndividualTrip;
import com.amitKundu.tourmate.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private BottomSheet_AddExpense bottomSheet_addExpense;
    public String eventId;
    private BottomSheet_AddTrip bottomSheet_addTrip;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference dataB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private List<Expense> expenseList;
    private List<Expense> filterList;
    private ExpenseAdapter expenseAdapter;
    //private Context context;
    private FirebaseAuth firebaseAuth;
    private String currentuser;


    private NumberFormat nf = new DecimalFormat("##.###");
    //int total;

    private TextView currentBalanceTvId, expensePersentageTv, budExTv;

    ProgressBar progressBar;

    int expenditure;
    int reducedBudget = 0;
    int budget;
    int consumed;


    int cBudget;
    int cExpense;

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        filterList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();
        expenseList = new ArrayList<>();
        currentBalanceTvId = view.findViewById(R.id.currenBalanceDisplayTvId);

        expensePersentageTv = view.findViewById(R.id.expensePersentageTvId);
        budExTv = view.findViewById(R.id.budExTvId);


        recyclerView = view.findViewById(R.id.recyclerviewExpenseId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        eventId = getArguments().getString("message");
        //Toast.makeText(getContext(), "get" + eventId, Toast.LENGTH_SHORT).show();

        floatingActionButton = view.findViewById(R.id.floatingbtnId);

        progressBar = view.findViewById(R.id.progressBar);
        CreateProgressBar();


        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").child(eventId);
        database.child("Wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    expenseList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Expense expense = data.getValue(Expense.class);
                        expenseList.add(expense);

                    }

                    expenseAdapter = new ExpenseAdapter(expenseList, getContext());
                    expenseAdapter.setEventId(eventId);
                    recyclerView.setAdapter(expenseAdapter);
                    expenseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Empty database", Toast.LENGTH_SHORT).show();
                    expenseAdapter = new ExpenseAdapter(filterList, getContext());
                    expenseAdapter.setEventId(eventId);
                    recyclerView.setAdapter(expenseAdapter);
                    expenseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        dataB = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser);
        dataB.child("Events").child(eventId).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                budget = Integer.valueOf(dataSnapshot.getValue(IndividualTrip.class).getTrip_Budget());

                //   Toast.makeText(getContext(), "Budget" + budget, Toast.LENGTH_SHORT).show();

                cBudget = budget;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").child(eventId);
        database.child("Wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int total = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    int number = Integer.valueOf(ds.getValue(Expense.class).getExpenseAmount());
                    total = total + number;
                }

                //   Toast.makeText(getContext(), "Total Value"+total, Toast.LENGTH_SHORT).show();
                expenditure = total;
                // cExpense = expenditure;


                ShowProgressBar();

                checkBalance(total, budget);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void checkBalance(int total, int bud) {

        double consumed2 = (Double.valueOf(expenditure) * 100) / Double.valueOf(budget);
        expensePersentageTv.setText(String.valueOf(nf.format(consumed2)) + "%");
        final int cBalance = bud - total;
        currentBalanceTvId.setText(String.valueOf(cBalance) + " BDT");

        budExTv.setText(total + "/" + bud);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cBalance > 0) {
                    bottomSheet_addExpense = new BottomSheet_AddExpense();
                    bottomSheet_addExpense.setEventId(eventId);
                    bottomSheet_addExpense.show(getFragmentManager(), "bottomSheetImageDialog");

                } else {

                    Toast.makeText(getContext(), "Insufficient Balance!", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    private void ShowProgressBar() {

        if (expenditure >= 0) {
            calculateProgress();
        } else
            Toast.makeText(getContext(), "Sorry! No Ammount is remainnig.", Toast.LENGTH_SHORT).show();
    }

    private void calculateProgress() {
        if (expenditure >= 0) {

            //consumed = (expenditure * 100) / budget;
            double consumed3 = (Double.valueOf(expenditure) * 100) / Double.valueOf(budget);
            progressBar.setProgress(Integer.valueOf((int) consumed3));

        } else Toast.makeText(getContext(), "please enter some ammount", Toast.LENGTH_SHORT).show();
    }

    private void CreateProgressBar() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.showContextMenu();
        progressBar.setScaleY(5f);
    }

}
