//package org.jabelpeeps.jabeltris;
//
//class  InputThreadSender {
//	
//	private int tDx, tDy, tUx, tUy;
//	private boolean toTouchDown, toTouchUp;
//	
//	class DownLock {};
//	class UpLock {};
//	UpLock upLock = new UpLock();
//	DownLock downLock = new DownLock();
//		
//	InputThreadSender () {
//		tDx = -10;
//		tDy = -10;
//		tUx = -10;
//		tUy = -10;
//		toTouchUp = false;
//		toTouchDown = false;
//	}
//	
//	void sendTouchDown (int x, int y) {
//	    synchronized(downLock) {
//	    	tDx = x;
//			tDy = y;
//			toTouchDown = true;
//	    }
//	}
//	synchronized void sendTouchUp (int x, int y) {
//		synchronized(upLock) {
//			tUx = x;
//			tUy = y;
//			toTouchUp = true;
//		}
//	}
//}
