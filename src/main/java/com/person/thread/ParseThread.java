package com.person.thread;

import java.io.File;
import java.io.IOException;

import com.person.model.ParmeterModel;
import com.person.service.ParseCsvService;

public class ParseThread implements Runnable {
    private ParmeterModel pm;
    private String subPath;
	public ParseThread(ParmeterModel pm, String subPath){
		this.pm = pm;
		this.subPath = subPath;
	}
	
	@Override
	public void run() {
		System.out.println("Thread: "+Thread.currentThread().getName()+" start...");
		try {
			new ParseCsvService(pm.getBaseFilePath()+File.separator+subPath).parseCSVFile(pm.getBatchNum(), pm.getFieldNum(), pm.getSql());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("Thread: "+Thread.currentThread().getName()+" finished task!");
		}
	}
}
