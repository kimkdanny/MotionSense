package com.example.motionsense;
/**
 * @(#)MotionSense.java
 *
 * MotionSense application
 *
 * @author
 * @version 1.00 2013/9/21
 */

 import java.util.*;

public class Calculate {

    public Calculate(){
    }


	//This method returns the percent error of two numbers
    public static double percentError(double approximateValue, double exactValue){

		return 100*((approximateValue-exactValue)/exactValue);
    }

    //This method returns an ArrayList of percent errors between two other ArrayLists
    public static ArrayList <Double> percentErrorArrayListMethod(ArrayList <Double> trueArrayList,
        	ArrayList <Double> triedArrayList){
    	
            int size;
            if(triedArrayList.size()<trueArrayList.size())
        	        size = trueArrayList.size();
            else
                    size = triedArrayList.size();

        	ArrayList <Double> percentErrorList = new ArrayList <Double> ();

        	for(int i = 0; i < size-1; i++){
        		percentErrorList.add(Math.abs(percentError(triedArrayList.get(i), trueArrayList.get(i))));
        	}

        	return percentErrorList;
        }

	//This method averages 3 numbers
    public static double average(double number1, double number2, double number3){

		return (number1+number2+number3)/3;
    }

    //This method returns an arraylist taking the average of 3 arrayLists
    public static ArrayList <Double> takeAverageInArrayList(ArrayList <Double> one, ArrayList <Double> two,
    	ArrayList <Double> three){

    		ArrayList <Double> average = new ArrayList <Double>();

    		for(int i = 0; i < one.size(); i ++){
    			average.add(average(one.get(i), two.get(i), three.get(i)));
    		}

    		return average;

    }

    //This method shortens the list to whatever value you want it to be
    public static ArrayList <Double> trimArrayList(int sizeDesired, ArrayList <Double> arrayList){
    	while (arrayList.size() > sizeDesired){
    		arrayList.remove(arrayList.size()-1);
    	}
    	return (arrayList);
    }

    //This method picks out which the shorter ArrayList is and makes the sizes of them equal
    public static void trimToShorter(ArrayList <Double> one, ArrayList <Double> two){
    	if(one.size()>two.size()){
    		one = trimArrayList(two.size(), one);
    	}else{
    		two = trimArrayList(one.size(), two);
    	}
    }

