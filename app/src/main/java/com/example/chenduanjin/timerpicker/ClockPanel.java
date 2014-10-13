package com.example.chenduanjin.timerpicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.content.Context;
/**
 * Created by chenduanjin on 10/9/14.
 */
public class ClockPanel extends View {

    enum ClockType{HOUR, MINUTE};

    public static final int AM = 1;
    public static final int PM = 2;

    Paint mPaint;
    float mRadiusPercent;//radius pecent of min(width, height) of the view
    float mInnerRadiusPercent;//inner radius to draw text
    float mTextSizeMultipler;//
    float mSelectedCircleRadiusMultipler;
    float mAMPMCircleRadiusMultipler;
    String[] minutesStr = null;
    int selectedNumber = 12;//which number is selected, 1-12 for hour, 0-59 for minute
    ClockType mType;


    int am_pm = AM;
    float mAmCircleRadius;
    float mAmXcenter;
    float mPmXcenter;
    float mAmPmYcenter;

    public ClockPanel(Context context) {
        super(context);
        init(context);
    }

    public ClockPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);

    }

    private void init(Context context) {

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);


        mTextSizeMultipler = Float.parseFloat(context.getResources().getString(R.string.text_size_multipler));
        mInnerRadiusPercent = Float.parseFloat(context.getResources().getString(R.string.circle_inner_radius_multipler));
        mRadiusPercent = Float.parseFloat(context.getResources().getString(R.string.circle_radius_multipler));
        mSelectedCircleRadiusMultipler = Float.parseFloat(context.getResources().getString(R.string.text_selection_circle_radius_multipler));
        mAMPMCircleRadiusMultipler = Float.parseFloat(context.getResources().getString(R.string.am_pm_circle_radius_multipler));
    }

    /**
     *
     * @param textArray, texts to be drawn around the circle
     * @param number, default value which is selected
     */
    public void initialize(String[] textArray, int number) {
        this.minutesStr = textArray;
        selectedNumber = number;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2 + 1;
        float centerY = height / 2;
        float radius = Math.min(width, height) / 2 * mRadiusPercent;
        //draw circle panel background
        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, radius, mPaint);
        //draw numbers
        drawNumbers(canvas, radius, centerX, centerY);
        //draw circle for selected number
        drawSelectionCircle(canvas, radius, centerX, centerY);
        //draw PM and AM circle
        drawAMPMCircle(canvas, radius, centerX, centerY);
    }

    public void setClockType(ClockType type) {
        mType = type;
    }

    public void setSelectedNumber(int number) {
        this.selectedNumber = number;
    }

    public void setAm_pm(int i) {
        this.am_pm = i;
    }

    public int touchesInAMOrPM(float eventx, float eventy) {

        eventx -= ((View)this.getParent()).getLeft();
        eventy -= ((View)this.getParent()).getTop();
        float distanceToCenterAM = (float)Math.sqrt((eventx - mAmXcenter) * (eventx - mAmXcenter) +
                (eventy - mAmPmYcenter) * (eventy - mAmPmYcenter));
        if (distanceToCenterAM < mAmCircleRadius)
            return AM;

        float distanceToCenterPM = (float)Math.sqrt((eventx - mPmXcenter) * (eventx - mPmXcenter) +
                (eventy - mAmPmYcenter) * (eventy - mAmPmYcenter));
        if (distanceToCenterPM < mAmCircleRadius)
            return PM;
        return -1;//neither in am nor pm
    }



    /**
     * private methods
     */
    private void drawNumbers(Canvas canvas, float numberCircleRadius, double centerX, double centerY) {
        float innerRadius = numberCircleRadius * mInnerRadiusPercent;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(numberCircleRadius * mTextSizeMultipler);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        Paint.FontMetrics metrics =  mPaint.getFontMetrics();
        double x1 = centerX;
        double y1 = centerY - (mPaint.descent() + mPaint.ascent()) / 2;

        if (minutesStr != null)
            for (int i = 0; i < 12; ++i) {
                double angle = i / 12.0 * 2 * Math.PI;
                double x = x1 + innerRadius * Math.sin(angle);
                double y = y1 - innerRadius * Math.cos(angle);
                canvas.drawText(minutesStr[i], (float)x, (float)y, mPaint);
            }
    }

    private void drawSelectionCircle(Canvas canvas, float outerCircleRadius, double centerX, double centerY) {
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(128);
        double x1 = centerX;
        double y1 = centerY;
        if (mType == ClockType.HOUR) {
            double angle = (selectedNumber == 12) ? 0 : (selectedNumber / 12.0 * 2 * Math.PI);
            double x = x1 + (outerCircleRadius* 0.8 )  * Math.sin(angle);
            double y = y1 - (outerCircleRadius* 0.8 )  * Math.cos(angle);
            canvas.drawCircle((float)x, (float)y, outerCircleRadius * mSelectedCircleRadiusMultipler, mPaint);

            double lineLength = outerCircleRadius * (0.8 - mSelectedCircleRadiusMultipler);
            double lx = x1 + lineLength * Math.sin(angle);
            double ly = y1 - lineLength * Math.cos(angle);
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(1);
            canvas.drawLine((float)x1, (float)y1, (float)lx, (float)ly, mPaint);

        }
        //TODO draw minutes selection circle
        else {
        }
    }

    private void drawAMPMCircle(Canvas canvas, float outerCircleRadius, double centerX, double centerY) {
        float textSize = outerCircleRadius * mTextSizeMultipler;
        mAmCircleRadius = outerCircleRadius * mAMPMCircleRadiusMultipler;
        mAmXcenter = (float)centerX - outerCircleRadius + mAmCircleRadius;
        mPmXcenter = (float)centerX + outerCircleRadius - mAmCircleRadius;
        mAmPmYcenter = (float)centerY + outerCircleRadius - mAmCircleRadius / 2;
        if (am_pm == AM) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(128);
            canvas.drawCircle(mAmXcenter, mAmPmYcenter, mAmCircleRadius, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawCircle(mPmXcenter, mAmPmYcenter, mAmCircleRadius, mPaint);
        }
        else {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(128);
            canvas.drawCircle(mPmXcenter, mAmPmYcenter, mAmCircleRadius, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawCircle(mAmXcenter, mAmPmYcenter, mAmCircleRadius, mPaint);
        }

        String amStr = getResources().getString(R.string.time_am);
        String pmStr = getResources().getString(R.string.time_pm);

        mPaint.setColor(Color.WHITE);
        float amPmTextYcenter = mAmPmYcenter - (mPaint.descent() + mPaint.ascent()) / 2;
        canvas.drawText(amStr, mAmXcenter, amPmTextYcenter, mPaint);
        canvas.drawText(pmStr, mPmXcenter, amPmTextYcenter, mPaint);
    }
}
