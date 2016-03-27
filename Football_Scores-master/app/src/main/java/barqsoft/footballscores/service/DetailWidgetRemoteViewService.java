package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilies;

/**
 * Created by Afzal on 3/5/16.
 */
public class DetailWidgetRemoteViewService extends RemoteViewsService {

    public final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                if(data != null){
                    data.close();
                }

                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission



                final long identityToken = Binder.clearCallingIdentity();

                Uri matchDateQuery = DatabaseContract.scores_table.buildScoreWithDate();

                Date fragmentdate = new Date();
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                String todayDate=mformat.format(fragmentdate);

                Log.v("LOG_TAG", "Date we will search for is = " + todayDate);

                data = getContentResolver().query(matchDateQuery, null, null, new String[]{todayDate}, DatabaseContract.scores_table.TIME_COL + " ASC");
                Binder.restoreCallingIdentity(identityToken);


            }

            @Override
            public void onDestroy() {

                if(data != null){
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                                    data == null || !data.moveToPosition(position)) {
                            return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                                    R.layout.score_single_layout);

                int homescore = data.getInt(ScoresProvider.MATCH_HOME_GOALS);
                int awayscore = data.getInt(ScoresProvider.MATCH_AWAY_GOALS);

                String scores = Utilies.getScores(homescore, awayscore);
                String homeTeamName = data.getString(ScoresProvider.MATCH_HOME_TEAM);
                String awayTeamName = data.getString(ScoresProvider.MATCH_AWAY_TEAM);
                String matchTime = data.getString(ScoresProvider.MATCH_TIME);

                int homeTeamCrest = Utilies.getTeamCrestByTeamName(homeTeamName);
                int awayTeamCrest = Utilies.getTeamCrestByTeamName(awayTeamName);


                String description = getApplicationContext().getString(R.string.a11y_scores_readout, homeTeamName, awayTeamName, homescore, awayscore, matchTime);


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

                return views;

            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description){
                views.setContentDescription(R.id.widget, description);
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.score_single_layout);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {

                if(data.moveToPosition(position))
                    return data.getLong(ScoresProvider.MATCH_ID);

                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }


}
