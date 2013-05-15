package com.example.vectorial_renderer;

import junit.framework.Assert;
import sword.android.vectorial_renderer.Color;
import sword.android.vectorial_renderer.RegionRenderer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;

    private static final PointF points[][] = {
        { new PointF(0.0f, 1.0f), new PointF(0.2f, 1.0f) },
        { new PointF(0.0f, 0.0f), new PointF(0.2f, 0.2f) },
        { new PointF(1.0f, 0.0f), new PointF(1.0f, 0.2f) }
    };

    private static final Color colors[][] = {
        { new Color(1.0f, 0.0f, 1.0f, 1.0f) },
        { new Color(1.0f, 1.0f, 0.0f, 0.0f) }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final int uPointCount = points.length;
        final int vPointCount = points[0].length;

        for(int i=1; i<uPointCount; i++) {
            Assert.assertEquals(vPointCount, points[i].length);
        }

        Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);
        new RegionRenderer().draw(bitmap, points, colors);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        ((ImageView)findViewById(R.id.mainImageView)).setImageDrawable(drawable);
    }
}