	//This method contains percent errors of x,y,z of all 4 sensors for every 300ms
	public static ArrayList <Double> percentErrorTime(ArrayList <Double> xGravityTrue, ArrayList <Double> yGravityTrue,
    	ArrayList <Double> zGravityTrue, ArrayList <Double> xRotationTrue, ArrayList <Double> yRotationTrue,
    		ArrayList <Double> zRotationTrue, ArrayList <Double> xGyroTrue, ArrayList <Double> yGyroTrue,
    			ArrayList <Double> zGyroTrue, ArrayList <Double> xAccelTrue, ArrayList <Double> yAccelTrue,
    				ArrayList <Double> zAccelTrue, ArrayList <Double> xGravityTried, ArrayList <Double> yGravityTried,
    					ArrayList <Double> zGravityTried, ArrayList <Double> xRotationTried, ArrayList <Double> yRotationTried,
    						ArrayList <Double> zRotationTried, ArrayList <Double> xGyroTried,
    						 ArrayList <Double> yGyroTried, ArrayList <Double> zGyroTried,
    						 	ArrayList <Double> xAccelTried, ArrayList <Double> yAccelTried,
    								ArrayList <Double> zAccelTried){

    	//make true and tried ArrayList sizes the same for all 12 ArrayLists lol
	    trimToShorter(xGravityTrue, xGravityTried);
	    trimToShorter(yGravityTrue, yGravityTried);
	    trimToShorter(zGravityTrue, zGravityTried);
	    trimToShorter(xRotationTrue, xRotationTried);
	    trimToShorter(yRotationTrue, yRotationTried);
	    trimToShorter(zRotationTrue, zRotationTried);
	    trimToShorter(xGyroTrue, xGyroTried);
	    trimToShorter(yGyroTrue, yGyroTried);
	    trimToShorter(zGyroTrue, zGyroTried);
	    trimToShorter(xAccelTrue, xAccelTried);
	    trimToShorter(yAccelTrue, yAccelTried);
	    trimToShorter(zAccelTrue, zAccelTried);

	    //ArrayLists for percent error for all 12 possible ArrayLists
	    ArrayList <Double> xGravityPercent = percentErrorArrayListMethod(xGravityTrue, xGravityTried);
	    ArrayList <Double> yGravityPercent = percentErrorArrayListMethod(yGravityTrue, yGravityTried);
	    ArrayList <Double> zGravityPercent = percentErrorArrayListMethod(zGravityTrue, zGravityTried);
	    ArrayList <Double> xRotationPercent = percentErrorArrayListMethod(xRotationTrue, xRotationTried);
	    ArrayList <Double> yRotationPercent = percentErrorArrayListMethod(yRotationTrue, yRotationTried);
	    ArrayList <Double> zRotationPercent = percentErrorArrayListMethod(zRotationTrue, zRotationTried);
	    ArrayList <Double> xGyroPercent = percentErrorArrayListMethod(xGyroTrue, xGyroTried);
	    ArrayList <Double> yGyroPercent = percentErrorArrayListMethod(yGyroTrue, yGyroTried);
	    ArrayList <Double> zGyroPercent = percentErrorArrayListMethod(zGyroTrue, zGyroTried);
	    ArrayList <Double> xAccelPercent = percentErrorArrayListMethod(xAccelTrue, xAccelTried);
	    ArrayList <Double> yAccelPercent = percentErrorArrayListMethod(yAccelTrue, yAccelTried);
	    ArrayList <Double> zAccelPercent = percentErrorArrayListMethod(zAccelTrue, zAccelTried);

	    //Take average between x/y/x percent errors
	    ArrayList <Double> gravityPercent = takeAverageInArrayList(xGravityPercent, yGravityPercent,
	    	zGravityPercent);
	    ArrayList <Double> rotationPercent = takeAverageInArrayList(xRotationPercent, yRotationPercent,
	    	zRotationPercent);
	    ArrayList <Double> gyroPercent = takeAverageInArrayList(xGyroPercent, yGyroPercent,
	    	zGyroPercent);
	    ArrayList <Double> accelPercent = takeAverageInArrayList(xAccelPercent, yAccelPercent,
	    	zAccelPercent);

	    ArrayList <Double> percentErrorList = new ArrayList <Double> ();

	    for(int i = 0; i < gravityPercent.size(); i++){
	    	percentErrorList.add((gravityPercent.get(i)+rotationPercent.get(i)+accelPercent.get(i))/3);
	    }

		return percentErrorList;
	}



