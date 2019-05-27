package com.fantasy.app.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;



/**
 * 字符串处理工具
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:27:29
 */
public class StrUtil extends StringUtils{
	
	/**
	 * 是否不为null,""," "
	 * @param str
	 * @return
	 */
	public static boolean isNotBank(String str){
		return !isBlank(str);
	}
	/**
	 * 字符数组是否不为null,是否有数据，数据是否不为null,""," "
	 * @param str
	 * @return
	 */
	public static boolean isNotBank(String[] strs){
		if(strs==null||strs.length==0){
			return false;
		}
		for (String str:strs) {
			if(isBlank(str)){
				return false;
			}
		}
		return true;
	}
	 /**
     * <p>获取数据库主键参数</p>
     *
     * <p>A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.</p>
     *
     * <pre>
     * StrUtil.getPkParam(null)      = null
     * StrUtil.getPkParam("")        = ""
     * StrUtil.getPkParam("111,222,333")      = "'111','222','333'"
     * </pre>
     * @param pkStr 主键参数 格式英文逗号分隔  eg.  111,222,333,444
     */
	public static String getPkParam(String pkStr){
		if(StringUtils.isBlank(pkStr)){
			return pkStr;
		}
		String pkParam = "";
		String[] pks = pkStr.split(",");
		for(String pk:pks){
			pkParam +="'"+pk+"',";
		}
		pkParam = StrUtil.substringBeforeLast(pkParam, ",");
		return pkParam;
	}
	
	
	
	/**
	 * <p>处理从Map中取出的Null值,默认返回空字符串</p>
	 * <pre>
	 * Map: {name=sinba,age=11}
	 * StrUtil.fixNull( map.get("noSuchKey") )  =  ""
	 * StrUtil.fixNull( map.get("name") )       =  "sinba"
	 * StrUtil.fixNull( map.get("age") )        =  "11"
	 * @param o
	 * @return
	 * </pre>
	 */
	public static String fixNull(Object o) {
		return o == null ? "" : o.toString().trim();
	}
	

