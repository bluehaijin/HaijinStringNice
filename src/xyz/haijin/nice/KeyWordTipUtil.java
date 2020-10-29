package xyz.haijin.nice;

import java.util.ArrayList;
import java.util.List;

public class KeyWordTipUtil {
	private List<String> keyWordStrs = new ArrayList<String>();
	
	public List<String> getKeyWordStrs() {
		return keyWordStrs;
	}

	private  String keyWord = "div";
	
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	protected  String getkeyWordStr(String html) {
		return getkeyWordStr(html, "<"+keyWord+">", "</"+keyWord+">");
	}
	
	protected  String getkeyWordStr(String html,String end_html) {
		return getkeyWordStr(html, "<"+keyWord+">", end_html);
	}
	
	protected  String getkeyWordStr(String html,String start_html,String end_html) {
		String div = "";
		if(html.contains("<"+keyWord+" ")) {
			start_html = "<"+keyWord+" "+ TextUtils.getStringBetweenKeys(html, "<"+keyWord+" ", ">")+">";
			if(TextUtils.getStringBeforeKey(html, start_html).contains("<"+keyWord+">")) {
				start_html = "<"+keyWord+">";
			}
		}
		div = TextUtils.getStringBetweenKeys(html, start_html, end_html);
		if(div.contains("<"+keyWord+" ")||div.contains("<"+keyWord+">")) {
			div = getkeyWordStr(div,start_html,end_html);
		}
		if(!div.contains("<"+keyWord+"")) {
			div = start_html+div+"</"+keyWord+">";
		}
		return div;
	}
	
	protected String getkeyWordEntity(String minDiv) {
		String div = new String();
		String keyValue = "<"+keyWord+">";
		if(minDiv.contains("<"+keyWord+" ")) {
			keyValue = "<"+keyWord+" "+TextUtils.getStringBetweenKeys(minDiv, "<"+keyWord+" ", ">")+">";

		}
		div = TextUtils.getStringBetweenKeys(minDiv, keyValue, "</"+keyWord+">");
		return div;
	}
	
	public void returnKeyWordTipHtml(String html) {
		keyWordStrs.add(getkeyWordEntity(getkeyWordStr(html)));
		String newhtml = TextUtils.getStringBeforeKey(html, getkeyWordStr(html))+TextUtils.getStringAfterKey(html, getkeyWordStr(html));
		String keyValue = "<"+keyWord+">";
			if( html.contains("<"+keyWord+" ")) {
				keyValue = "<"+keyWord+" "+TextUtils.getStringBetweenKeys(html, "<"+keyWord+" ", ">")+">";
					if(TextUtils.getStringBeforeKey(html, keyValue).contains("<"+keyWord+">")) {
						keyValue = "<"+keyWord+">";
					}
			}

			String lastDivContent = TextUtils.getStringAfterKey(newhtml, keyValue);
			if(lastDivContent.contains("<"+keyWord+"")) {
				returnKeyWordTipHtml(newhtml);
			}else {
				keyWordStrs.add(getkeyWordEntity(getkeyWordStr(newhtml)));
			}
	}
	
	public static void main(String[] args) {
		String html = "<li>123</li><li>456<li>xxx</li></li>";
//		System.out.println(html);
		KeyWordTipUtil keyWordTipUtil = new KeyWordTipUtil();
		keyWordTipUtil.setKeyWord("li");
		keyWordTipUtil.returnKeyWordTipHtml(html);
		System.out.println(keyWordTipUtil.getKeyWordStrs().size());
		for(String str : keyWordTipUtil.getKeyWordStrs()) {
			System.out.println(str);
//			System.out.println(TextUtils.getStringBetweenKeys(str, "title=\"", "\">"));
		}
	}
}
