package com.example.motionsense;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class Scan implements MediaScannerConnectionClient {
	private MediaScannerConnection mMs;
	private File mFile;
	
	public Scan(Context context, File f) {
	    mFile = f;
	    mMs = new MediaScannerConnection(context, this);
	    mMs.connect();
	}
	
	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
	    mMs.scanFile(mFile.getAbsolutePath(), null);

	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		// TODO Auto-generated method stub
		System.out.println("DONE SCAN1");
	    mMs.disconnect();

	}

}
