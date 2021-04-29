package com.facedetectionusingcamera.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic
{
    private int rect_color = Color.CYAN;
    private Paint paint;
    private GraphicOverlay graphicOverlay;
    private Rect get_rect;

    public RectOverlay(GraphicOverlay overlay , Rect rect)
    {
        super(overlay);

        paint = new Paint();
        paint.setColor(rect_color);
        paint.setStyle(Paint.Style.STROKE);

        graphicOverlay = overlay;
        get_rect = rect;

        postInvalidate();

    }

    @Override
    public void draw(Canvas canvas)
    {
        RectF rectF = new RectF(get_rect);
        rectF.top = translateX(rectF.top);
        rectF.bottom = translateX(rectF.bottom);
        rectF.right = translateX(rectF.right);
        rectF.left = translateX(rectF.left);

        canvas.drawRect(rectF , paint);
    }
}
