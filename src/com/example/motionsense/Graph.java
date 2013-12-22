package com.example.motionsense;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

class Graph extends View{

    double[] y;

	public Graph(Context context, AttributeSet aSet) {
		super(context, aSet);
	}

    public void setY(double[] y) {
        this.y=y;
    }



	@Override
	synchronized public void onDraw(Canvas canvas) {
        int dx=getWidth()/y.length;
        Paint red = new Paint();
        red.setARGB(255,50,194,214);
        red.setStrokeWidth(6);
        Paint blue = new Paint();
        blue.setARGB(255,0,0,0);
        blue.setStrokeWidth(3);
        Paint actualRed = new Paint();
        actualRed.setARGB(255, 255, 0, 0);
        actualRed.setStrokeWidth(4);
        for(int i=0; i<20; i++) {
            canvas.drawLine(0,getHeight()/20*i,20,getHeight()/20*i,blue);
        }
        for(int i=0; i<y.length-1; i++) {
        	System.out.println("WHAT THE FUCK IS THE HEIGHT" + getHeight());
            canvas.drawLine(i*dx,(int)(getHeight()-y[i]*getHeight()/2),(i+1)*dx,(int)(getHeight()-y[i+1]*getHeight()/2),red);
            canvas.drawLine((i+1)*dx,getHeight()-20,(i+1)*dx,getHeight(),blue);
        }
        canvas.drawLine(0,getHeight()-1,getWidth(),getHeight()-1,blue);
        canvas.drawLine(1,0,1,getHeight(),blue);
        canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, actualRed);

	}
}