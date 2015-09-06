package com.person.client;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.person.model.ParmeterModel;
import com.person.service.PreparedService;
import com.person.thread.ParseThread;

public class CsvParseApp {

	public static void main(String[] args) {
		System.out.println("system start...");
		ParmeterModel pm = new PreparedService().getPreparedParmeter();
		File dir = new File(pm.getBaseFilePath());
		if(dir.isDirectory()){
			ExecutorService executorService = Executors.newFixedThreadPool(4);
			File[] files = dir.listFiles();
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < files.length; i++){
				executorService.execute(new Thread(new ParseThread(pm, files[i].getName())));
			}
			executorService.shutdown();
			try {
				boolean loop = true;
				do { // waitting all task finished
					loop = !executorService.awaitTermination(2, TimeUnit.SECONDS);
				} while (loop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			System.out.println("system end.");
			System.out.println("spend time:" + (endTime - startTime) / 1000.00 + "s");
		}
	}
}
