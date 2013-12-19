package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class MyView extends ViewGroup {

	float curX, curY;
	int xWidth;
	int color_type = 0;
	boolean isOnClick = false;
	boolean isPushed = false;
	int mPushDeep = 0;
	int mBottom = 0, mLeft = 0, mRight = 0, mTop = 0;
	
	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init (attrs);
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void init(AttributeSet attrs) { 
	    TypedArray a=getContext().obtainStyledAttributes(
	         attrs,
	         R.styleable.MyView);

	    //Use a
	    try {
	    	mPushDeep = a.getInteger(R.styleable.MyView_pushDeep, 100);
	    }
	    finally {    

	    	//Don't forget this
	    	a.recycle();
	    }
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		getChildAt(0).layout(left, top, right, bottom);
		getChildAt(1).layout(left - right, top, left, bottom);
		color_type = 0;
		mBottom = bottom;
		mTop = top;
		mRight = right;
		mLeft = left;
	}
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    xWidth = widthSize;
	    
	    for (int i = 0; i < getChildCount(); i++) {
	    	
	    	getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
	    }
	    
	    setMeasuredDimension(widthSize, heightSize);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {

		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		switch (e.getAction()) {
		
			case MotionEvent.ACTION_DOWN: 
				curX = e.getX();
				curY = e.getY();
				isOnClick = true;
				return true;
			case MotionEvent.ACTION_MOVE:
				float x_new = e.getX();
				float y_new = e.getY();
				float dX = x_new - curX;
				float dY = y_new - curY;
				curX = x_new;
				curY = y_new;
				float copyCurX = curX;
				if (dX > 0 && copyCurX < xWidth*1/4) {
				
					if (Math.abs(dX) > Math.abs(dY)) {
					
						//if (Math.abs(dX) > ViewConfiguration.getTouchSlop()) {
						float velocity = Math.abs(dX)*500/(e.getEventTime() - e.getDownTime());
						System.out.println("Velocity" + velocity);
						System.out.println(ViewConfiguration.getMinimumFlingVelocity());
						if (velocity <= ViewConfiguration.getMaximumFlingVelocity() &&
							velocity >= ViewConfiguration.getMinimumFlingVelocity()) {	
						
							Log.e("onFling", String.valueOf(dX));
							if (isPushed) {
								getChildAt(1).offsetLeftAndRight((int)(mPushDeep));
							}
							isOnClick = false;
							isPushed = false;
						}
					}
				}
				if (Math.abs(dX) > ViewConfiguration.getTouchSlop() && isOnClick) {
					isOnClick = false;
				}
				break;
			case MotionEvent.ACTION_UP:
				
				if (isOnClick) {
					
					getChildAt(1).layout(mLeft - mRight, mTop, mLeft + 10, mBottom);
					isPushed = true;
					switch (color_type) {
					
					case 0:
						getChildAt(0).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
						color_type = 1;
						break;
					case 1:
						getChildAt(0).setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
						color_type = 2;
						break;
					case 2:
						getChildAt(0).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
						color_type = 0;
						break;
					default:
						break;
					}
				}
			default:
				return false;
		}
		return super.onTouchEvent(e);
	}
}
