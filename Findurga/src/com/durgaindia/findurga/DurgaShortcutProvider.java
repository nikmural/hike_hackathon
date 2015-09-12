package com.durgaindia.findurga;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class DurgaShortcutProvider extends AppWidgetProvider{

	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i("Sonika", "Inside onUpdate initial launch");
			for (int widgetId : appWidgetIds) {
				RemoteViews remoteViews;
					remoteViews = new RemoteViews(context.getPackageName(),
							R.layout.init_layout_small);
					Intent refreshIntent = new Intent(context, DurgaLocationService.class);
					refreshIntent.putExtra("widget",0);
					PendingIntent refreshPendingIntent = PendingIntent.getService(context,
							0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					remoteViews.setOnClickPendingIntent(R.id.init_img,
							refreshPendingIntent);
					appWidgetManager.updateAppWidget(widgetId, remoteViews);
			}
			
			
	}
	
}
