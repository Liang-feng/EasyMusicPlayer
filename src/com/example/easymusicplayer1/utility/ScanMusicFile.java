package com.example.easymusicplayer1.utility;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * http://blog.csdn.net/a345017062/article/details/6321403
 * ����ɨ���ļ���ɨ����󣬻�������ص�������ӵ���ý�����ݿ�����ȥ��
 * @author feng
 *
 */
public class ScanMusicFile implements MediaScannerConnectionClient {

	/*MediaScannerConnection�ṩ��һ��������Ӧ�ó���ͨ��media scanner serviceȥ������������һ����ý���ļ������media scanner service
	*��������ļ��ж�ȡԪ���ݲ��Ұ�����ļ���ӵ�media content provider��ȥ��MediaScannerConnectionClientΪmedia scanner service�ṩ��һ���ӿ�
	*����������ɨ���ļ���uri��MediaScannerConnection��Ŀͻ���.
	*/
	MediaScannerConnection mConnection;
	
	public void scanMusic(Context context)
	{
		mConnection = new MediaScannerConnection(context , this);     //�첽����butoon�ĵ���¼�����
		mConnection.connect();                                        //Initiates a connection to the media scanner service. MediaScannerConnectionClient.onMediaScannerConnected() will be called when the connection is established.
	}
	
	/**
	 * Called to notify the client when a connection to the MediaScanner service has been established.
	 * ����֪ͨ�ͻ��ˣ�����MediaScanner service��һ�������Ѿ�������
	 */
	@Override
	public void onMediaScannerConnected() {
		//Environment.getExternalStorageDirectory()      �����ֻ��ϵ�·����/sdcard/sdcard0/
		Log.e("MainActivity", Environment.getExternalStorageDirectory().toString());
		mConnection.scanFile(Environment.getExternalStorageDirectory() + "/" + "musicTree", ".mp3");         //mimeType   �ļ�����

	}

	/**
	 * Called to notify the client when the media scanner has finished scanning a file.
	 * ����֪ͨ�ͻ��ˣ���media scannerɨ���ļ���ɨ����ɵ�ʱ��
	 */
	@Override
	public void onScanCompleted(String path, Uri uri) {    //the Uri for the file if the scanning operation succeeded and the file was added to the media database, or null if scanning failed.
       
		mConnection.disconnect();                         //Releases the connection to the media scanner service.

	}

}
