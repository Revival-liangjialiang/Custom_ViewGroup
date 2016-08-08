package com.example.k.touchtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by k on 2016/8/7.
 */
public class Custom_ViewGroup extends ViewGroup {
    int offsetY = 0;
    int value_1 = 0;
    int value_2 = 0;
    float current_value = 0;
    int start_valueY;
    int child_count = 0;
    int mStart = 0;
    int mEnd = 0;
    int parentView_height;
    boolean b = false;
    //Scroller scroller = new Scroller(getContext(),new BounceInterpolator());//回弹的动画
    Scroller scroller = new Scroller(getContext(),new LinearInterpolator());

    public Custom_ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean ch, int l, int t, int r, int b) {
        parentView_height = getHeight();
        child_count = getChildCount();
    MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.height =  parentView_height*child_count;
        setLayoutParams(params);
        for(int a = 0;a<child_count;a++){
            View view = getChildAt(a);
            if(view.getVisibility()!=View.GONE) {
                view.layout(l, a * parentView_height, r, (a * parentView_height) + parentView_height);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStart = getScrollY();
                start_valueY = (int)event.getY();
                current_value = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                    offsetY = start_valueY-(int) event.getY();
                    value_2 = value_1 - Math.abs(offsetY);
                    value_1 = Math.abs(offsetY);
                if(current_value<event.getY()) {
                    if(getScrollY()>0) {
                        scrollBy(0, -Math.abs(value_2));
                    }else{
                        scrollBy(0, -Math.abs(value_2)/15);
                    }
                }else {
                    if(getScrollY()<(child_count-1)*parentView_height) {
                        scrollBy(0, Math.abs(value_2));
                    }else{
                        scrollBy(0, Math.abs(value_2)/15);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(getScrollY()<0) {
                    scroller.startScroll(0, getScrollY(), 0, -(parentView_height - (parentView_height - getScrollY())), 200);
                    postInvalidate();
                }
                if(getScrollY()>(child_count-1)*parentView_height){
                    scroller.startScroll(0, getScrollY(), 0, -(getScrollY()-parentView_height), 200);
                    postInvalidate();
                }
                current_value = 0;
                value_1 = 0;
                value_2 = 0;
                offsetY = 0;
                mEnd = getScrollY();
                int dY = mEnd - mStart;
                if(dY>0){
                    if(dY<parentView_height/2){
                        scroller.startScroll(0,getScrollY(),0,-dY,80);
                    }else{
                        scroller.startScroll(0,getScrollY(),0,parentView_height-dY,100);
                    }
                    postInvalidate();
                }else{
                    if(-dY<parentView_height/2){
                        scroller.startScroll(0,getScrollY(),0,-dY,80);
                    }else{
                        scroller.startScroll(0,getScrollY(),0,-parentView_height-dY,100);
                    }
                    postInvalidate();
                }
                break;
            default:
                break;
        }
        current_value = event.getY();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(0,scroller.getCurrY());
            postInvalidate();
        }
    }
}
