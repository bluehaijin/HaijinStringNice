package xyz.haijin.nice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RemoveSpecialString {
	protected static String removeSpecialString(String oldstr) {
		String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].·<>》《/?~！@#￥%……&*（）——+|{}【】‘；：”\"“’。， 、？]";
		//被替换的字符正则表达式
		String goal = "";//替换的字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(oldstr);
		String newString = m.replaceAll(goal).trim();
		return newString;
	}

}

