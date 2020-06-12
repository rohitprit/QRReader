package com.qrreader.QRReader.test;

public class ThreadPoolCalculator {
	private ThreadPoolCalculator() {

		super();
	}

	public static int getOptimalThreadCount() {

		int numberOfThreads = 4;

		int numberOfCPU = Runtime.getRuntime().availableProcessors();
//		System.out.println("Number of processors: "+numberOfCPU);

		int targetCPUUtilization = 1;

		int ratioWC = 2;

		numberOfThreads = numberOfCPU * targetCPUUtilization * (1 + ratioWC);
		return numberOfThreads;
	}
}