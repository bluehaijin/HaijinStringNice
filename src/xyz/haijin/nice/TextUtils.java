package xyz.haijin.nice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class TextUtils {
	
	private static			Map<String, Integer>	ipTransMap		= null;
	protected static final	Pattern					numericPattern	= Pattern.compile("\\d+(\\.\\d+)?");
	protected static final	Pattern					integerPattern	= Pattern.compile("\\d+");
	
	public static boolean isEmpty(String str) {
		return str == null && "".equals(str);
	}

	public static String getBase64(String srcString) { 
		return DatatypeConverter.printBase64Binary(srcString.getBytes());
	}

	public static String getFromBase64(String srcString, String type) throws UnsupportedEncodingException { 
		return new String(DatatypeConverter.parseBase64Binary(srcString), type);
	}

	protected void initMap(Map<String, String> map, String value) {
		String[] lines = value.split("\n");
		for(String line : lines) {
			String[] params = line.split("\t");
			map.put(params[0], params[1]);
		}
	}

	// 截取多个指定字符之间�?短的字符�?
	public static String getShortStringBetweenKeys(String str, String first, String[] secondKeys) {
		return getShortStringBetweenKeys(str, first, secondKeys, true);
	}

	// 截取多个指定字符之间�?短的字符�?
	public static String getShortStringBetweenLastKeys(String str, String first, String[] secondKeys) {
		return getShortStringBetweenKeys(str, first, secondKeys, false);
	}

	// 截取多个指定字符之间�?短的字符�?
	protected static String getShortStringBetweenKeys(String str, String first, String[] secondKeys, boolean asc) {
		if(str != null && first != null && secondKeys != null) { // 判断�?有参数不为空
			// 判断第一个查找串位置
			int firstIndex = -1;
			if(asc) {
				firstIndex = str.indexOf(first);
			} else {
				firstIndex = str.lastIndexOf(first);
			}
			if(firstIndex != -1) {
				// 遍历第二组查找串位置�?小的�?
				int secondIndex = -1;
				firstIndex += first.length();
				for(int i = 0; i < secondKeys.length; i++) {
					int index = str.indexOf(secondKeys[i], firstIndex);
					if(index != -1 && (secondIndex == -1 || index < secondIndex)) {
						secondIndex = index;
					}
				}
				if(secondIndex == -1) {
					secondIndex = str.length();
				}
				str = str.substring(firstIndex, secondIndex);
			}
		}
		return str;
	}

	// 截取指定字符之间字符�?
	public static String getStringBetweenKeys(String str, String first, String second) {
		if(str != null && first != null && second != null) { // 判断�?有参数不为空
			// 判断第一个查找串位置
			int firstIndex = str.indexOf(first);
			if(firstIndex != -1) {
				firstIndex += first.length();
				int secondIndex = str.indexOf(second, firstIndex);
				if(secondIndex == -1) {
					secondIndex = str.length();
				}
				str = str.substring(firstIndex, secondIndex);
			} else {
				str = "";
			}
		}
		return str;
	}

	// 截取指定字符之间字符�?
	public static String getStringBetweenLastKeys(String str, String first, String second) {
		if(str != null && first != null && second != null) { // 判断�?有参数不为空
			// 判断第一个查找串位置
			int firstIndex = str.lastIndexOf(first);
			if(firstIndex != -1 && first.equals(second)) {
				firstIndex = str.lastIndexOf(first, firstIndex - first.length());
			}
			if(firstIndex != -1) {
				firstIndex += first.length();
				int secondIndex = str.indexOf(second, firstIndex);
				if(secondIndex == -1) {
					secondIndex = str.length();
				}
				str = str.substring(firstIndex, secondIndex);
			}
		}
		return str;
	}

	// 截取指定字符后的字符�?
	public static String getStringAfterKey(String str, String key) {
		return getStringAfterKey(str, key, 0);
	}

	// 截取指定字符后的字符�?
	protected static String getStringAfterKey(String str, String key, int fromIndex) {
		if(str != null && key != null) {
			str = str.substring(str.indexOf(key, fromIndex) + key.length());
		}
		return str;
	}

	// 截取指定�?后一个字符后的字符串
	public static String getStringAfterLastKey(String str, String key) {
		if(str != null && key != null) {
			str = str.substring(str.lastIndexOf(key) + key.length());
		}
		return str;
	}

	// 截取指定字符前的字符�?
	public static String getStringBeforeKey(String str, String key) {
		if(str != null && key != null && str.indexOf(key) != -1) {
			str = str.substring(0, str.indexOf(key));
		}
		return str;
	}
	public static String getStringBeforeKeys(String str, String ... keys) {
		// 遍历查找串位置最小的�?
		int firstIndex = -1;
		for(int i = 0; i < keys.length; i++) {
			int index = str.indexOf(keys[i]);
			if(index != -1 && (firstIndex == -1 || index < firstIndex)) {
				firstIndex = index;
			}
		}
		if(firstIndex == -1) {
			firstIndex = str.length();
		}
		str = str.substring(0, firstIndex);
		return str;
	}

	public static String getStringBeforeLastKey(String str, String key) {
		if(str != null && key != null && str.lastIndexOf(key) != -1) {
			str = str.substring(0, str.indexOf(key));
		}
		return str;
	}

	private String intToHex(int num) {
		String str = Integer.toHexString(num);
		if(str.length() < 2) {
			str = "0" + str;
		}
		return str;
	}

	protected String encodeUnicodeChars(String s) {
		try {
			StringBuffer out = new StringBuffer();
			byte[] bytes = s.getBytes("unicode");
			for (int i = 2; i < bytes.length - 1; i += 2) {

				if(bytes[i] != 0) {
					out.append("\\u");
					out.append(intToHex(bytes[i] & 0xff));
					out.append(intToHex(bytes[i + 1] & 0xff));
				} else {
					if(bytes[i + 1] == '/') {
						out.append("\\");
					}
					out.append((char)bytes[i + 1]);
				}
			}
			return out.toString();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean write(String fileName, String str) {
		boolean success = false;
		try{
			File outFile = new File(fileName);
			if(!outFile.exists()) {
				outFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName,true), "utf-8"));
			bw.write(str);
			bw.close();
			success = true;
		} catch(Exception e) {
		}
		return success;
	}

	public static String readEntyFile(String fileName) {
		String childPath = null;
		try{
			StringBuffer buffer = new StringBuffer();
			InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName),"utf-8");
			BufferedReader br = new BufferedReader(isr); 
			String str;
			while((str = br.readLine()) != null){
				buffer.append(str);
				buffer.append('\r');
			}
			childPath = buffer.toString();
			br.close();
			isr.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return childPath;
	}

	private static void initIpMap() {
		if(ipTransMap == null) {
			ipTransMap = new HashMap<String, Integer>();
			for(Integer i = 0; i < 256; i++) {
				ipTransMap.put(i.toString(), i);
			}
		}
	}
	public static int ip2Int(String ip) {
		int result = 0;
		initIpMap();
        StringTokenizer ipToken = new StringTokenizer(ip, ".");
		if(ipToken.countTokens() == 4) {
			for(int i = 0; i < 4; i++) {
				Integer tokenValue = ipTransMap.get(ipToken.nextToken());
				if(tokenValue != null) {
					result += tokenValue << (24 - i * 8);
				}
			}
		}
		return result;
	}

	public static boolean isNumeric(String str){
		return numericPattern.matcher(str).matches();
	}

	public static boolean isInteger(String str){
		return integerPattern.matcher(str).matches();
	}

	public static boolean isInteger(char chr){
		return (chr >= '0' && chr <= '9');
	}

	public static <T> T getNotNull(T first, T second) {
		return first != null ? first : second;
	}

}
