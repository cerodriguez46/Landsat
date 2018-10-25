package christopher.landsat3;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import christopher.landsat3.Networking.LandsatModel;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory {

        ArrayList<LandsatModel> satelliteList = new ArrayList<>();

        LandsatModel model = null;

        private Context mContext;
        private int appWidgetId;

        public WidgetItemFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return satelliteList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.landsat_widget);
            //remoteViews.setImageViewResource(R.drawable.placeholder, satelliteList[i]);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
