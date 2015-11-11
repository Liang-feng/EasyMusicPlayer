package com.example.easymusicplayer1.utility;

import com.example.easymusicplayer1.db.SaveMusicInfo;
import com.example.easymusicplayer1.model.MusicTop;
import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 *   ���������緵�ص����ݣ����Ұ����ݴ洢�����ݿ���!!!
 * @author feng
 *
 */
public class DataDipose {

		private static  MusicTop music;
		
		/**
		 * ���½������̣����ܽ��ĵ�!!!!
		 * �������������ݷ�����music������!!!
		 * @param response
		 */
		@SuppressLint("UseValueOf")
		public static void dealString(String response )
		{
			//�Ⱥ�"[" , "]"�ָ��ַ���    ,�����ַ���������"[" , "]"
			String [] allMusic = response.split("\\[");    
			Toast.makeText(MyApplication.getContext() , String.valueOf(allMusic.length) , Toast.LENGTH_LONG).show();
			String [] allMusic1 = allMusic[1].split("\\]");          //allMusic1[0]������
	    	
			
	    	String [] allMusic2 = allMusic1[0].split("\\{");
			
	    	int musicArrayLength = allMusic2.length - 1;          //��������ĳ���
	        for(int i=1; i<musicArrayLength; i++)                 //i��1��ʼ����ΪallMusic2�����һ��Ԫ����""
	        {
	        	allMusic2[i] = allMusic2[i].substring(0 , allMusic2[i].length()-2);       //ɾ����} ,
	        }
	        //���һ��Ԫ��������ͬ��û��","��
	        allMusic2[musicArrayLength] = allMusic2[musicArrayLength].substring(0 , allMusic2[musicArrayLength].length() -1);
	        
	        
	        
	        int i = 0;
			for (String musicInfo : allMusic2) 
			{
				if(i == 0){}  //��Ϊ��һ��Ԫ��Ϊ""���Բ����ַ�������
				else 
				{
					//�����ݽ������������Լ��洢�����ݿ�����!!!
					String [] musicMsg = musicInfo.split(",");        //���ַ���һ","���� ,    musicMsg�ַ�������ʽ��"albumid": 1182135  "seconds": 214
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
							music.setSeconds(new Integer(array[1]));             //���Ե�ʱ�����������!!!
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
					
					SaveMusicInfo.saveMusicInfo(music);        //�����ݴ洢�����ݿ���
					/*txt.setText(txt.getText() + String.valueOf(music.getAlbumId()) + "  " + music.getDownUrl() + "  " 
					        +music.getSeconds() +  "   " + String.valueOf(music.getSingerId()) + "  " + 
							music.getSingerName() + "    " +String.valueOf(music.getSongId()) + "  " + music.getSongName() + "   " +
							music.getUrl() + "   " + "\n\n");*/

				}
				i++;
			}
	    

		}

}
