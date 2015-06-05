package com.zylbaby.app.util;

public class ArrayUtils {
	
	public static int GetPosition(int[] array,int value){
		int position=-1;
		for(int i=0;i<array.length;i++){
			if(array[i]==value){
				position=i;
				break;
			}
			
		}
		
		return position;
	}

}
