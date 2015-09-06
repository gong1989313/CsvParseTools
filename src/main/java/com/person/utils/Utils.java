package com.person.utils;

import org.apache.commons.lang3.StringUtils;

import com.person.exception.InParticularCases;

public class Utils {
	public static String convertMillsToTimes(long milliseconds){
		long hour = milliseconds / (60 * 60 * 1000);
		long minute = (milliseconds - hour * 60 * 60 * 1000) / (60 * 1000);
		long second = (milliseconds - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
		if (second >= 60) {
			second = second % 60;
			minute += second / 60;
		}
		if (minute >= 60) {
			minute = minute % 60;
			hour += minute / 60;
		}
		String sh = " ";
		String sm = " ";
		String ss = " ";
		if (hour < 10) {
			sh = "0" + String.valueOf(hour);
		} else {
			sh = String.valueOf(hour);
		}
		if (minute < 10) {
			sm = "0" + String.valueOf(minute);
		} else {
			sm = String.valueOf(minute);
		}
		if (second < 10) {
			ss = "0" + String.valueOf(second);
		} else {
			ss = String.valueOf(second);
		}
		return sh + ": " + sm + ": " + ss;
	}
	
	public static String[] bakQuotesFieldProcess(String[] parms, int pos, StringBuilder quotesFieldBak){
		String[] newParms = quotesFieldBak.toString().split(",", -1);
		if(newParms.length+pos==parms.length){
			for (int i = 0; i < newParms.length; i++) {
				parms[pos + i] = newParms[i];
			} 
		}
		return parms;
	}
	
	public static String[] specialCharProcess(String[] parms, String[] baks, int pos, StringBuilder quotesFieldBak){
		if(parms.length+pos>parms.length){
			String temp = processSpecialChar(quotesFieldBak.toString());
			if(temp != null){
				String[] newParms = temp.split(",", -1);
				
				for(int i=0; i<baks.length; i++){
					parms[i]=baks[i];
				}
				for(int i=0 ; i<newParms.length; i++){
					parms[pos+i] = newParms[i];
				}
				return parms;
			}
		}
		return null;
	}
	
	public static String processSpecialChar(String str){
		if(StringUtils.contains(str, InParticularCases.MultipleQuotes)){
			str = StringUtils.replace(str, InParticularCases.MultipleQuotes, "");
			return str;
		}
		return null;
	}
	
	public static String[] bakArray(String[] src, int pos){
		String[] newArr = new String[pos];
		System.arraycopy(src, 0, newArr, 0, pos);
		return newArr;
	}
}
