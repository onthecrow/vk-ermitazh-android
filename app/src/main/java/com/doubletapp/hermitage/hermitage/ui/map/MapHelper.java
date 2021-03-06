package com.doubletapp.hermitage.hermitage.ui.map;

import android.os.Bundle;
import android.util.Log;

import com.doubletapp.hermitage.hermitage.model.map.Position;
import com.doubletapp.hermitage.hermitage.model.map.Room;
import com.doubletapp.hermitage.hermitage.ui.map.mark.HallMarker;
import com.doubletapp.hermitage.hermitage.ui.map.mark.MapMark;
import com.doubletapp.hermitage.hermitage.ui.map.mark.RoomMarker;
import com.qozix.tileview.TileView;
import com.qozix.tileview.widgets.ZoomPanLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by navi9 on 21.10.2017.
 */

public class MapHelper implements ZoomPanLayout.ZoomPanListener {
    private int x = 0;
    private int y = 0;
    private int mapWidth;
    private int mapHeight;
    private int screenWidth;
    private int screenHeight;
    private float scale;

    private PublishSubject<Boolean> mUpdateSubject;
    private PublishSubject<Float> mScaleSubject;
    private PublishSubject<Position> mPositionSubject;

    private static MapHelper sInstance = null;

    public static MapHelper newInstance(int mapWidth, int mapHeight, int screenWidth, int screenHeight, float scale) {
        if (sInstance == null) {
            sInstance = new MapHelper(mapWidth, mapHeight, screenWidth, screenHeight, scale);
        }
        return sInstance;
    }

    public void setTileView(TileView tileView) {
       tileView.addZoomPanListener(this);
    }

    private MapHelper(int mapWidth, int mapHeight, int screenWidth, int screenHeight, float scalex) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scale = scalex;

        mScaleSubject = PublishSubject.create();
        mPositionSubject = PublishSubject.create();
        mUpdateSubject = PublishSubject.create();

        mScaleSubject.onBackpressureBuffer().debounce(30, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Float>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Float aFloat) {
                scale = aFloat;
                Log.d("event_trace", "onZoomUpdate " + scale);

                mUpdateSubject.onNext(true);
            }
        });
        mPositionSubject.onBackpressureBuffer().debounce(50, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Position>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Position position) {
                x = (int) (position.getX() / scale);
                y = (int) (position.getY() / scale);
                Log.d("event_trace", "onPanUpdate " + x + " " + y);

                mUpdateSubject.onNext(true);
            }
        });
    }

    public Observable<Boolean> getUpdateObservable() {
        return mUpdateSubject.onBackpressureBuffer().observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onPanBegin(int x, int y, Origination origin) {

    }

    @Override
    public void onPanUpdate(int x, int y, Origination origin) {
        this.x = x;
        this.y = y;
        mPositionSubject.onNext(new Position(x, y));
    }

    @Override
    public void onPanEnd(int x, int y, Origination origin) {

    }

    @Override
    public void onZoomBegin(float scale, Origination origin) {

    }

    @Override
    public void onZoomUpdate(float scale, Origination origin) {
        this.scale = scale;
        mScaleSubject.onNext(scale);
    }

    @Override
    public void onZoomEnd(float scale, Origination origin) {

    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private int getScaledScreenWidth() {
        return (int) (screenWidth / scale);
    }

    private int getScaledScreenHeight() {
        return (int) (screenHeight / scale);
    }

    public boolean isPositionVisible(Position position) {
        int posX = (int) position.getX();
        int posY = (int) position.getY();
        boolean xOk = posX > x && posX < (x + getScaledScreenWidth());
        boolean yOk = posY > y && posY < (y + getScaledScreenHeight());

        return xOk && yOk;
    }

    public boolean isMapMarkVisible(MapMark mark) {
        return isPositionVisible(mark.getMarkPosition()) && scale > mark.getMinimumScaleForShow();
    }

    public boolean isRoomVisible(Room room) {
        return isPositionVisible(room.getPosition());
    }

    public boolean isRoomMarkerVisible(RoomMarker roomMarker) {
        return isRoomVisible(roomMarker.getRoom());
    }

    public boolean isHallMarkerVisible(HallMarker hallMarker) {
        return isRoomVisible(hallMarker.getHall().getMainRoom());
    }
}
