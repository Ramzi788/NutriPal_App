package com.example.nutripal;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BmiScaleView extends View {
    private Paint paint;
    private float bmiValue;

    public BmiScaleView(Context context) {
        super(context);
        init(null, 0);
    }

    public BmiScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BmiScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Initialize any components that you need
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setBmiValue(float bmiValue) {
        this.bmiValue = bmiValue;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Define your colors
        int underweightColor = Color.GREEN;
        int normalColor = Color.BLUE;
        int overweightColor = Color.YELLOW;
        int obeseColor = Color.RED;


        float sectionWidth = getWidth() / 25f; // Assuming a scale from 15 to 40
        paint.setStrokeWidth(5f);


        paint.setColor(underweightColor);
        canvas.drawRect(0, getHeight() / 2f, sectionWidth * (18.5f - 15), getHeight() / 2f + 10f, paint);


        paint.setColor(normalColor);
        canvas.drawRect(sectionWidth * (18.5f - 15), getHeight() / 2f, sectionWidth * (25 - 15), getHeight() / 2f + 10f, paint);


        paint.setColor(overweightColor);
        canvas.drawRect(sectionWidth * (25 - 15), getHeight() / 2f, sectionWidth * (30 - 15), getHeight() / 2f + 10f, paint);


        paint.setColor(obeseColor);
        canvas.drawRect(sectionWidth * (30 - 15), getHeight() / 2f, getWidth(), getHeight() / 2f + 10f, paint);


        paint.setColor(Color.BLACK);
        float bmiIndicatorX = sectionWidth * (bmiValue - 15);
        canvas.drawLine(bmiIndicatorX, getHeight() / 4f, bmiIndicatorX, 3 * getHeight() / 4f, paint);
    }
}
