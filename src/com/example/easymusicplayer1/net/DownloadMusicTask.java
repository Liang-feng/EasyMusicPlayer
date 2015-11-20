package com.example.easymusicplayer1.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.easymusicplayer1.utility.ScanMusicFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class DownloadMusicTask extends AsyncTask<Void , Integer ,  Boolean> {
	
	String address;                      //�����������ֵ����ص�ַ
	
	Context mContext;                    //������ʾprogressDialog
	
	String fileName;                     //�����������ֵ�����
	
	ProgressDialog progressDialog;       //
	
	
	
	public DownloadMusicTask(String url , Context context , String musicName) {
	
		address = url;              //���ֵ����ص�ַ!!!
	    mContext = context;         //������ʾprogressDialog
	    fileName = musicName;       //���ڹ����ļ���
	} 
	
	@Override
	protected void onPreExecute() {           //��һЩ���ڳ�ʼ������
		super.onPreExecute();

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("����������");
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(1);                                //����Ϊ0������һ��ȦȦ��ת����1Ϊƽ���Ľ�������2��3.������������0�����!!!
		progressDialog.show();
	}
	
	
	

	@Override 
	protected Boolean doInBackground(Void... params) {                 //�����߳��н���

		try {
			URL url = new URL(address);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();    //ͨ��url�����ã�����һ���µ���Դ����
			httpUrlConnection.setRequestMethod("GET");                                         //����Ҫ������������ȡ����
			httpUrlConnection.setReadTimeout(8000);    //���������Ӧ��ʱ�����ȴ��������Ľ����ڷ���֮ǰ�������ݱ�ÿ���֮ǰ�����ʱ�䳬ʱ�ͻ��׳��쳣��Ĭ��ֵ0����û�ж�ȡ��ʱ���˵������������������ȥ!!!
			httpUrlConnection.setConnectTimeout(8000); //���õȴ����ӵ�ʱ�䣬�����ӱ�����֮ǰ����涨ʱ������ˣ�����׳��쳣��Ĭ��ֵΪ0����һ�����������ӣ��Ⲣ����ζ��û�г�ʱ������ζ���㽫�ڼ����Ӻ���һ��TCP��ʱ
			//httpUrlConnection.connect();
			
			String path = Environment.getExternalStorageDirectory() + "/musicTree/";     //Environment�ṩ������������� , ��ȡ�ⲿ�洢·��������ֻ��ϵ��ڴ濨����װ�ڵ����ϣ�USB���ԣ������Ѿ������ֻ����Ƴ��˻���һЩ���ⷢ���ˣ������·����ǰ���޷�����ģ�����Թۿ���ǰ���ڴ濨״̬��getExternalStorageState����������ע�⣺��Ҫ��external��������Ի����·�����Ա����õ���Ϊ��ý��洢�����ǹ���洢������һ������������Դ������ݵ��ļ�ϵͳ�����ҿ��������е�Ӧ�ó���֮�乲����ִ��Ȩ�ޣ�����ͳ��˵����һ��sd card���������Ա�ʵ��Ϊ���õĴ洢װ�ã����Բ�ͬ���ܱ������ڲ��洢��������Ϊһ���ļ�ϵͳ��װ�ڵ�����.�ڶ����û��豸�ϣ���UserManager����Ϊ����ÿ���û��Լ����ж������ⲿ�洢�ռ䣬�û����е�Ӧ�ó���ֻ�ܷ����ⲿ�洢�����ڴ��ж��ⲿ�洢·�����豸�У�����洢·�������ź��û��໥Ӱ��ġ������ⲿ�洢��ͨ��Ӧ�ó�����Խ��뵽�����洢·���� Applications should not directly use this top-level directory, in order to avoid polluting the user's root namespace. Any files that are private to the application should be placed in a directory returned by Context.getExternalFilesDir, which the system will take care of deleting if the application is uninstalled. Other shared files should be placed in one of the directories returned by getExternalStoragePublicDirectory. Writing to this path requires the android.Manifest.permission.WRITE_EXTERNAL_STORAGE permission, and starting in read access requires the android.Manifest.permission.READ_EXTERNAL_STORAGE permission, which is automatically granted if you hold the write permission. Starting in android.os.Build.VERSION_CODES.KITKAT, if your application only needs to store internal data, consider using Context.getExternalFilesDir(String) or Context.getExternalCacheDir(), which require no permissions to read or write. This path may change between platform versions, so applications should only persist relative paths. Here is an example of typical code to monitor the state of external storage: {@sample development/samples/ApiDemos/src/com/example/android/apis/content/ExternalStorage.java monitor_storage}


			Log.e("DownloadMusicTask", "Path : " + path);
			File file = new File(path);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			InputStream in = httpUrlConnection.getInputStream();    //��URLConnectionָ�����Դ�з���һ��InputStream��Ĭ������»��׳�һ���쳣������������뱻�����า��!!
			File outputFile = new File(file , fileName + ".mp3");            //��.mp3��β�����ļ�����Ȼ�޷�����!!!
			FileOutputStream fileOutput = new FileOutputStream(outputFile);
			byte[]buffer = new byte[1024];                          //
			int len1 = 0;
			while((len1 = in.read(buffer)) != -1)                   //Equivalent���൱�ڣ� to read(buffer, 0, buffer.length).
			{
				fileOutput.write(buffer , 0 , len1);                //д�����ݵ��ļ�
			}
			fileOutput.flush();	                                    //http://wenda.tianya.cn/question/11d68fb1fb46a2cf		//flush()����գ�������ˢ�°���һ����Ҫ����IO�У�����ջ��������ݣ�����˵���ö�д����ʱ����ʵ�������ȱ��������ڴ��У�Ȼ��������д���ļ��У��������ݶ����ʱ�򲻴�����������Ѿ�д���ˣ���Ϊ����һ�����п��ܻ������ڴ�����������С���ʱ������������ close()�����ر��˶�д������ô�ⲿ�����ݾͻᶪʧ������Ӧ���ڹرն�д��֮ǰ��flush()��������ǿ�������Ŀ���ļ��С�
			fileOutput.close();                                     //�ر������÷�����ʵ��Ӧ�����ͷ������õ���Դ���Ⲣû��ʵ��.�ļ�������֮����뼰ʱͨ��close�����رգ������һֱ���ڴ�״̬��ֱ������ֹͣ������ϵͳ������
			in.close();			                             	    // //�ر������÷�����ʵ��Ӧ�����ͷ������õ���Դ���Ⲣû��ʵ��.�ļ�������֮����뼰ʱͨ��close�����رգ������һֱ���ڴ�״̬��ֱ������ֹͣ������ϵͳ������
			
		
		} catch (MalformedURLException e) {                         //This exception is thrown when a program attempts to create an URL from an incorrect specification.
			Log.e("DownloadMusicTask", "Error: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("DownloadMusicTask", "Error: " + e);
			e.printStackTrace();
		}
		return true;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int progress = progressDialog.getProgress();
		progressDialog.setProgress(Integer.parseInt(String.valueOf(values)) + progress);         //���ý��ȣ����Ұ�Integer����ת��Ϊint
		
	}
	
	@Override
	protected void onCancelled(Boolean result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.cancel();                     //�����������ʹprogressDialog��ʧ!!!
		
		ScanMusicFile scanMusic = new ScanMusicFile();                //��������������Զ�ɨ�������ļ������ٴε�����ҵ����֡��������ʱ����Ѿ��Ǹ��µ������б���!!!
		scanMusic.scanMusic(mContext);
		
		super.onPostExecute(result);
	}
	
}
