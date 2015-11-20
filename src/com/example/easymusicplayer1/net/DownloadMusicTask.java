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
	
	String address;                      //用来储存音乐的下载地址
	
	Context mContext;                    //用于显示progressDialog
	
	String fileName;                     //用来建立音乐的名称
	
	ProgressDialog progressDialog;       //
	
	
	
	public DownloadMusicTask(String url , Context context , String musicName) {
	
		address = url;              //音乐的下载地址!!!
	    mContext = context;         //用于显示progressDialog
	    fileName = musicName;       //用于构造文件名
	} 
	
	@Override
	protected void onPreExecute() {           //做一些窗口初始化工作
		super.onPreExecute();

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("音乐下载中");
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(1);                                //参数为0，就是一个圈圈在转动，1为平常的进度条，2，3.。。。。都是0的情况!!!
		progressDialog.show();
	}
	
	
	

	@Override 
	protected Boolean doInBackground(Void... params) {                 //在子线程中进行

		try {
			URL url = new URL(address);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();    //通过url的引用，返回一个新的资源连接
			httpUrlConnection.setRequestMethod("GET");                                         //设置要向服务器请求获取数据
			httpUrlConnection.setReadTimeout(8000);    //设置最大响应的时间来等待输入流的进入在放弃之前。在数据变得可用之前，如果时间超时就会抛出异常。默认值0，则没有读取超时这个说法，而是无限阻塞下去!!!
			httpUrlConnection.setConnectTimeout(8000); //设置等待连接的时间，在连接被建立之前如果规定时间结束了，则会抛出异常。默认值为0，是一个阻塞的连接，这并不意味着没有超时，这意味着你将在几分钟后获得一个TCP超时
			//httpUrlConnection.connect();
			
			String path = Environment.getExternalStorageDirectory() + "/musicTree/";     //Environment提供环境变量的入口 , 获取外部存储路径，如果手机上的内存卡被安装在电脑上（USB调试）或者已经被从手机上移除了或者一些问题发生了，则这个路径当前是无法进入的，你可以观看当前的内存卡状态用getExternalStorageState（）函数。注意：不要被external这个词所迷惑。这个路径可以被更好的认为是媒体存储或者是共享存储，这是一个可以容纳相对大量数据的文件系统，而且可以在所有的应用程序之间共享（不执行权限），传统来说这是一个sd card，但他可以被实现为内置的存储装置，明显不同于受保护的内部存储，可以作为一个文件系统安装在电脑上.在多重用户设备上（被UserManager描述为），每个用户自己都有独立的外部存储空间，用户运行的应用程序只能访问外部存储器。在带有多外部存储路径的设备中，这个存储路径代表着和用户相互影响的“主”外部存储，通过应用程序可以进入到二级存储路径， Applications should not directly use this top-level directory, in order to avoid polluting the user's root namespace. Any files that are private to the application should be placed in a directory returned by Context.getExternalFilesDir, which the system will take care of deleting if the application is uninstalled. Other shared files should be placed in one of the directories returned by getExternalStoragePublicDirectory. Writing to this path requires the android.Manifest.permission.WRITE_EXTERNAL_STORAGE permission, and starting in read access requires the android.Manifest.permission.READ_EXTERNAL_STORAGE permission, which is automatically granted if you hold the write permission. Starting in android.os.Build.VERSION_CODES.KITKAT, if your application only needs to store internal data, consider using Context.getExternalFilesDir(String) or Context.getExternalCacheDir(), which require no permissions to read or write. This path may change between platform versions, so applications should only persist relative paths. Here is an example of typical code to monitor the state of external storage: {@sample development/samples/ApiDemos/src/com/example/android/apis/content/ExternalStorage.java monitor_storage}


			Log.e("DownloadMusicTask", "Path : " + path);
			File file = new File(path);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			InputStream in = httpUrlConnection.getInputStream();    //从URLConnection指向的资源中返回一个InputStream，默认情况下会抛出一个异常，这个方法必须被其子类覆盖!!
			File outputFile = new File(file , fileName + ".mp3");            //以.mp3结尾保存文件，不然无法播放!!!
			FileOutputStream fileOutput = new FileOutputStream(outputFile);
			byte[]buffer = new byte[1024];                          //
			int len1 = 0;
			while((len1 = in.read(buffer)) != -1)                   //Equivalent（相当于） to read(buffer, 0, buffer.length).
			{
				fileOutput.write(buffer , 0 , len1);                //写入数据到文件
			}
			fileOutput.flush();	                                    //http://wenda.tianya.cn/question/11d68fb1fb46a2cf		//flush()是清空，而不是刷新啊。一般主要用在IO中，即清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后用数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()，将数据强行输出到目标文件中。
			fileOutput.close();                                     //关闭流，该方法的实现应该是释放流调用的资源。这并没有实现.文件流用完之后必须及时通过close方法关闭，否则会一直处于打开状态，直至程序停止，增加系统负担。
			in.close();			                             	    // //关闭流，该方法的实现应该是释放流调用的资源。这并没有实现.文件流用完之后必须及时通过close方法关闭，否则会一直处于打开状态，直至程序停止，增加系统负担。
			
		
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
		progressDialog.setProgress(Integer.parseInt(String.valueOf(values)) + progress);         //设置进度，并且把Integer设置转换为int
		
	}
	
	@Override
	protected void onCancelled(Boolean result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.cancel();                     //下载完歌曲后，使progressDialog消失!!!
		
		ScanMusicFile scanMusic = new ScanMusicFile();                //当下载完歌曲后，自动扫描下载文件，让再次点击“我的音乐”导航项的时候就已经是更新的音乐列表了!!!
		scanMusic.scanMusic(mContext);
		
		super.onPostExecute(result);
	}
	
}
