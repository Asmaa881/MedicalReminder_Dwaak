package com.example.mediacalremider.drugs.add_drugs.view;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.drugsModel;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.requests;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugThirdFragment.days;
import static com.example.mediacalremider.home.HomeActivity.todayIsDrugs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;

import com.example.mediacalremider.R;
import com.example.mediacalremider.notifications.ReminderWorker;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReminderTimesAdapter extends RecyclerView.Adapter<ReminderTimesAdapter.ViewHolder> {
    Context context;
    List timesArray;
    int hour, minute;
    String localTime;
    public static List<String> timesSelected;
    public ReminderTimesAdapter(Context context, List timesArray){
        this.context = context;
        this.timesArray = timesArray;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.row_times_of_drugs,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                            if(days.size()!=0){
                                execute(hour,minute);
                            }else{
                                String startDate= AddDrugActivity.drugsModel.getStartDate();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String[] s=startDate.split("/");
                                Calendar today=Calendar.getInstance();
                                Calendar cStartDate= Calendar.getInstance();
                                String endDate = AddDrugActivity.drugsModel.getEndDate();
                                cStartDate.set(Integer.parseInt(s[2]),Integer.parseInt(s[1]),Integer.parseInt(s[0]),hour,minute);
                                long diffInMinutes=((cStartDate.getTimeInMillis()-today.getTimeInMillis())/60000);
                                diffInMinutes=diffInMinutes-44640;
                                Date d1 = null;
                                Date d2 = null;
                                try {
                                    d1 = simpleDateFormat.parse(startDate);
                                    d2 = simpleDateFormat.parse(endDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long differenceInTime = d2.getTime() - d1.getTime();
                                long diffInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
                               // Log.i("TAG","Days: "+diffInDays);
                                Log.i("TAG","diff In Minutes: " + diffInMinutes);
                                Log.i("Tagg","num of dayes"+String.valueOf(diffInDays));
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
                                    Log.i("Tagg","done");
                                }
                                for(int i=1;i<=diffInDays;i++){
                                    Long duration=Math.abs(diffInMinutes+1440*i);
                                    workRequest=new OneTimeWorkRequest.Builder(ReminderWorker.class)
                                            .setInitialDelay(duration, TimeUnit.MINUTES)
                                            .setInputData(inputData).build();
                                    requests.add(workRequest);
                                }
                            }
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
}
