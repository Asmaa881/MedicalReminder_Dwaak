package com.example.mediacalremider.drugs.add_drugs.view;

import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.progressDialog1;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.add_drugs.presenter.AddDrugPresenter;
import com.example.mediacalremider.home.HomeActivity;
import com.example.mediacalremider.network.InternetConnectionChecker;
import com.example.mediacalremider.repository.DatabaseRepository;
import com.example.mediacalremider.repository.DrugDAO;

import java.util.Calendar;
import java.util.List;

public class AddDrugFifthFragment extends Fragment implements IView{
    private AddDrugPresenter presenter;
    TextView txtRefillReminderTime;
    Button btnSaveAddedDrug;
    EditText editTextPillsNum, editTextreminderNum;
    int hour, minute;
    int currentPillNum , refillReminderNum;
    String reminderTime;
    private String saveCurrentDate, saveCurrentTime, drugRandomKey;
    public AddDrugFifthFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_drug_fifth, container, false);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtRefillReminderTime = view.findViewById(R.id.txtRefillReminderTime);
        editTextPillsNum = view.findViewById(R.id.editTextPillsNum);
        editTextreminderNum = view.findViewById(R.id.editTextReminderNum);
        btnSaveAddedDrug = view.findViewById(R.id.btnSaveAddedDrug);
        presenter = new AddDrugPresenter(this);
        txtRefillReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        hour = hourOfDay;
                        minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, hour, minute);
                         reminderTime = (String) DateFormat.format("hh:mm aa", calendar);
                        txtRefillReminderTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(hour, minute);
                timePickerDialog.show();
            }
        });

        btnSaveAddedDrug.setOnClickListener(btnView -> {
            drugRandomKey = randomkey();
            refillReminderNum = Integer.valueOf(editTextreminderNum.getText().toString());
            currentPillNum = Integer.valueOf(editTextPillsNum.getText().toString());
            AddDrugActivity.drugsModel.setCurrentNumOfPills(currentPillNum);
            AddDrugActivity.drugsModel.setNumOfRefillReminder(refillReminderNum);
            AddDrugActivity.drugsModel.setRefillReminderTime(reminderTime);
            presenter.onClick(AddDrugActivity.drugsModel);
        });
    }

    private String randomkey(){
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        java.text.SimpleDateFormat currentTime = new java.text.SimpleDateFormat("HH:mm:ss a");  // 12 hours
        saveCurrentTime = currentTime.format(calendar.getTime());
        drugRandomKey = saveCurrentDate + saveCurrentTime;
        return drugRandomKey;
    }

    @Override
    public void goToHome(boolean result) {

        if(new InternetConnectionChecker(getContext()).isConnected()){
            if(result== true) {
                Toast.makeText(getContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), HomeActivity.class);

                startActivity(intent);

            }
            else{
                Toast.makeText(getContext(), "Data not saved.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(), "You are Offline!", Toast.LENGTH_SHORT).show();
        }
    }
}