	/**
	 * 去掉字符串前面的空格
	 * @param original
	 * @return
	 */
	public static String beforeTrim(String original) {
		if(original==null||original.trim().length()==0)return original;
		int len = original.length();
		int st = 0;
		int off = 0;      /* avoid getfield opcode */
		char[] originalValue = original.toCharArray();
		char[] val = Arrays.copyOfRange(originalValue, off, off+len);;    /* avoid getfield opcode */

		while ((st < len) && (val[off + st] <= ' ')) {
		    st++;
		}
		return ((st > 0) || (st < len)) ? original.substring(st, len) : "";
	  }
	/**
	 * 去掉字符串后面的空格
	 * @param original
	 * @return
	 */
	public static String afterTrim(String original) {
		if(original==null||original.trim().length()==0)return original;
		int len = original.length();
		int st = 0;
		int off = 0;      /* avoid getfield opcode */
		char[] originalValue = original.toCharArray();
		char[] val = Arrays.copyOfRange(originalValue, off, off+len);    /* avoid getfield opcode */

		while ((st < len) && (val[off + len - 1] <= ' ')) {
		    len--;
		}
		return ((st > 0) || (st < len)) ? original.substring(st, len) : "";
	  }
	/**
	 * 字符串转list
	 * @param str
	 * @param splitBy
	 * @return
	 */
	public static List<String> splitToList(String str,String splitBy){
		if(StringUtils.isBlank(str))return null;
		String[] arrays = str.split(splitBy);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arrays.length; i++) {
			list.add(arrays[i].trim());
		}
		return list;
	}
	public static boolean isAllNotBlank(String... text){
		if(text!=null&&text.length!=0){
			for (int i = 0; i < text.length; i++) {
				if(!isNotBlank(text[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	   /**
     * This is a string replacement method.
     */
    public final static String replaceString(String source, String oldStr,
            String newStr) {
        StringBuilder sb = new StringBuilder(source.length());
        int sind = 0;
        int cind = 0;
        while ((cind = source.indexOf(oldStr, sind)) != -1) {
            sb.append(source.substring(sind, cind));
            sb.append(newStr);
            sind = cind + oldStr.length();
        }
        sb.append(source.substring(sind));
        return sb.toString();
    }

    /**
     * Replace string
     */
    public final static String replaceString(String source, Object[] args) {
        int startIndex = 0;
        int openIndex = source.indexOf('{', startIndex);
        if (openIndex == -1) {
            return source;
        }

        int closeIndex = source.indexOf('}', startIndex);
        if ((closeIndex == -1) || (openIndex > closeIndex)) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(startIndex, openIndex));
        while (true) {
            String intStr = source.substring(openIndex + 1, closeIndex);
            int index = Integer.parseInt(intStr);
            sb.append(args[index]);

            startIndex = closeIndex + 1;
            openIndex = source.indexOf('{', startIndex);
            if (openIndex == -1) {
                sb.append(source.substring(startIndex));
                break;
            }

            closeIndex = source.indexOf('}', startIndex);
            if ((closeIndex == -1) || (openIndex > closeIndex)) {
                sb.append(source.substring(startIndex));
                break;
            }
            sb.append(source.substring(startIndex, openIndex));
        }
        return sb.toString();
    }

    /**
     * Replace string.
     */
    public final static String replaceString(String source,
            Map<String, Object> args) {
        int startIndex = 0;
        int openIndex = source.indexOf('{', startIndex);
        if (openIndex == -1) {
            return source;
        }

        int closeIndex = source.indexOf('}', startIndex);
        if ((closeIndex == -1) || (openIndex > closeIndex)) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(startIndex, openIndex));
        while (true) {
            String key = source.substring(openIndex + 1, closeIndex);
            Object val = args.get(key);
            if (val != null) {
                sb.append(val);
            }

            startIndex = closeIndex + 1;
            openIndex = source.indexOf('{', startIndex);
            if (openIndex == -1) {
                sb.append(source.substring(startIndex));
                break;
            }

            closeIndex = source.indexOf('}', startIndex);
            if ((closeIndex == -1) || (openIndex > closeIndex)) {
                sb.append(source.substring(startIndex));
                break;
            }
            sb.append(source.substring(startIndex, openIndex));
        }
        return sb.toString();
    }

    /**
         * This method is used to insert HTML block dynamically
         *
         * @param source the HTML code to be processes
         * @param bReplaceNl if true '\n' will be replaced by <br>
         * @param bReplaceTag if true '<' will be replaced by &lt; and 
         *                          '>' will be replaced by &gt;
         * @param bReplaceQuote if true '\"' will be replaced by &quot; 
         */
    public final static String formatHtml(String source, boolean bReplaceNl,
            boolean bReplaceTag, boolean bReplaceQuote) {

        StringBuilder sb = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char c = source.charAt(i);
            switch (c) {
            case '\"':
                if (bReplaceQuote)
                    sb.append("&quot;");
                else
                    sb.append(c);
                break;

            case '<':
                if (bReplaceTag)
                    sb.append("&lt;");
                else
                    sb.append(c);
                break;

            case '>':
                if (bReplaceTag)
                    sb.append("&gt;");
                else
                    sb.append(c);
                break;

            case '\n':
                if (bReplaceNl) {
                    if (bReplaceTag)
                        sb.append("&lt;br&gt;");
                    else
                        sb.append("<br>");
                } else {
                    sb.append(c);
                }
                break;

            case '\r':
                break;

            case '&':
                sb.append("&amp;");
                break;

            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Pad string object
     */
    public final static String pad(String src, char padChar, boolean rightPad,
            int totalLength) {

        int srcLength = src.length();
        if (srcLength >= totalLength) {
            return src;
        }

        int padLength = totalLength - srcLength;
        StringBuilder sb = new StringBuilder(padLength);
        for (int i = 0; i < padLength; ++i) {
            sb.append(padChar);
        }

        if (rightPad) {
            return src + sb.toString();
        } else {
            return sb.toString() + src;
        }
    }

    /**
     * Get hex string from byte array
     */
    public final static String toHexString(byte[] res) {
        StringBuilder sb = new StringBuilder(res.length << 1);
        for (int i = 0; i < res.length; i++) {
            String digit = Integer.toHexString(0xFF & res[i]);
            if (digit.length() == 1) {
                sb.append('0');
            }
            sb.append(digit);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Get byte array from hex string
     */
    public final static byte[] toByteArray(String hexString) {
        int arrLength = hexString.length() >> 1;
        byte buff[] = new byte[arrLength];
        for (int i = 0; i < arrLength; i++) {
            int index = i << 1;
            String digit = hexString.substring(index, index + 2);
            buff[i] = (byte) Integer.parseInt(digit, 16);
        }
        return buff;
    }
	public static void main(String[] args) {
		System.out.println(isAllNotBlank("1111"," s  ","5"));
		/*System.out.println(StrUtil.afterTrim("  aa aa  "));
		System.out.println(StrUtil.beforeTrim("  aa aa  "));
		System.out.println(StrUtil.afterTrim("    "));
		System.out.println(StrUtil.beforeTrim("    "));
		System.out.println(StrUtil.afterTrim(""));
		System.out.println(StrUtil.beforeTrim(""));
		System.out.println(StrUtil.afterTrim(null));
		System.out.println(StrUtil.beforeTrim(null));
		System.out.println("-----------------------");
		System.out.println(StrUtil.getPkParam(null));
		System.out.println(StrUtil.getPkParam(""));
		System.out.println(StrUtil.getPkParam("111,222,333"));
		System.out.println("-----------------------");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("name", "sinba");
		data.put("age", 11);
		System.out.println(StrUtil.fixNull(data.get("noSuchKey")));
		System.out.println(StrUtil.fixNull(data.get("name")));
		System.out.println(StrUtil.fixNull(data.get("age")));*/
	}
}
