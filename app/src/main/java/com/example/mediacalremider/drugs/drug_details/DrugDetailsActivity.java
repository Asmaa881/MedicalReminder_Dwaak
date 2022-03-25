package com.example.mediacalremider.drugs.drug_details;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.MainActivity.repo;
import static com.example.mediacalremider.MainActivity.userDrugs;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.drugsModel;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.requests;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugThirdFragment.days;
import static com.example.mediacalremider.home.HomeActivity.drugsAdapter1;
import static com.example.mediacalremider.users.UserController.userDocRef;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.DrugsAdapter;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity;
import com.example.mediacalremider.drugs.add_drugs.view.UpdateReminderTimeAdapter;
import com.example.mediacalremider.home.HomeActivity;
import com.example.mediacalremider.notifications.ReminderWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DrugDetailsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String drugId, editDate;
    List<String> editDays = new ArrayList<>();
    boolean [] editSelectedDay;
    ArrayList<Integer> editDayList = new ArrayList<>();
    String [] editDayArray;
    List editTimes;
    boolean isDataChanges = false;
    int drugPos;
    ProgressDialog progressDialog;
    DrugsModel drugsModel;
    ImageButton arrowButton1, arrowButton2, arrowButton3,arrowButton4,arrowButton5, arrowButton6, arrowButton7,arrowButton8;
    LinearLayout hiddenView1, hiddenView2, hiddenView3, hiddenView4,hiddenView5,hiddenView6, hiddenView7,hiddenView8;
    CardView cardDrugName, cardDrugStrength, cardDrugReminders, cardDrugSchedual, cardDrugInstruction, cardDrugIcon, cardDrugRefill, cardDrugReson;
    TextView textDrugNameText, textDrugStrength, textDrugInstruction, textViewDrugCurrentPillNum
            ,textViewDrugReminderPillNum, textViewDrugRefillTime, textViewDrugReson, textViewDrugStartDate, textViewDrugEndDate;
    TextView textVieweditDrugSchedualEndDate, txtDrugReminerCounts, txtDrugReminerTimes, txtDrugReminerDays;
    EditText editTexteditDrugName,editTextEditDrugStrength,editTextEditDrugInstruction, editTextEditDrugReson,
            editTextEditDrugCurrentPillNum , editTextEditDrugReminderNum, editTextEditDrugNumOfTimes;
    ImageView imgViewDrugIcone, imgPill, imgInjection, imgInhaler, imgDrops, imgPowder, imgTemp, imgOther;
    RecyclerView recyclerEditDrugTimes;
    RadioGroup radioGroupEditOptions;
    RadioButton editRadioPerDay, editaRadioPerWeek;
    Button btnUpdateDrugData;
    ImageView imgViewEditBack, imgViewDeleteDrug;
    TextView txtDisplayTitle;
    Dialog dialog, deleteDialog;
    Button btnCancelDelete, btnConfirmDelete;
    Button btnConfirmUpdate, btnCancelUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_details);

        intializeVariables();
        intialization();
        displayDrugData();


        imgViewEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToHome = new Intent(DrugDetailsActivity.this,HomeActivity.class);
                startActivity(backToHome);
            }
        });
        imgViewDeleteDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.show();
            }
        });
        btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDrug();
            }
        });
        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        textVieweditDrugSchedualEndDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createDatePickDialogForEditingEndDate();
            }
        });
        editRadioPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtEditSelectDays.setVisibility(View.INVISIBLE);
                recyclerEditDrugTimes.setVisibility(View.VISIBLE);
                setRecyclerViewEditDrugTimes();
            }
        });
        editaRadioPerWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadioPerWeek();
            }
        });
        imgPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPill.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.pill);
                drugsModel.setIcon(R.drawable.pill);
            }
        });
        imgDrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDrops.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.drops);
                drugsModel.setIcon(R.drawable.drops);

            }
        });
        imgInhaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgInhaler.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.inhaler);
                drugsModel.setIcon(R.drawable.inhaler);
            }
        });
        imgInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgInjection.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.injection);
                drugsModel.setIcon(R.drawable.injection);
            }
        });
        imgPowder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPowder.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.powder);
                drugsModel.setIcon(R.drawable.powder);
            }
        });
        imgTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgTemp.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.temp);
                drugsModel.setIcon(R.drawable.temp);
            }
        });
        imgOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgOther.setAlpha(0.5f);
                imgViewDrugIcone.setImageResource(R.drawable.other);
                drugsModel.setIcon(R.drawable.other);
            }
        });

        editTexteditDrugName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTexteditDrugName.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setName(editTexteditDrugName.getText().toString());
                    textDrugNameText.setText(editTexteditDrugName.getText().toString());
                    isDataChanges = true;
                }
            }
        });
        editTextEditDrugStrength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextEditDrugStrength.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setStrength(editTextEditDrugStrength.getText().toString());
                    textDrugStrength.setText(editTextEditDrugStrength.getText().toString());
                    isDataChanges = true;
                }
            }
        });
        editTextEditDrugInstruction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextEditDrugInstruction.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setInstructions(editTextEditDrugInstruction.getText().toString());
                    textDrugInstruction.setText(editTextEditDrugInstruction.getText().toString());
                    isDataChanges = true;
                }
            }
        });
        editTextEditDrugReson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextEditDrugReson.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setReasons(editTextEditDrugReson.getText().toString());
                    textViewDrugReson.setText(editTextEditDrugReson.getText().toString());
                    isDataChanges = true;
                }
            }
        });
        editTextEditDrugCurrentPillNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextEditDrugCurrentPillNum.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setCurrentNumOfPills(Integer.valueOf(editTextEditDrugCurrentPillNum.getText().toString()));
                    textViewDrugCurrentPillNum.setText(editTextEditDrugCurrentPillNum.getText().toString());
                    isDataChanges = true;
                }
            }
        });
        editTextEditDrugReminderNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextEditDrugReminderNum.getText().toString().trim().equalsIgnoreCase(""))){
                    drugsModel.setNumOfRefillReminder(Integer.valueOf(editTextEditDrugReminderNum.getText().toString()));
                    textViewDrugReminderPillNum.setText(editTextEditDrugReminderNum.getText().toString());
                    isDataChanges = true;
                }
            }
        });

        arrowButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugName();
            }
        });
        arrowButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugStrength();
            }
        });
        arrowButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugRemindersTimes();
            }
        });
        arrowButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugSchedual();
            }
        });
        arrowButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugInstruction();
            }
        });
        arrowButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugIcon();
            }
        });
        arrowButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugRefill();
            }
        });
        arrowButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardDrugReson();
            }
        });
        btnConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimesAndDate();
                updateDrugData();
            }
        });
        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdateDrugData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void setTimesAndDate(){
        if(!(textVieweditDrugSchedualEndDate.getText().toString().trim().equalsIgnoreCase(""))){
            textViewDrugEndDate.setText(textVieweditDrugSchedualEndDate.getText().toString());
            drugsModel.setEndDate(textVieweditDrugSchedualEndDate.getText().toString());
            isDataChanges = true;
        }
        if(!(editTextEditDrugNumOfTimes.getText().toString().trim().equalsIgnoreCase("")) ||
                !(editTextEditDrugNumOfTimes.getText().toString().equals("0"))){
            if (!getEditSelectedOption().isEmpty()){
                String numOfTime = editTextEditDrugNumOfTimes.getText().toString()+ getEditSelectedOption();
                txtDrugReminerCounts.setText(numOfTime);
                txtDrugReminerTimes.setText("");
                drugsModel.setNumberOfTimes(numOfTime);
                isDataChanges = true;
            }
            else{
                Toast.makeText(DrugDetailsActivity.this, "Please Choose Per which!!!", Toast.LENGTH_SHORT).show();
            }
        }
        if(UpdateReminderTimeAdapter.timesSelected != null){
            drugsModel.setTimes(UpdateReminderTimeAdapter.timesSelected);
            isDataChanges = true;
        }
    }
    private void updateDrugData(){
        drugId = drugsModel.getId();
        progressDialog.setTitle("Updating Drug Data.");
        progressDialog.show();
        db.collection("Drugs")
                .whereEqualTo("id",drugId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String docId = documentSnapshot.getId();
                    db.collection("Drugs").document(docId)
                            .set(drugsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(DrugDetailsActivity.this, "Updated Successfully!!", Toast.LENGTH_SHORT).show();
                            isDataChanges = false;
                            WorkManager.getInstance().cancelUniqueWork(drugId);
                            requests=new ArrayList<>();
                            setWorkManager();
                            WorkManager.getInstance().enqueueUniqueWork(drugId, ExistingWorkPolicy.REPLACE,requests);
                            userDrugs.remove(drugPos);
                            userDrugs.add(drugsModel);
                            drugsAdapter1.setDrugs(userDrugs);
                            drugsAdapter1.notifyDataSetChanged();
                            Intent intent = new Intent(DrugDetailsActivity.this,HomeActivity.class);
                            startActivity(intent);
                            repo.updateDrug(drugsModel);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DrugDetailsActivity.this, "Updated Faild..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(DrugDetailsActivity.this, "Faild..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void setWorkManager(){
        List<String>times= drugsModel.getTimes();
        String endDate= drugsModel.getEndDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new Date();
        Date d2 = null;
        try {
           // d1 = simpleDateFormat.parse(d1);
            d2 = simpleDateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differenceInTime = d2.getTime() - d1.getTime();
        long diffInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
        Log.i("TAG","Days: "+diffInDays);
        //Log.i("Tagg",String.valueOf(diffInDays));
        for(int i=0;i<times.size();i++){
            List<Integer>myTimes=convertTime(times.get(i));
            if(days.size()!=0){
                execute(myTimes.get(0),myTimes.get(1));
            }else{
                Calendar today=Calendar.getInstance();
                Calendar cStartDate= Calendar.getInstance();
                int year= today.get(Calendar.YEAR);
                int month=today.get(Calendar.MONTH);
                int day=today.get(Calendar.DAY_OF_MONTH);
                cStartDate.set(year,month+1,day,myTimes.get(0),myTimes.get(1));
                long diffInMinutes=((cStartDate.getTimeInMillis()-today.getTimeInMillis())/60000);
                diffInMinutes=diffInMinutes-44640;
                Data inputData  = new Data.Builder()
                        .putString("medId",drugsModel.getId())
                        .putString("medName",drugsModel.getName())
                        .putString("medStrength",drugsModel.getStrength())
                        .putInt("medPill",drugsModel.getNumOfPills())
                        .build();

                OneTimeWorkRequest workRequest= new  OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
                        .setInputData(inputData).build();
                if(diffInMinutes>0){
                    requests.add(workRequest);
                    Log.i("Tagg","done");
                }
                for(int z=1;z<=diffInDays+1;z++){
                    Long duration=Math.abs(diffInMinutes+1440*z);
                    workRequest=new OneTimeWorkRequest.Builder(ReminderWorker.class)
                            .setInitialDelay(duration, TimeUnit.MINUTES)
                            .setInputData(inputData).build();
                    requests.add(workRequest);
                }
            }
        }
    }
    void execute(int hour,int minute){
        Format f = new SimpleDateFormat("EEEE");
        Date dt = new Date();
        Calendar today=Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        Calendar cs=Calendar.getInstance();
        String endDate = AddDrugActivity.drugsModel.getEndDate();
        Date endDatee = null;
        try {
            endDatee=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
        for(int i=1;i<=7;i++){
            String compareDate=sdformat.format(dt);
            //int compare=endDatee.compareTo(dt);
            // Log.i("Tagg",String.valueOf(dt));
            if(endDatee.after(dt)||endDate.equals(compareDate)){
                Log.i("Tagg","done");

                String str = f.format(dt);
                if(days.contains(str)){
                    Log.i("Tagg",str);
                    Calendar n=Calendar.getInstance();
                    n.setTime(dt);
                    int year= n.get(Calendar.YEAR);
                    int month=n.get(Calendar.MONTH);
                    int day=n.get(Calendar.DAY_OF_MONTH);
                    cs.set(year,month+1,day,hour,minute);
                    Log.i("Tagg","year"+year+"month"+(month+1)+"day"+day);
                    long diffInMinutes=((cs.getTimeInMillis()-today.getTimeInMillis())/60000);
                    diffInMinutes=diffInMinutes-44640;
                    Log.i("Tagg",String.valueOf(diffInMinutes));
                    Data inputData  = new Data.Builder()
                            .putString("medId",drugsModel.getId())
                            .putString("medName",drugsModel.getName())
                            .putString("medStrength",drugsModel.getStrength())
                            .putInt("medPill",drugsModel.getNumOfPills())
                            .build();
                    OneTimeWorkRequest workRequest= new  OneTimeWorkRequest.Builder(ReminderWorker.class)
                            .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
                            .setInputData(inputData).build();
                    if(diffInMinutes>0){
                        requests.add(workRequest);
                        //Log.i("Tagg","done");
                    }
                }
                c.add(Calendar.DATE, 1);
                dt = c.getTime();
            }
        }
    }
    List<Integer> convertTime(String time){
        String[] stringTimes=time.split(":");
        List<Integer>times=new ArrayList<>();
        times.add(Integer.parseInt(stringTimes[0]));
        times.add(Integer.parseInt(stringTimes[1]));
        return  times;
    }
    private void displayDrugData(){
        Bundle drugDetails = getIntent().getExtras();
        if(drugDetails!= null){
            drugsModel = (DrugsModel) drugDetails.getSerializable("drugDetails");
             drugPos    = drugDetails.getInt("drugPosition");
            Log.i("TAG1",drugsModel.getName());
            if(drugsModel.getType().equals("Pill")){
                cardDrugRefill.setVisibility(View.VISIBLE);
            }
            else{
                cardDrugRefill.setVisibility(View.INVISIBLE);
            }
            textViewDrugRefillTime.append(" "+drugsModel.getRefillReminderTime());
            textViewDrugCurrentPillNum.append(" "+drugsModel.getCurrentNumOfPills());
            textViewDrugReminderPillNum.append(" "+drugsModel.getNumOfRefillReminder());
            textDrugNameText.setText(drugsModel.getName());
            textDrugStrength.setText(drugsModel.getStrength());
            textDrugInstruction.setText(drugsModel.getInstructions());
            imgViewDrugIcone.setImageResource(drugsModel.getIcon());
            textViewDrugReson.setText(drugsModel.getReasons());
            textViewDrugStartDate.append(" "+drugsModel.getStartDate());
            textViewDrugEndDate.append(" "+drugsModel.getEndDate());
            txtDrugReminerCounts.setText(drugsModel.getNumberOfTimes());
            List drugTimes= drugsModel.getTimes();
            for(int i=0;i<drugTimes.size();i++) {
                txtDrugReminerTimes.append("\n" +drugTimes.get(i).toString()+"\n");
            }
            if(drugsModel.getDaysOrMonthsselected().size()!=0){
                for(int i=0;i<drugsModel.getDaysOrMonthsselected().size();i++) {
                    txtDrugReminerDays.append(drugsModel.getDaysOrMonthsselected().get(i)+" , ");
                }
            }
            else{
                txtDrugReminerDays.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void intializeVariables(){
        // Intialize Variables
        db = FirebaseFirestore.getInstance();
        editDayArray = getResources().getStringArray(R.array.dayArray);
        editSelectedDay = new boolean[editDayArray.length];
        progressDialog = new ProgressDialog(this);
        /// Update Dialog
        dialog = new Dialog(DrugDetailsActivity.this);
        dialog.setContentView(R.layout.comfirm_update_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        btnCancelUpdate = dialog.findViewById(R.id.btnCancelUpdate);
        btnConfirmUpdate = dialog.findViewById(R.id.btnConfirmUpdate);
        // Delete Dialog
        deleteDialog = new Dialog(DrugDetailsActivity.this);
        deleteDialog.setContentView(R.layout.comfirm_delete_dialog);
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.setCancelable(false);
        btnConfirmDelete  = deleteDialog.findViewById(R.id.btnConfirmDelete);
        btnCancelDelete = deleteDialog.findViewById(R.id.btnCancelDelete);
    }
    private void intialization() {
        // Tool bar
        imgViewEditBack = findViewById(R.id.img_view_edit_back);
        txtDisplayTitle = findViewById(R.id.txtDisplayTitle);
        imgViewDeleteDrug = findViewById(R.id.imgViewDeleteDrug);
        // Drug Name Intialization
        hiddenView1 = findViewById(R.id.hidden_view1);
        arrowButton1 = findViewById(R.id.arrow_button1);
        cardDrugName = findViewById(R.id.cardDrugName);
        textDrugNameText = findViewById(R.id.text_view_drug_name);
        editTexteditDrugName = findViewById(R.id.editTexteditDrugName);
        // Drug Strengths Intialization
        hiddenView2 = findViewById(R.id.hidden_view2);
        arrowButton2 = findViewById(R.id.arrow_button2);
        cardDrugStrength = findViewById(R.id.cardDrugStrength);
        textDrugStrength = findViewById(R.id.text_view_drug_strength);
        editTextEditDrugStrength = findViewById(R.id.editTextEditDrugStrength);
        // Drug Reminder Times Intialization
        hiddenView8 = findViewById(R.id.hidden_view8);
        arrowButton8 = findViewById(R.id.arrow_button8);
        cardDrugReminders = findViewById(R.id.cardDrugReminders);
        txtDrugReminerTimes = findViewById(R.id.txt_drug_reminer_times);
        txtDrugReminerCounts = findViewById(R.id.txt_drug_reminer_counts);
        txtDrugReminerDays = findViewById(R.id.txt_drug_reminer_days);
        recyclerEditDrugTimes = findViewById(R.id.recyclerEditDrugTimes);
        ////// txtEditSelectDays = findViewById(R.id.txtEditSelectDays);
        radioGroupEditOptions = findViewById(R.id.radioGroupEditOptions);
        editRadioPerDay = findViewById(R.id.editRadioPerDay);
        editaRadioPerWeek = findViewById(R.id.editaRadioPerWeek);
        editTextEditDrugNumOfTimes = findViewById(R.id.editTextEditDrugNumOfTimes);
        // Drug Schedual Intialization
        hiddenView3 = findViewById(R.id.hidden_view3);
        arrowButton3 = findViewById(R.id.arrow_button3);
        cardDrugSchedual = findViewById(R.id.cardDrugSchedual);
        textViewDrugStartDate = findViewById(R.id.text_view_drug_start_date);
        textViewDrugEndDate = findViewById(R.id.text_view_drug_end_date);
        textVieweditDrugSchedualEndDate = findViewById(R.id.textViewEditDrugSchedualEndDate);
        // Drug Instruction Intialization
        hiddenView4 = findViewById(R.id.hidden_view4);
        arrowButton4 = findViewById(R.id.arrow_button4);
        cardDrugInstruction = findViewById(R.id.cardDrugInstruction);
        textDrugInstruction = findViewById(R.id.text_view_drug_instruction);
        editTextEditDrugInstruction = findViewById(R.id.editTextEditDrugInstruction);
        // Drug Icon Intialization
        hiddenView5 = findViewById(R.id.hidden_view5);
        arrowButton5 = findViewById(R.id.arrow_button5);
        cardDrugIcon = findViewById(R.id.cardDrugIcon);
        imgViewDrugIcone = findViewById(R.id.img_view_drug_icone);
        // Icons Intialization
        imgPill = findViewById(R.id.imgPill);
        imgDrops = findViewById(R.id.imgDrops);
        imgInhaler = findViewById(R.id.imgInhaler);
        imgPowder = findViewById(R.id.imgPowder);
        imgInjection = findViewById(R.id.imgInjection);
        imgTemp = findViewById(R.id.imgTemp);
        imgOther = findViewById(R.id.imgOther);
        // Drug Refill Intialization
        hiddenView6 = findViewById(R.id.hidden_view6);
        arrowButton6 = findViewById(R.id.arrow_button6);
        cardDrugRefill = findViewById(R.id.cardDrugRefill);
        textViewDrugCurrentPillNum = findViewById(R.id.text_view_drug_current_pill_num);
        textViewDrugReminderPillNum = findViewById(R.id.text_view_drug_reminder_pill_num);
        textViewDrugRefillTime = findViewById(R.id.text_view_drug_refill_time);
        editTextEditDrugCurrentPillNum = findViewById(R.id.editTextEditDrugCurrentPillNum);
        editTextEditDrugReminderNum = findViewById(R.id.editTextEditDrugReminderNum);
        // Drug Reson Intialization
        hiddenView7 = findViewById(R.id.hidden_view7);
        arrowButton7 = findViewById(R.id.arrow_button7);
        cardDrugReson = findViewById(R.id.cardDrugReson);
        textViewDrugReson = findViewById(R.id.text_view_drug_reson);
        editTextEditDrugReson = findViewById(R.id.editTextEditDrugReson);
        // Button Update
        btnUpdateDrugData = findViewById(R.id.btnUpdateDrugData);


    }
    private void setCardDrugName(){
        if (hiddenView1.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugName, new AutoTransition());
            textDrugNameText.setVisibility(View.VISIBLE);
            hiddenView1.setVisibility(View.GONE);
            arrowButton1.setImageResource(R.drawable.ic_expand_more);
        }
        else {
            TransitionManager.beginDelayedTransition(cardDrugName, new AutoTransition());
            hiddenView1.setVisibility(View.VISIBLE);
            textDrugNameText.setVisibility(View.INVISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton1.setImageResource(R.drawable.ic_expand_less);
        }
    }
    private  void setCardDrugStrength(){
        if (hiddenView2.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugStrength, new AutoTransition());
            textDrugStrength.setVisibility(View.VISIBLE);
            hiddenView2.setVisibility(View.GONE);
            arrowButton2.setImageResource(R.drawable.ic_expand_more);
        }
        else{
            hiddenView2.setVisibility(View.VISIBLE);
            textDrugStrength.setVisibility(View.INVISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton2.setImageResource(R.drawable.ic_expand_less);
        }
    }
    private void setCardDrugRemindersTimes(){
        if(!(appCurrentUser.getId().equals(drugsModel.getUserId()))){
        }
        else{
            if (hiddenView8.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardDrugSchedual, new AutoTransition());
                txtDrugReminerTimes.setVisibility(View.VISIBLE);
                txtDrugReminerCounts.setVisibility(View.VISIBLE);
                txtDrugReminerDays.setVisibility(View.VISIBLE);
                hiddenView8.setVisibility(View.GONE);
                arrowButton8.setImageResource(R.drawable.ic_expand_more);
            }
            else {
                TransitionManager.beginDelayedTransition(cardDrugSchedual, new AutoTransition());
                hiddenView8.setVisibility(View.VISIBLE);
                txtDrugReminerTimes.setVisibility(View.INVISIBLE);
                txtDrugReminerCounts.setVisibility(View.INVISIBLE);
                txtDrugReminerDays.setVisibility(View.INVISIBLE);
                txtDisplayTitle.setText(R.string.edit_drug_info);
                arrowButton8.setImageResource(R.drawable.ic_expand_less);
            }
        }

    }
    private void setCardDrugSchedual(){
        if(!(appCurrentUser.getId().equals(drugsModel.getUserId()))){
        } else {
            if (hiddenView3.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardDrugSchedual, new AutoTransition());
                textViewDrugStartDate.setVisibility(View.VISIBLE);
                textViewDrugEndDate.setVisibility(View.VISIBLE);
                hiddenView3.setVisibility(View.GONE);
                arrowButton3.setImageResource(R.drawable.ic_expand_more);
            } else {
                TransitionManager.beginDelayedTransition(cardDrugSchedual, new AutoTransition());
                hiddenView3.setVisibility(View.VISIBLE);
                textViewDrugStartDate.setVisibility(View.INVISIBLE);
                textViewDrugEndDate.setVisibility(View.INVISIBLE);
                txtDisplayTitle.setText(R.string.edit_drug_info);
                arrowButton3.setImageResource(R.drawable.ic_expand_less);
            }
        }
    }
    private void setCardDrugInstruction(){
        if (hiddenView4.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugInstruction, new AutoTransition());
            textDrugInstruction.setVisibility(View.VISIBLE);
            hiddenView4.setVisibility(View.GONE);
            arrowButton4.setImageResource(R.drawable.ic_expand_more);
        }
        else{
            hiddenView4.setVisibility(View.VISIBLE);
            textDrugInstruction.setVisibility(View.INVISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton4.setImageResource(R.drawable.ic_expand_less);
        }
    }
    private void setCardDrugIcon(){
        if (hiddenView5.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugInstruction, new AutoTransition());
            imgViewDrugIcone.setVisibility(View.VISIBLE);
            hiddenView5.setVisibility(View.GONE);
            arrowButton5.setImageResource(R.drawable.ic_expand_more);
        }
        else{
            hiddenView5.setVisibility(View.VISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton5.setImageResource(R.drawable.ic_expand_less);
        }
    }
    private void setCardDrugRefill(){
        if (hiddenView6.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugInstruction, new AutoTransition());
            textViewDrugCurrentPillNum.setVisibility(View.VISIBLE);
            textViewDrugReminderPillNum.setVisibility(View.VISIBLE);
            textViewDrugRefillTime.setVisibility(View.VISIBLE);
            hiddenView6.setVisibility(View.GONE);
            arrowButton6.setImageResource(R.drawable.ic_expand_more);
        }
        else{
            hiddenView6.setVisibility(View.VISIBLE);
            textViewDrugCurrentPillNum.setVisibility(View.INVISIBLE);
            textViewDrugReminderPillNum.setVisibility(View.INVISIBLE);
            textViewDrugRefillTime.setVisibility(View.INVISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton6.setImageResource(R.drawable.ic_expand_less);
        }
    }
    private void setCardDrugReson(){
        if (hiddenView7.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardDrugReson, new AutoTransition());
            textViewDrugReson.setVisibility(View.VISIBLE);
            hiddenView7.setVisibility(View.GONE);
            arrowButton7.setImageResource(R.drawable.ic_expand_more);
        }
        else {
            TransitionManager.beginDelayedTransition(cardDrugName, new AutoTransition());
            hiddenView7.setVisibility(View.VISIBLE);
            textViewDrugReson.setVisibility(View.INVISIBLE);
            txtDisplayTitle.setText(R.string.edit_drug_info);
            arrowButton7.setImageResource(R.drawable.ic_expand_less);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDatePickDialogForEditingEndDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DrugDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                editDate = day+"/"+month+"/"+year;
                textVieweditDrugSchedualEndDate.setText(editDate);
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()-1000);
        datePickerDialog.show();
    }
    private void setRadioPerWeek(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DrugDetailsActivity.this);
        builder.setTitle(R.string.selectDays);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(editDayArray, editSelectedDay, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int postion, boolean isChecked) {
                if(isChecked){
                    if(! editDayList.contains(postion)){
                        editDayList.add(postion);
                    }
                    else{
                        editDayList.remove(postion);
                    }
                }
                else{
                    editDayList.remove(postion);
                }
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String item ="";
                for(int j=0;j<editDayList.size();j++){
                    item = item + editDayArray[editDayList.get(j)];
                    editDays.add(editDayArray[editDayList.get(j)]);
                    if(j != editDayList.size()-1){
                        item = item+ " & ";
                    }
                }
                setRecyclerViewEditDrugTimes();
                drugsModel.setDaysOrMonthsselected(editDays);
            }
        });
        builder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton(R.string.clearAll, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int k=0;k<editSelectedDay.length;k++){
                    editSelectedDay[k] = false;
                    editDayList.clear();
                    //txtEditSelectDays.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void setRecyclerViewEditDrugTimes(){
        int size = Integer.parseInt(editTextEditDrugNumOfTimes.getText().toString());
        editTimes=new ArrayList();
        recyclerEditDrugTimes.setHasFixedSize(true);
        for(int timecounter=1; timecounter<=size;timecounter++){
            editTimes.add("Select Time "+ timecounter);
        }

        UpdateReminderTimeAdapter adapter = new UpdateReminderTimeAdapter(getApplicationContext(), editTimes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerEditDrugTimes.setLayoutManager(layoutManager);
        recyclerEditDrugTimes.setAdapter(adapter);
    }
    private String getEditSelectedOption() {
        String optionSelected = "";
        switch (radioGroupEditOptions.getCheckedRadioButtonId()) {
            case R.id.editRadioPerDay:
                optionSelected = " Per Day";
                break;
            case R.id.editaRadioPerWeek:
                optionSelected = " Per Week";
                break;
        }
        return optionSelected;
    }
    private void deleteDrug(){
        drugId = drugsModel.getId();
        progressDialog.setTitle("Deleting Drug Data.");
        progressDialog.show();

        FirebaseFirestore.getInstance().collection("Drugs").document(drugId).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("delete","from_delete "+ drugsModel.getName());
                        progressDialog.dismiss();
                        WorkManager.getInstance().cancelUniqueWork(drugId);
                        userDrugs.remove(drugsModel);
                        drugsAdapter1.setDrugs(userDrugs);
                        drugsAdapter1.notifyDataSetChanged();
                        deleteDialog.dismiss();
                        Toast.makeText(DrugDetailsActivity.this, "Drug Deleted Successfully!!!", Toast.LENGTH_SHORT).show();
                        Intent goTOHome = new Intent(DrugDetailsActivity.this, HomeActivity.class);
                        startActivity(goTOHome);
                        appCurrentUser.getDrugsIds().remove(drugId);
                        userDocRef.document(appCurrentUser.getId()).set(appCurrentUser);
                        repo.deleteDrug(drugsModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DrugDetailsActivity.this, "Delete Failed..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearEditTexts(){
        editTexteditDrugName.setText("");
        editTextEditDrugStrength.setText("");
        editTextEditDrugInstruction.setText("");
        editTextEditDrugReson.setText("");
        editTexteditDrugName.setText("");
        editTextEditDrugCurrentPillNum.setText("");
        editTextEditDrugReminderNum.setText("");
        textVieweditDrugSchedualEndDate.setText("");
        editTextEditDrugNumOfTimes.setText("");
    }








}