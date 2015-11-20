package com.example.easymusicplayer1.utility;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * http://blog.csdn.net/a345017062/article/details/6321403
 * 用于扫描文件，扫描完后，会把新下载的音乐添加到多媒体数据库里面去！
 * @author feng
 *
 */
public class ScanMusicFile implements MediaScannerConnectionClient {

	/*MediaScannerConnection提供了一个方法给应用程序通过media scanner service去创建或者下载一个多媒体文件。这个media scanner service
	*将从这个文件中读取元数据并且把这个文件添加到media content provider中去。MediaScannerConnectionClient为media scanner service提供了一个接口
	*用来返回新扫描文件的uri到MediaScannerConnection类的客户端.
	*/
	MediaScannerConnection mConnection;
	
	public void scanMusic(Context context)
	{
		mConnection = new MediaScannerConnection(context , this);     //异步，和butoon的点击事件类似
		mConnection.connect();                                        //Initiates a connection to the media scanner service. MediaScannerConnectionClient.onMediaScannerConnected() will be called when the connection is established.
	}
	
	/**
	 * Called to notify the client when a connection to the MediaScanner service has been established.
	 * 调用通知客户端，当与MediaScanner service的一个连接已经被建立
	 */
	@Override
	public void onMediaScannerConnected() {
		//Environment.getExternalStorageDirectory()      在我手机上的路径是/sdcard/sdcard0/
		Log.e("MainActivity", Environment.getExternalStorageDirectory().toString());
		mConnection.scanFile(Environment.getExternalStorageDirectory() + "/" + "musicTree", ".mp3");         //mimeType   文件类型

	}

	/**
	 * Called to notify the client when the media scanner has finished scanning a file.
	 * 调用通知客户端，当media scanner扫描文件，扫描完成的时候。
	 */
	@Override
	public void onScanCompleted(String path, Uri uri) {    //the Uri for the file if the scanning operation succeeded and the file was added to the media database, or null if scanning failed.
       
		mConnection.disconnect();                         //Releases the connection to the media scanner service.

	}

}
