package com.example.easymusicplayer1.utility;

import com.example.easymusicplayer1.db.SaveMusicInfo;
import com.example.easymusicplayer1.model.MusicTop;
import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 *   解析从网络返回的数据，并且把数据存储到数据库中!!!
 * @author feng
 *
 */
public class DataDipose {

		private static  MusicTop music;
		
		/**
		 * 如下解析过程，看总结文档!!!!
		 * 解析出来得数据放在了music对象中!!!
		 * @param response
		 */
		@SuppressLint("UseValueOf")
		public static void dealString(String response )
		{
			//先后按"[" , "]"分割字符串    ,所得字符串不包括"[" , "]"
			String [] allMusic = response.split("\\[");    
			Toast.makeText(MyApplication.getContext() , String.valueOf(allMusic.length) , Toast.LENGTH_LONG).show();
			String [] allMusic1 = allMusic[1].split("\\]");          //allMusic1[0]是所需
	    	
			
	    	String [] allMusic2 = allMusic1[0].split("\\{");
			
	    	int musicArrayLength = allMusic2.length - 1;          //音乐数组的长度
	        for(int i=1; i<musicArrayLength; i++)                 //i从1开始，因为allMusic2数组第一个元素是""
	        {
	        	allMusic2[i] = allMusic2[i].substring(0 , allMusic2[i].length()-2);       //删除掉} ,
	        }
	        //最后一个元素有所不同，没有","了
	        allMusic2[musicArrayLength] = allMusic2[musicArrayLength].substring(0 , allMusic2[musicArrayLength].length() -1);
	        
	        
	        
	        int i = 0;
			for (String musicInfo : allMusic2) 
			{
				if(i == 0){}  //因为第一个元素为""所以不做字符串解析
				else 
				{
					//把数据进行最后解析，以及存储进数据库里面!!!
					String [] musicMsg = musicInfo.split(",");        //对字符串一","解析 ,    musicMsg字符串的形式是"albumid": 1182135  "seconds": 214
					music = new MusicTop();

					
					for (String music1 : musicMsg)
					{

						String []array = music1.split(":");
						
		
						if (array[0].equals("\"albumid\"")) 
						{
							music.setAlbumId(Integer.valueOf(array[1]));
						} 
						else if (array[0].equals("\"downUrl\""))
						{
							array[1] = array[1].substring(1 , array[1].length());
							array[2] = array[2].substring(0 , array[2].length()-1);
							music.setDownUrl(array[1].concat(":" + array[2]));
						}
						else if(array[0].equals("\"seconds\""))
						{
							music.setSeconds(new Integer(array[1]));             //调试的时候发现这里错了!!!
						}
						else if(array[0].equals("\"singerid\""))
						{
							music.setSingerId(Integer.valueOf(array[1]));
						}
						else if(array[0].equals("\"singername\""))
						{
							music.setSingerName(array[1] = array[1].substring(1 , array[1].length()-1));
						}
						else if(array[0].equals("\"songid\""))
						{
							music.setSongId(Integer.valueOf(array[1]));
						}
						else if(array[0].equals("\"songname\""))
						{
							music.setSongName(array[1] = array[1].substring(1 , array[1].length()-1));
						}
						else if(array[0].equals("\"url\""))
						{
							array[1] = array[1].substring(1 , array[1].length());
							array[2] = array[2].substring(0 , array[2].length() - 1);
							music.setUrl(array[1].concat(":" + array[2]));
						}
						
					}
					
					SaveMusicInfo.saveMusicInfo(music);        //把数据存储到数据库中
					/*txt.setText(txt.getText() + String.valueOf(music.getAlbumId()) + "  " + music.getDownUrl() + "  " 
					        +music.getSeconds() +  "   " + String.valueOf(music.getSingerId()) + "  " + 
							music.getSingerName() + "    " +String.valueOf(music.getSongId()) + "  " + music.getSongName() + "   " +
							music.getUrl() + "   " + "\n\n");*/

				}
				i++;
			}
	    

		}

}
