package com.example.mediacalremider.drugs.add_drugs.view;

import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.requests;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugThirdFragment.days;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;

import com.example.mediacalremider.R;
import com.example.mediacalremider.notifications.ReminderWorker;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UpdateReminderTimeAdapter extends RecyclerView.Adapter<UpdateReminderTimeAdapter.ViewHolder>{

    Context context;
    List timesArray;
    int hour, minute;
    String localTime;
    public static List<String> timesSelected;
    public UpdateReminderTimeAdapter(Context context, List timesArray){
        this.context = context;
        this.timesArray = timesArray;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public UpdateReminderTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.row_times_of_drugs,parent,false);
        UpdateReminderTimeAdapter.ViewHolder viewHolder = new UpdateReminderTimeAdapter.ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull UpdateReminderTimeAdapter.ViewHolder holder, int position) {
        holder.getTxtReminderTimes().setText(timesArray.get(position).toString());
    }
    @Override
    public int getItemCount() {
        return timesArray.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        View row;
        TextView txtReminderTimes;
        @RequiresApi(api = Build.VERSION_CODES.N)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView;
            txtReminderTimes = row.findViewById(R.id.txtReminderTimes);
            timesSelected = new ArrayList<String>();
            txtReminderTimes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getRow().getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                            hour = hourOfDay;
                            minute = minutes;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0,0,0,hour,minute);
                            String time = hour + ":" +minute;
                            timesSelected.add(time);
                            txtReminderTimes.setText(DateFormat.format("hh:mm aa", calendar));


                        }
                    },12,0,false);
                    timePickerDialog.updateTime(hour,minute);
                    timePickerDialog.show();
                }
            });
        }
        public View getRow() {
            return row;
        }
        public TextView getTxtReminderTimes() {
            return txtReminderTimes;
        }
    }







}
