package com.example.mediacalremider.home;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.MainActivity.userDependencies;
import static com.example.mediacalremider.MainActivity.userDrugs;
import static com.example.mediacalremider.users.UserController.userDocRef;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediacalremider.R;
import com.example.mediacalremider.dependency.DependencyDrugsActivity;
import com.example.mediacalremider.dependency.getDrugsDelegation;
import com.example.mediacalremider.drugs.DrugsAdapter;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity;
import com.example.mediacalremider.notifications.NotificationController;
import com.example.mediacalremider.notifications.ReminderNotification;
import com.example.mediacalremider.repository.DatabaseRepository;
import com.example.mediacalremider.requests.RequestListActivity;
import com.example.mediacalremider.users.AddDependency;
import com.example.mediacalremider.users.Delegation;
import com.example.mediacalremider.users.LoginActivity;
import com.example.mediacalremider.users.UserController;
import com.example.mediacalremider.users.UserModel;
import com.example.mediacalremider.users.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class HomeActivity extends AppCompatActivity implements  View.OnClickListener,NavigationView.OnNavigationItemSelectedListener, Delegation, getDrugsDelegation {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    static Dialog reminderDialog;
    ImageView home_icon;
    public static DrugsAdapter drugsAdapter1;
    List<DrugsModel> drug;
    List<DrugsModel> inActiveDrug;
    List<UserModel> dependency;
    FirebaseUser s;
    RecyclerView.LayoutManager lm;
    CircleImageView profileImage;
    TextView userName;
    Menu nav_Menu;
    FloatingActionButton add_new_drug;
    DatabaseRepository db;
    public static List<DrugsModel>todayIsDrugs;
    ReminderNotification notification;
    List<DrugsModel>showDrugs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        notification = new ReminderNotification(getApplicationContext());
        db=DatabaseRepository.getInstance(HomeActivity.this);
        reminderDialog= new Dialog(this);
        todayIsDrugs= new ArrayList<>();
        add_new_drug=findViewById(R.id.add_new_drug);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycler_view);
        home_icon = findViewById(R.id.home_icon);
        s = FirebaseAuth.getInstance().getCurrentUser();
        home_icon.setOnClickListener(this);
        dependency=new ArrayList<>();
        navigationView.setNavigationItemSelectedListener(this);
        drug = new ArrayList<>();
        inActiveDrug = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        drugsAdapter1 = new DrugsAdapter(drug,this);
        lm = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(drugsAdapter1);
        drugsAdapter1.notifyDataSetChanged();
        View headerView = navigationView.inflateHeaderView(R.layout.header);
        profileImage =  headerView.findViewById(R.id.header_user_profile_img);
        userName =  headerView.findViewById(R.id.header_user_name);
        nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.dependent_one).setVisible(false);
        nav_Menu.findItem(R.id.dependent_two).setVisible(false);
        reminderDialog.setContentView(R.layout.dailog_reminder);
        reminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView= reminderDialog.findViewById(R.id.drug_image);
        imageView.setImageResource(R.drawable.daughter);
        showData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(HomeActivity.this)){
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),20);
            }
        }
        if(userDrugs.size()==0){
            UserController.getDrugs(s.getUid(),this);
        }
        else{
            showData();
        }
        if(appCurrentUser!=null){
            profileImage.setImageResource(appCurrentUser.getIcon());
            userName.setText(appCurrentUser.getName());
            if(userDependencies.size()==0){
                UserController.getDependent(appCurrentUser,HomeActivity.this);
            }
            else{
                if(userDependencies.size()==1){
                    nav_Menu.findItem(R.id.dependent_one).setVisible(true);
                    nav_Menu.findItem(R.id.dependent_one).setTitle(userDependencies.get(0).getName());
                }
                else if(userDependencies.size()==2){
                    nav_Menu.findItem(R.id.dependent_one).setVisible(true);
                    nav_Menu.findItem(R.id.dependent_one).setTitle(userDependencies.get(0).getName());
                    nav_Menu.findItem(R.id.dependent_two).setVisible(true);
                    nav_Menu.findItem(R.id.dependent_two).setTitle(userDependencies.get(1).getName());
                }
            }

        }else{
            UserController.getUser(this);
        }
        add_new_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(HomeActivity.this, AddDrugActivity.class);
                startActivity(intent);
            }
        });
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        // get Time Now
        long timeNow = endDate.getTimeInMillis();
        // request Data from presenter

        endDate.add(Calendar.MONTH, 1);

        System.out.println(findViewById(R.id.calendarView).getId());
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(HomeActivity.this,R.id.calendarView)

                .range(startDate, endDate)

                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                showDrugs=new ArrayList<>();
                for(DrugsModel drug:userDrugs){
                    String startDate= drug.getStartDate();
                    String endDate= drug.getEndDate();
                    int[] allStartDates = splitMedDate(String.valueOf(startDate));
                    int[] allEndtDates = splitMedDate(String.valueOf(endDate));
                    Calendar sd=Calendar.getInstance();
                    sd.set(allStartDates[2],allStartDates[1],allStartDates[0]);
                    int year=date.get(Calendar.YEAR);
                    int moth=date.get(Calendar.MONTH);
                    int day=date.get(Calendar.DAY_OF_MONTH);
                    System.out.println(year/moth/day);
                    Log.i("hsvj",String.valueOf(year)+String.valueOf(moth+1)+day);
                        if(betweenDate(allStartDates[0],allStartDates[1],allStartDates[2],allEndtDates[0],allEndtDates[1],allEndtDates[2],day,moth+1,year)
                                &&equalDate(allStartDates[0],allStartDates[1],allStartDates[2],allEndtDates[0],allEndtDates[1],allEndtDates[2],day,moth+1,year)){
                            showDrugs.add(drug);
                        }
                }

                drugsAdapter1.setDrugs(showDrugs);
                drugsAdapter1.notifyDataSetChanged();

            }
            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_icon:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addHealthTacker:
                Intent intent= new Intent(HomeActivity.this, AddDependency.class);
                startActivity(intent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                nav_Menu.findItem(R.id.dependent_one).setVisible(false);
                nav_Menu.findItem(R.id.dependent_two).setVisible(false);
                userDrugs=new ArrayList<>();
                appCurrentUser=null;
                userDependencies=new ArrayList<>();
                Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("EXIT", true);
                startActivity(intent1);
                finish();
                break;
            case R.id.myRequests:
                Intent intent2 = new Intent(HomeActivity.this, RequestListActivity.class);
                startActivity(intent2);
                break;
            case R.id.profile:
                Intent intent3 = new Intent(HomeActivity.this, UserProfile.class);
                startActivity(intent3);
                break;
            case R.id.dependent_one:
                Intent intent4 = new Intent(HomeActivity.this, DependencyDrugsActivity.class);
                intent4.putExtra("name",  userDependencies.get(0).getName());
                intent4.putExtra("id",  userDependencies.get(0).getId());
                intent4.putExtra("img",  userDependencies.get(0).getIcon());
                startActivity(intent4);
                break;
            case R.id.dependent_two:
                Intent intent5 = new Intent(HomeActivity.this, DependencyDrugsActivity.class);
                intent5.putExtra("name",  userDependencies.get(1).getName());
                intent5.putExtra("id",  userDependencies.get(1).getId());
                intent5.putExtra("img",  userDependencies.get(1).getIcon());
                startActivity(intent5);
                break;
        }
        return true;
    }
    @Override
    protected void onStart() {
        if(userDependencies.size()==1){
            userDocRef.document(userDependencies.get(0).getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    UserModel userModel= value.toObject(UserModel.class);
                    String drugState=userModel.getDrugState();
                    if(drugState.equals("Missed")){
                        NotificationCompat.Builder builder = notification.getMedMissedChannelNotification();
                        notification.getManager().notify(3,builder.build());
                        UserModel dependent=userDependencies.get(0);
                        dependent.setDrugState("Taken");
                        userDocRef.document(userDependencies.get(0).getId()).set(dependent, SetOptions.merge());
                    }
                }
            });
        }
        else if(userDependencies.size()==2){
            userDocRef.document(userDependencies.get(0).getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    UserModel userModel= value.toObject(UserModel.class);
                    String drugState=userModel.getDrugState();
                    if(drugState.equals("Missed")){
                        NotificationCompat.Builder builder = notification.getMedMissedChannelNotification();
                        notification.getManager().notify(3,builder.build());
                        UserModel dependent=userDependencies.get(0);
                        dependent.setDrugState("Taken");
                        userDocRef.document(userDependencies.get(0).getId()).set(dependent, SetOptions.merge());
                    }
                }
            });
            userDocRef.document(userDependencies.get(1).getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    UserModel userModel= value.toObject(UserModel.class);
                    String drugState=userModel.getDrugState();
                    if(drugState.equals("Missed")){
                        NotificationCompat.Builder builder = notification.getMedMissedChannelNotification();
                        notification.getManager().notify(3,builder.build());
                        UserModel dependent=userDependencies.get(1);
                        dependent.setDrugState("Taken");
                        userDocRef.document(userDependencies.get(1).getId()).set(dependent, SetOptions.merge());
                    }
                }
            });
        }
        userDocRef.document(s.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.i("error",error.toString());
                    return;
                }
                if(value.exists()){
                    UserModel userModel= value.toObject(UserModel.class);
                    String state= userModel.getRequestState();
                    if(state.equals("Exist")){
                        NotificationCompat.Builder builder = notification.getRequestChannelNotification();
                        notification.getManager().notify(4,builder.build());

                    }
                }
            }
        });
        super.onStart();
    }


    @Override
    public void getUserSuccessListener() {
        profileImage.setImageResource(appCurrentUser.getIcon());
        userName.setText(appCurrentUser.getName());
        UserController.getDependent(appCurrentUser,HomeActivity.this);
    }

    @Override
    public void getDependencySuccessListener(List<UserModel>dependencies) {
        dependency=dependencies;
        if(dependencies.size()==1){
            nav_Menu.findItem(R.id.dependent_one).setVisible(true);
            nav_Menu.findItem(R.id.dependent_one).setTitle(dependencies.get(0).getName());
        }
        else if(dependencies.size()==2){
            nav_Menu.findItem(R.id.dependent_one).setVisible(true);
            nav_Menu.findItem(R.id.dependent_one).setTitle(dependencies.get(0).getName());
            nav_Menu.findItem(R.id.dependent_two).setVisible(true);
            nav_Menu.findItem(R.id.dependent_two).setTitle(dependencies.get(1).getName());
        }
    }

    @Override
    public void getDrugsSuccessfully(List<DrugsModel> drugs) {
        userDrugs=drugs;
        showData();
       // drugsAdapter1.setDrugs(drugs);
      //  drugsAdapter1.notifyDataSetChanged();
    }

    public int[] splitMedDate(String data) {
        String[] splitDate = data.split("/");
        int day = Integer.parseInt(splitDate[0]);

        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        int[] allDate = {day, month, year};
        return allDate;    }


    public boolean betweenDate(int sday, int smonth, int syear, int eday, int emonth, int eyear, int cday, int cmonth, int cyear) {
        boolean result = false;

        Date start = new Date(syear, smonth - 1, sday);  // -1 because months are from 0 to 11
        Date end = new Date(eyear, emonth - 1, eday);
        Date check = new Date(cyear, cmonth - 1, cday);
        if ((check.compareTo(start) > 0 && check.compareTo(end) < 0) || check.compareTo(start) == 0 || check.compareTo(end) == 0) {
            result = true;
        }
        return result;
    }

    public boolean equalDate(int sday, int smonth, int syear, int eday, int emonth, int eyear, int cday, int cmonth, int cyear) {
        boolean result = false;
        Date start = new Date(syear, smonth - 1, sday);  // -1 because months are from 0 to 11
        Date end = new Date(eyear, emonth - 1, eday);
        Date check = new Date(cyear, cmonth - 1, cday);

        if (!(check.compareTo(start) < 0) && !(check.compareTo(end) > 0)) {

            result = true;
        }
        return result;    }


        void showData(){
        showDrugs= new ArrayList<>();
            drugsAdapter1.setDrugs(showDrugs);
            drugsAdapter1.notifyDataSetChanged();
            for(DrugsModel drug:userDrugs){
                Log.i("dnk","iiiinside");
                Calendar date= Calendar.getInstance();
                String startDate= drug.getStartDate();
                String endDate= drug.getEndDate();
                int[] allStartDates = splitMedDate(String.valueOf(startDate));
                int[] allEndtDates = splitMedDate(String.valueOf(endDate));
                Calendar sd=Calendar.getInstance();
                sd.set(allStartDates[2],allStartDates[1],allStartDates[0]);
                int year=date.get(Calendar.YEAR);
                int moth=date.get(Calendar.MONTH);
                int day=date.get(Calendar.DAY_OF_MONTH);
                System.out.println(year/moth/day);
                Log.i("hsvj",String.valueOf(year)+String.valueOf(moth+1)+day);
                if(betweenDate(allStartDates[0],allStartDates[1],allStartDates[2],allEndtDates[0],allEndtDates[1],allEndtDates[2],day,moth+1,year)
                        &&equalDate(allStartDates[0],allStartDates[1],allStartDates[2],allEndtDates[0],allEndtDates[1],allEndtDates[2],day,moth+1,year)){
                    showDrugs.add(drug);
                }
                else{
                    inActiveDrug.add(drug);
                }

            }

            drugsAdapter1.setDrugs(showDrugs);
            drugsAdapter1.notifyDataSetChanged();
            for(DrugsModel d:inActiveDrug){
                d.setState("InActive");
                FirebaseFirestore.getInstance().collection("Drugs").document(d.getId()).set(d,SetOptions.merge());
            }
        }

}