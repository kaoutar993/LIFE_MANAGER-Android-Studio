package com.khadija.makhchoun.lifemanager.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khadija.makhchoun.lifemanager.Model.Data;
import com.khadija.makhchoun.lifemanager.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends androidx.fragment.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Floating Button :
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    // Floating button text view :
    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    // Boolean
    private boolean isOpen=false;
    // Animation.
    private Animation FadOpen,FadeClose;
    // Dashbord income and expense result :
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    // Firebase :
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    // Recycler view :
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment :

        View myview=inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        // Connect floating button to layout :

        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_Ft_btn);

        // Connect floating text :

        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        //Total income and expense result SET :
        totalIncomeResult=myview.findViewById(R.id.income_set_result);
        totalExpenseResult=myview.findViewById(R.id.expense_set_result);

        // Recycler :
        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);

        // Animation Connect :

        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);




        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                if (isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    isOpen=false;
                }
                else{
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen=true;

                }

            }
        });

        // Calculate total income :
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum=0;

                for (DataSnapshot mydatasnapshot:dataSnapshot.getChildren()){
                    Data data = mydatasnapshot.getValue(Data.class);
                    totalSum+=data.getAmount();
                    String stResult=String.valueOf(totalSum);
                    totalIncomeResult.setText(stResult+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Calculate total expense :
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum=0;
                for (DataSnapshot mydatasnapshot:dataSnapshot.getChildren()){
                    Data data = mydatasnapshot.getValue(Data.class);
                    totalSum+=data.getAmount();
                    String stResult=String.valueOf(totalSum);
                    totalExpenseResult.setText(stResult+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Recycler :
        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return myview;
    }
    // Floating button animation :
    private void ftAnimation() {
        if (isOpen) {
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

            isOpen = false;
        } else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen = true;

        }
    }



    private void addData(){
        //Fab button income :
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });
        //Fab button expense :
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });

    }
    public void incomeDataInsert(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insert_data,null);
        mydialog.setView(myview);
        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);

        final EditText edtAmount=myview.findViewById(R.id.amount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();
                if (TextUtils.isEmpty(type)){
                    edtType.setError("Champ obligatoire !");
                    return;
                }
                if (TextUtils.isEmpty(amount)){
                    edtAmount.setError("Champ obligatoire !");
                    return;
                }
                int ouramountint=Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)){
                    edtNote.setError("Champ obligatoire !");
                    return;
                }
                String id=mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(ouramountint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Données enregistées !",Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void expenseDataInsert(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insert_data,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final EditText amount=myview.findViewById(R.id.amount_edt);
        final EditText type=myview.findViewById(R.id.type_edt);
        final EditText note=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmAmount=amount.getText().toString().trim();
                String tmtype=type.getText().toString().trim();
                String tmnote=note.getText().toString().trim();
                if (TextUtils.isEmpty(tmtype)){
                    type.setError("Champ obligatoire !");
                    return;
                }
                if (TextUtils.isEmpty(tmAmount)){
                    amount.setError("Champ obligatoire !");
                    return;
                }
                int ouramountint=Integer.parseInt(tmAmount);

                if (TextUtils.isEmpty(tmnote)){
                    note.setError("Champ obligatoire !");
                    return;
                }
                String id =mExpenseDatabase.push().getKey();
                String mDate=DateFormat.getDateInstance().format(new Date());
                Data data=new Data(ouramountint,tmtype,tmnote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Données enregistrées !",Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,IncomeViewHolder>incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income,
                        DashBoardFragment.IncomeViewHolder.class,
                        mIncomeDatabase

                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                incomeViewHolder.setIncomeType(model.getType());
                incomeViewHolder.setIncomeAmount(model.getAmount());
                incomeViewHolder.setIncomeDate(model.getDate());

            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data,ExpenseViewHolder>expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_expense,
                        DashBoardFragment.ExpenseViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data model, int i) {
                expenseViewHolder.setExpenseType(model.getType());
                expenseViewHolder.setExpenseAmount(model.getAmount());
                expenseViewHolder.setExpenseDate(model.getDate());


            }
        };

        mRecyclerExpense.setAdapter(expenseAdapter);


    }
    // For income data :
    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView=itemView;
        }
        public void setIncomeType(String type){
            TextView mtype=mIncomeView.findViewById(R.id.type_Income_ds);
            mtype.setText(type);
        }
        public void setIncomeAmount(int amount){
            TextView mAmount=mIncomeView.findViewById(R.id.amount_Income_ds);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }
        public void setIncomeDate(String date){
            TextView mdate=mIncomeView.findViewById(R.id.date_Income_ds);
            mdate.setText(date);
        }
    }
    // For expense data :
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{
        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView=itemView;
        }
        public void setExpenseType(String type){
            TextView mtype=mExpenseView.findViewById(R.id.type_Expense_ds);
            mtype.setText(type);
        }
        public void setExpenseAmount(int amount){
            TextView mAount=mExpenseView.findViewById(R.id.amount_Expense_ds);
            String strAmount=String.valueOf(amount);
            mAount.setText(strAmount);
        }
        public void setExpenseDate(String date){
            TextView mdate=mExpenseView.findViewById(R.id.date_Expense_ds);
            mdate.setText(date);
        }
    }
}