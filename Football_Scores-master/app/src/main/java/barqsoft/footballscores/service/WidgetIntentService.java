package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilies;

/**
 * Created by Afzal on 2/20/16.
 */
public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("WidgetIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoreAppWidgetProvider.class));

        Uri matchDateQuery = DatabaseContract.scores_table.buildScoreWithDate();

        Date fragmentdate = new Date();
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate=mformat.format(fragmentdate);


        Cursor data = getContentResolver().query(matchDateQuery, null, null, new String[]{todayDate}, DatabaseContract.scores_table.TIME_COL + " ASC");


        int homescore = -1;
        int awayscore = -1;
        String homeTeamName = null;
        String awayTeamName = null;
        String matchTime = null;
        String scores = null;

        int homeTeamCrest = -1;
        int awayTeamCrest =-1;


        if(data != null && data.moveToFirst()) {
            homescore = data.getInt(ScoresProvider.MATCH_HOME_GOALS);
            awayscore = data.getInt(ScoresProvider.MATCH_AWAY_GOALS);
            scores = Utilies.getScores(homescore, awayscore);
            homeTeamName = data.getString(ScoresProvider.MATCH_HOME_TEAM);
             awayTeamName = data.getString(ScoresProvider.MATCH_AWAY_TEAM);
             matchTime = data.getString(ScoresProvider.MATCH_TIME);

             homeTeamCrest = Utilies.getTeamCrestByTeamName(homeTeamName);
             awayTeamCrest = Utilies.getTeamCrestByTeamName(awayTeamName);



        }
        data.close();

        String description = getApplicationContext().getString(R.string.a11y_scores_readout, homeTeamName, awayTeamName, homescore, awayscore, matchTime);

        for(int appWidgetId: appWidgetIds){


            int layoutId = R.layout.score_single_layout;

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                if(homescore == -1)
                    description = "No Data Available";
                setRemoteContentDescription(views, description);
            }

            views.setImageViewResource(R.id.home_crest, homeTeamCrest);
            views.setImageViewResource(R.id.away_crest, awayTeamCrest);
            views.setTextViewText(R.id.home_name, homeTeamName);
            views.setTextViewText(R.id.away_name, awayTeamName);
            views.setTextViewText(R.id.data_textview, matchTime);
            views.setTextViewText(R.id.score_textview, scores);



            views.setEmptyView(layoutId, R.id.empty_view);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, launchIntent,0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }


//        while(data.moveToNext()){
//
//            for(int i = 0; i< data.getColumnCount(); i++){
//
//                Log.v("RET_DATA", data.getColumnName(i) + " is  " + data.getString(i));
//
//            }
//            Log.v("LOG_TAG", "Row Completed");
//
//
//        }




//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.score_single_layout);
//        appWidgetManager.updateAppWidget(appWidgetId,views);

    }


    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            return getResources().getDimensionPixelSize(R.dimen.score_single_default_width);
        }

        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId){

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if(options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)){
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp, displayMetrics);
        }

        return getResources().getDimensionPixelSize(R.dimen.score_single_default_width);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description){
        views.setContentDescription(R.id.widget, description);
    }


}
