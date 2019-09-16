package com.amitKundu.tourmate.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.amitKundu.tourmate.BottomSheet.BottomSheet_AddExpense;
import com.amitKundu.tourmate.Classes.Expense;
import com.amitKundu.tourmate.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private String currentuser;
    private StorageReference postimagesreference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, postRef;
    private FirebaseDatabase firebaseDatabase;
    private SimpleDateFormat timeSDF = new SimpleDateFormat("hh:mm aa");

    private List<Expense> expenseList;
    Context context;

    private String eventId;

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ExpenseAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Expense expenseitem = expenseList.get(i);

        viewHolder.exTypeTv.setText(expenseitem.getExpenseType());
        viewHolder.exAmountTv.setText(expenseitem.getExpenseAmount());
        viewHolder.expenseTimeTv.setText(expenseitem.getExpenseDate());

        viewHolder.popupMenuExpenseIvId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, viewHolder.popupMenuExpenseIvId);
                popupMenu.getMenuInflater().inflate(R.menu.popup_wallet_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.updateWaletMenu:

                                BottomSheet_AddExpense bottomSheet_addExpense = new BottomSheet_AddExpense();
                                FragmentManager fragmentManager2 = ((AppCompatActivity) context).getSupportFragmentManager();
                                bottomSheet_addExpense.setEventId(eventId);
                                bottomSheet_addExpense.setExpenseType(expenseitem.getExpenseType());
                                bottomSheet_addExpense.setExpenseAmount(expenseitem.getExpenseAmount());
                                bottomSheet_addExpense.setFlag(1);
                                bottomSheet_addExpense.setCurentExpenseId(expenseitem.getExpenseId());
                                bottomSheet_addExpense.show(fragmentManager2, "bottom");

                                break;
                            case R.id.deleteWaletMenu:


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
                                        postimagesreference = FirebaseStorage.getInstance().getReference();

                                        firebaseAuth = FirebaseAuth.getInstance();
                                        currentuser = firebaseAuth.getCurrentUser().getUid();

                                        DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser).child("Events").child(eventId);
                                        userDB.child("Wallet").child(expenseitem.getExpenseId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        notifyDataSetChanged();
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


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exTypeTv, exAmountTv, expenseTimeTv;
        private ImageView popupMenuExpenseIvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exTypeTv = itemView.findViewById(R.id.expenseTypeListTvId);
            exAmountTv = itemView.findViewById(R.id.expenseAmountListTvId);
            expenseTimeTv = itemView.findViewById(R.id.expenseTimeTvId);
            popupMenuExpenseIvId = itemView.findViewById(R.id.popUpMenuExpenseId);

        }
    }
}
