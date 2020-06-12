package com.qrreader.QRReader.test;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool implements Serializable {

	private static final long serialVersionUID = 232323;
	private static ExecutorService executorService = null;

	private ThreadPool() {
		if (executorService != null) {
			throw new AssertionError();
		}
	}

	public static ExecutorService getInstance() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(ThreadPoolCalculator.getOptimalThreadCount());
		}
		return executorService;
	}

	private Object readResolve() {
		return executorService;
	}
}