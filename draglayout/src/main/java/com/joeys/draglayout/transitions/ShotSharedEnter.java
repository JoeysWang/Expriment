/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joeys.draglayout.transitions;

import android.content.Context;
import android.graphics.Rect;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;

/**
 * Shared element transitions do not seem to like transitioning from a single view to two separate
 * views so we need to alter the ChangeBounds transition to compensate
 */
public class ShotSharedEnter extends ChangeBounds {

    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";

    public ShotSharedEnter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureEndValues(TransitionValues values) {
        super.captureEndValues(values);

        View view = values.view;

        if (view.isLaidOut() || view.getWidth() != 0 || view.getHeight() != 0) {
            values.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(),
                    view.getRight(), view.getBottom()));

        }

//        int width = ((View) values.values.get(PROPNAME_PARENT)).getWidth();
//        int height = ((View) values.values.get(PROPNAME_PARENT)).getHeight();
//        Rect bounds = (Rect) values.values.get(PROPNAME_BOUNDS);
//        bounds.right = width;
//        bounds.bottom =height;
//        values.values.put(PROPNAME_BOUNDS, bounds);
    }

}
