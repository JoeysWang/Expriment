package com.joeys.router.runtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;

public class ActivityBuilder {
    public static final ActivityBuilder INSTANCE = new ActivityBuilder();

    public void startActivity(Context context, Intent intent) {

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

    }
}
