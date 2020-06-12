package com.qrreader.QRReader.test;

public class TestMain {

	public static void main(String[] args) {
		ThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					System.out.println("Hi");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