    public static double accuracy(ArrayList <Double> xGravityTrue, ArrayList <Double> yGravityTrue,
    	ArrayList <Double> zGravityTrue, ArrayList <Double> xRotationTrue, ArrayList <Double> yRotationTrue,
    		ArrayList <Double> zRotationTrue, ArrayList <Double> xGyroTrue, ArrayList <Double> yGyroTrue,
    			ArrayList <Double> zGyroTrue, ArrayList <Double> xAccelTrue, ArrayList <Double> yAccelTrue,
    				ArrayList <Double> zAccelTrue, ArrayList <Double> xGravityTried, ArrayList <Double> yGravityTried,
    					ArrayList <Double> zGravityTried, ArrayList <Double> xRotationTried, ArrayList <Double> yRotationTried,
    						ArrayList <Double> zRotationTried, ArrayList <Double> xGyroTried,
    						 ArrayList <Double> yGyroTried, ArrayList <Double> zGyroTried,
    						 	ArrayList <Double> xAccelTried, ArrayList <Double> yAccelTried,
    								ArrayList <Double> zAccelTried){

	    //make true and tried ArrayList sizes the same for all 12 ArrayLists lol
	    trimToShorter(xGravityTrue, xGravityTried);
	    trimToShorter(yGravityTrue, yGravityTried);
	    trimToShorter(zGravityTrue, zGravityTried);
	    trimToShorter(xRotationTrue, xRotationTried);
	    trimToShorter(yRotationTrue, yRotationTried);
	    trimToShorter(zRotationTrue, zRotationTried);
	    trimToShorter(xGyroTrue, xGyroTried);
	    trimToShorter(yGyroTrue, yGyroTried);
	    trimToShorter(zGyroTrue, zGyroTried);
	    trimToShorter(xAccelTrue, xAccelTried);
	    trimToShorter(yAccelTrue, yAccelTried);
	    trimToShorter(zAccelTrue, zAccelTried);

	    //ArrayLists for percent error for all 12 possible ArrayLists
	    ArrayList <Double> xGravityPercent = percentErrorArrayListMethod(xGravityTrue, xGravityTried);
	    ArrayList <Double> yGravityPercent = percentErrorArrayListMethod(yGravityTrue, yGravityTried);
	    ArrayList <Double> zGravityPercent = percentErrorArrayListMethod(zGravityTrue, zGravityTried);
	    ArrayList <Double> xRotationPercent = percentErrorArrayListMethod(xRotationTrue, xRotationTried);
	    ArrayList <Double> yRotationPercent = percentErrorArrayListMethod(yRotationTrue, yRotationTried);
	    ArrayList <Double> zRotationPercent = percentErrorArrayListMethod(zRotationTrue, zRotationTried);
	    ArrayList <Double> xGyroPercent = percentErrorArrayListMethod(xGyroTrue, xGyroTried);
	    ArrayList <Double> yGyroPercent = percentErrorArrayListMethod(yGyroTrue, yGyroTried);
	    ArrayList <Double> zGyroPercent = percentErrorArrayListMethod(zGyroTrue, zGyroTried);
	    ArrayList <Double> xAccelPercent = percentErrorArrayListMethod(xAccelTrue, xAccelTried);
	    ArrayList <Double> yAccelPercent = percentErrorArrayListMethod(yAccelTrue, yAccelTried);
	    ArrayList <Double> zAccelPercent = percentErrorArrayListMethod(zAccelTrue, xGravityTried);

	    //Take average between x/y/x percent errors
	    ArrayList <Double> gravityPercent = takeAverageInArrayList(xGravityPercent, yGravityPercent,
	    	zGravityPercent);
	    ArrayList <Double> rotationPercent = takeAverageInArrayList(xRotationPercent, yRotationPercent,
	    	zRotationPercent);
	    ArrayList <Double> gyroPercent = takeAverageInArrayList(xGyroPercent, yGyroPercent,
	    	zGyroPercent);
	    ArrayList <Double> accelPercent = takeAverageInArrayList(xAccelPercent, yAccelPercent,
	    	zAccelPercent);

	    //average percent error for each category at each second
	    double averageGravityPercent = 0;
	    for(int i = 0; i < gravityPercent.size(); i++){
	    	averageGravityPercent += gravityPercent.get(i);
	    }
	    averageGravityPercent /= gravityPercent.size();

	    double averageRotationPercent = 0;
	    for(int i = 0; i < rotationPercent.size(); i++){
	    	averageRotationPercent += rotationPercent.get(i);
	    }
	    averageRotationPercent /= rotationPercent.size();

	    double averageGyroPercent = 0;
	    for(int i = 0; i < gyroPercent.size(); i++){
	    	averageGyroPercent += gyroPercent.get(i);
	    }
	    averageGyroPercent /= gyroPercent.size();

	    double averageAccelPercent = 0;
	    for(int i = 0; i < accelPercent.size(); i++){
	    	averageAccelPercent += accelPercent.get(i);
	    }
	    averageAccelPercent /= accelPercent.size();


	    double totalAccuracy = 100 - ((averageGravityPercent + averageRotationPercent + averageGyroPercent
	    	+ averageAccelPercent)/4);

	    return totalAccuracy;

    }
}
