package com.bmh.githubrepository.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bmh.githubrepository.Model.Repo;
import com.bmh.githubrepository.Tools.Notifications;
import com.bmh.githubrepository.Tools.StaticValues;
import com.bmh.githubrepository.WebService.RepoAPI;
import com.bmh.githubrepository.WebService.RepoRetrofit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
* A service has a timer task that fires every 1 hour to load repositories
* we check the "created at" parameter of the repository and compare it with the current date
* to determine if it is new repository added or not
* if it is new repository
* send notification
* */
public class RefreshService extends Service {

    Timer timer = new Timer();
    static String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    Date currentDate;
    String strCurrentDate = "";

    @Override
    public void onCreate() {
        super.onCreate();
        getStringCurrentDate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RepoAPI repoAPI = new RepoRetrofit(getApplicationContext()).getRepoAPI();
                //load all repositories
                Call<List<Repo>> call = repoAPI.loadRepo(StaticValues.ACCESS_TOKEN);
                call.enqueue(new Callback<List<Repo>>() {

                    @Override
                    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                        if (response.isSuccessful()) {
                            List<Repo> repoList = response.body();
                            for (Repo repo : repoList) {
                                // repository Created At is Greater than currentDate
                                //that means it is a new repository added,so we will notify user
                                if (parseDate(strCurrentDate).compareTo(parseDate(repo.getCreatedAt())) < 0) {
                                    setStringCurrentDate(repo.getCreatedAt());
                                    new Notifications(getApplicationContext()).sendNotification();
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Repo>> call, Throwable t) {

                    }
                });
            }
        }, 0, 1000 * 60 * 60);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getStringCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        if (strCurrentDate.equals("")) {
            strCurrentDate = new SimpleDateFormat(DATE_FORMAT_PATTERN).format(currentDate);
            return strCurrentDate;
        }
        return strCurrentDate;
    }

    private void setStringCurrentDate(String strCurrentDate) {
        this.strCurrentDate = strCurrentDate;
    }

    public static Date parseDate(String stringToParse) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(stringToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
