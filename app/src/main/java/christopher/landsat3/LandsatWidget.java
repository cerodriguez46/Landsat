package christopher.landsat3;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;

import java.util.ArrayList;

import christopher.landsat3.Networking.LandsatModel;

/**
 * Implementation of App Widget functionality.
 */
public class LandsatWidget extends AppWidgetProvider {

    static ArrayList<LandsatModel> satImageArrayList;

    LandsatModel model;

    //String image = model.url;
    private AppWidgetTarget appWidgetTarget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.landsat_widget);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);


        }
    }
}


