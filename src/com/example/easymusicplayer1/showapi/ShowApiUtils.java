package com.example.easymusicplayer1.showapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 系统工具类�??
 */
public   class ShowApiUtils {
	/**
	 * 给请求签名�??
	 * @param params �?有字符型的请求参�?
	 * @param secret 签名密钥
	 * @param isHmac 是否为HMAC方式加密
	 * @return 签名
	 * @throws IOException
	 */
	public static String signRequest(Map<String, String> params, String secret, boolean isHmac) throws IOException {
		//1.去除file字段 ,同时排序
		TreeSet<String> keys=new TreeSet<String>();
		Iterator<String> it=params.keySet().iterator();
		while(it.hasNext()){
			String k=it.next();
			Object v=params.get(k);
			if(!(v instanceof File))keys.add(k);
		}
		//2.把所有参数名和参数�?�串在一�?
		StringBuilder query = new StringBuilder();
		for (String key : keys) {
			if(key.equals(Constants.SHOWAPI_SIGN))continue;//此字段不参与签名
			String value = params.get(key)+"";
			if (StringUtils.areNotEmpty(key, value)) {
				query.append(key).append(value);
			}
		}
		if (!isHmac) {
			query.append(secret);
		}

		// 3.使用MD5/HMAC加密
		byte[] bytes;
		if (isHmac) {
			bytes = encryptHMAC(query.toString(), secret);
		} else {
			bytes = encryptMD5(query.toString());
		}

		// 4.把二进制转化为大写的十六进制
//		System.out.println(query);
//		System.out.println(byte2hex(bytes));
		return byte2hex(bytes);
	}
	private static byte[] encryptHMAC(String data, String secret) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacMD5");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
		} catch (GeneralSecurityException gse) {
			String msg=getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}

	private static String getStringFromException(Throwable e) {
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		e.printStackTrace(ps);
		try {
			result = bos.toString(Constants.CHARSET_UTF8);
		} catch (IOException ioe) {
		}
		return result;
	}

	private static byte[] encryptMD5(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(Constants.CHARSET_UTF8));
		} catch (GeneralSecurityException gse) {
			String msg = getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}

	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	/**
	 * 获取文件的真实后�?名�?�目前只支持JPG, GIF, PNG, BMP四种图片文件�?
	 * 
	 * @param bytes 文件字节�?
	 * @return JPG, GIF, PNG or null
	 */
	public static String getFileSuffix(byte[] bytes) {
		if (bytes == null || bytes.length < 10) {
			return null;
		}

		if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
			return "GIF";
		} else if (bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
			return "PNG";
		} else if (bytes[6] == 'J' && bytes[7] == 'F' && bytes[8] == 'I' && bytes[9] == 'F') {
			return "JPG";
		} else if (bytes[0] == 'B' && bytes[1] == 'M') {
			return "BMP";
		} else {
			return null;
		}
	}

	/**
	 * 获取文件的真实媒体类型�?�目前只支持JPG, GIF, PNG, BMP四种图片文件�?
	 * 
	 * @param bytes 文件字节�?
	 * @return 媒体类型(MEME-TYPE)
	 */
	public static String getMimeType(byte[] bytes) {
		String suffix = getFileSuffix(bytes);
		String mimeType;

		if ("JPG".equals(suffix)) {
			mimeType = "image/jpeg";
		} else if ("GIF".equals(suffix)) {
			mimeType = "image/gif";
		} else if ("PNG".equals(suffix)) {
			mimeType = "image/png";
		} else if ("BMP".equals(suffix)) {
			mimeType = "image/bmp";
		}else {
			mimeType = "application/octet-stream";
		}

		return mimeType;
	}
	 
}
