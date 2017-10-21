package com.doubletapp.hermitage.hermitage.ui.map.mark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.doubletapp.hermitage.hermitage.R;
import com.doubletapp.hermitage.hermitage.model.Hall;
import com.doubletapp.hermitage.hermitage.model.map.Position;

/**
 * Created by navi9 on 22.10.2017.
 */

public class HallMarker extends MapMark {
    private Hall hall;

    public HallMarker(Context context, Hall hall) {
        super(context, R.drawable.ic_user_blue_20px, 120);

        this.hall = hall;
    }

    @Override
    protected void onInvalidate() {

    }

    @Override
    public View getView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(getRes()), getSize(), getSize(), false));

        return imageView;
    }

    private Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        int xPos = (canvas.getWidth() / 2) - (int) (textPaint.measureText(hall.getId()) / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

        canvas.drawText(hall.getId(), xPos, yPos, textPaint);

        return bitmap;
    }

    @Override
    public Position getMarkPosition() {
        return hall.getMainRoom().getPosition();
    }

    public Hall getHall() {
        return hall;
    }
}