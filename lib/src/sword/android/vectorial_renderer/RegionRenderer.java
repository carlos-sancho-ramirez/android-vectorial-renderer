package sword.android.vectorial_renderer;

import junit.framework.Assert;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class RegionRenderer {

    private Bitmap mBitmap;
    private PointF[][] mPoints;
    private Color[][] mColors;

    private int factorial(int n) {
        Assert.assertTrue( n >= 0 );

        if (n == 0 || n == 1) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    private float power(float number, int exponent) {
        Assert.assertTrue( exponent >= 0);

        if (exponent == 0) {
            return 1;
        }

        return number * power(number, exponent - 1);
    }

    private PointF getXYFromUV(float u, float v) {
        final int uOrder = mPoints.length - 1;
        final int vOrder = mPoints[0].length - 1;

        PointF result = new PointF();
        for(int uCounter=0; uCounter<=uOrder; uCounter++) {
            float uCoeff = (float)factorial(uOrder) / (float)(factorial(uCounter) * factorial(uOrder - uCounter));
            uCoeff *= power(u, uCounter) * power(1 - u, uOrder - uCounter);

            for(int vCounter=0; vCounter<=vOrder; vCounter++) {
                float vCoeff = (float)factorial(vOrder) / (float)(factorial(vCounter) * factorial(vOrder - vCounter));
                vCoeff *= power(v, vCounter) * power(1 - v, vOrder - vCounter);

                float coeff = uCoeff * vCoeff;
                PointF point = mPoints[uCounter][vCounter];

                result.x += coeff * point.x;
                result.y += coeff * point.y;
            }
        }

        return result;
    }

    private Color getColorFromUV(float u, float v) {
        final int uOrder = mColors.length - 1;
        final int vOrder = mColors[0].length - 1;

        Color result = new Color();
        for(int uCounter=0; uCounter<=uOrder; uCounter++) {
            float uCoeff = (float)factorial(uOrder) / (float)(factorial(uCounter) * factorial(uOrder - uCounter));
            uCoeff *= power(u, uCounter) * power(1 - u, uOrder - uCounter);

            for(int vCounter=0; vCounter<=vOrder; vCounter++) {
                float vCoeff = (float)factorial(vOrder) / (float)(factorial(vCounter) * factorial(vOrder - vCounter));
                vCoeff *= power(v, vCounter) * power(1 - v, vOrder - vCounter);

                float coeff = uCoeff * vCoeff;
                Color color = mColors[uCounter][vCounter];

                final int numberOfComponents = color.getComponentCount();
                for(int component=0; component< numberOfComponents; component++) {
                    result.setComponentWithoutBoundsAt(component, result.getComponentAt(component)
                            + color.getComponentAt(component) * coeff);
                }
            }
        }

        result.saturateColorComponents();
        return result;
    }

    private boolean areCloseEnough(Point a, Point b) {
        return Math.abs(a.x - b.x) <= 1 && Math.abs(a.y - b.y) <= 1;
    }

    private void drawUV(RectF rect) {

        PointF[] uvPointsF = new PointF[] {
            new PointF(rect.left, rect.top),
            new PointF(rect.right, rect.top),
            new PointF(rect.left, rect.bottom),
            new PointF(rect.right, rect.bottom)
        };

        PointF[] xyPointsF = new PointF[uvPointsF.length];
        Color[] xyColors = new Color[uvPointsF.length];
        Point[] xyPoints = new Point[uvPointsF.length];

        for (int i=0; i<uvPointsF.length; i++) {
            xyPointsF[i] = getXYFromUV(uvPointsF[i].x, uvPointsF[i].y);
            xyColors[i] = getColorFromUV(uvPointsF[i].x, uvPointsF[i].y);
            xyPoints[i] = new Point(Math.round(xyPointsF[i].x * mBitmap.getWidth()),
                    Math.round(xyPointsF[i].y * mBitmap.getHeight()));
        }

        if (areCloseEnough(xyPoints[0], xyPoints[1]) &&
            areCloseEnough(xyPoints[0], xyPoints[2]) &&
            areCloseEnough(xyPoints[0], xyPoints[3]) &&
            areCloseEnough(xyPoints[1], xyPoints[2]) &&
            areCloseEnough(xyPoints[1], xyPoints[3]) &&
            areCloseEnough(xyPoints[2], xyPoints[3])) {

            for(int i=0; i<4; i++) {
                try {
                    Color color = xyColors[i];

                    int nativeColor = (int)(color.getComponentAt(Color.COMPONENT_ALPHA) * 0xFF);
                    nativeColor = (nativeColor << 8) + (int)(color.getComponentAt(Color.COMPONENT_RED) * 0xFF);
                    nativeColor = (nativeColor << 8) + (int)(color.getComponentAt(Color.COMPONENT_GREEN) * 0xFF);
                    nativeColor = (nativeColor << 8) + (int)(color.getComponentAt(Color.COMPONENT_BLUE) * 0xFF);

                    mBitmap.setPixel(xyPoints[i].x, xyPoints[i].y, nativeColor);
                }catch(IllegalArgumentException exception) { }
            }

        }else{
            drawUV(new RectF(rect.left, rect.top,
                    (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2));
            drawUV(new RectF((rect.left + rect.right) / 2, rect.top,
                    rect.right, (rect.top + rect.bottom) / 2));
            drawUV(new RectF(rect.left, (rect.top + rect.bottom) / 2,
                    (rect.left + rect.right) / 2, rect.bottom));
            drawUV(new RectF((rect.left + rect.right) / 2,
                    (rect.top + rect.bottom) / 2, rect.right, rect.bottom));
        }
    }

    public void draw(Bitmap bitmap, PointF[][] points, Color[][] colors) {
        mBitmap = bitmap;
        mPoints = points;
        mColors = colors;

        drawUV(new RectF(0.0f, 0.0f, 1.0f, 1.0f));

        mColors = null;
        mPoints = null;
        mBitmap = null;
    }
}
