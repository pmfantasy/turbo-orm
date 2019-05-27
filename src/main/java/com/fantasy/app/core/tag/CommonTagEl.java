package com.fantasy.app.core.tag;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.user.AuthUserType;
import com.fantasy.app.core.tag.CommonStaticCode.ENTITY_STATUS;
import com.fantasy.app.core.tag.CommonStaticCode.OPENCLOSE;
import com.fantasy.app.core.tag.CommonStaticCode.SEX;
import com.fantasy.app.core.tag.CommonStaticCode.SUCCESSFAIL;
import com.fantasy.app.core.tag.CommonStaticCode.YESNO;
import com.fantasy.app.core.util.ComputeUtil;
import com.fantasy.app.core.util.DateUtil;
import com.fantasy.app.core.util.UUIDGenerator;



/**
 * el 表达式
 * 
 * @日期：2013-1-8下午3:01:27
 * @作者：公众号：18岁fantasy
 */
public class CommonTagEl {
	
	static{
		/**
		 * 注册基本枚举类型
		 */
		CT.registEnumCodeTableWithCodeNameField(SEX.class,null, null);
		CT.registEnumCodeTableWithCustomCodeNameField(SUCCESSFAIL.class,"status","desc",null,null);
		CT.registEnumCodeTableWithCustomCodeNameField(YESNO.class,"status","desc",null,null);
		CT.registEnumCodeTableWithCodeNameField(OPENCLOSE.class,null,null);
		CT.registEnumCodeTableWithCodeNameField(AuthUserType.class,null, null);
		CT.registEnumCodeTableWithCodeNameField(ENTITY_STATUS.class, null, null);
	}

	/**
	 * 性别code-str转换 useage:${si:sex("1")} or ${si:sex(vo.sex)}
	 * 
	 * @param code
	 * @return
	 */
	public static String sex(String code) {
		if(SEX.MALE.getCode().equalsIgnoreCase(code)){
			return SEX.MALE.getName();
		}
		if(SEX.FMALE.getCode().equalsIgnoreCase(code)){
			return SEX.FMALE.getName();
		}
		
		return "";
	}

	/**
	 * useage:${si:splitStrVal("1","1,2,3,4,5",",")}
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String splitStrVal(String index, String splitStr, String split)
			throws Exception {
		if (!StringUtils.hasText(splitStr))
			return "";
		int vindex = -1;
		try {
			vindex = Integer.parseInt(index);
		} catch (Exception e) {
			throw e;
		}
		String[] array = splitStr.split("\\" + split);
		if (vindex < 0 || vindex > array.length - 1)
			return "";

		return array[vindex];
	}
	/**
	 * useage:${si:splitStrVal("1","1,2,3,4,5")}
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String strVal(String index, String splitStr) throws Exception {
		return splitStrVal(index, splitStr, ",");
	}

	/**
	 * useage:${si:statusStr("1")}
	 * 
	 * @param statusCode
	 *            "0" or "1"
	 * @return
	 */
	@Deprecated
	public static String statusStr(String statusCode) {
		if (CommonStaticCode.OPENCLOSE.COLSE.getStatus().equals(statusCode)) {
			return CommonStaticCode.OPENCLOSE.COLSE.getDesc();
		}
		if (CommonStaticCode.OPENCLOSE.OPEN.getStatus().equals(statusCode)) {
			return CommonStaticCode.OPENCLOSE.OPEN.getDesc();
		}
		return "";
	}

	/**
	 * useage:${si:yesNoStr("1")}
	 * 
	 * @param statusCode
	 *            "0" or "1"
	 * @return
	 */
	@Deprecated
	public static String yesNoStr(String statusCode) {

		if (CommonStaticCode.YESNO.YES.getStatus().equals(statusCode)) {
			return CommonStaticCode.YESNO.YES.getDesc();
		}
		if (CommonStaticCode.YESNO.NO.getStatus().equals(statusCode)) {
			return CommonStaticCode.YESNO.NO.getDesc();
		}
		return "";
	}

	/**
	 * useage:${si:succesFailStr("1")}
	 * 
	 * @param statusCode
	 *            "0" or "1"
	 * @return
	 */
	@Deprecated
	public static String succesFailStr(String statusCode) {
		if (CommonStaticCode.SUCCESSFAIL.SUCCESS.getStatus().equals(statusCode)) {
			return CommonStaticCode.SUCCESSFAIL.SUCCESS.getDesc();
		}
		if (CommonStaticCode.SUCCESSFAIL.FAIL.getStatus().equals(statusCode)) {
			return CommonStaticCode.SUCCESSFAIL.FAIL.getDesc();
		}
		return "";
	}
	/**
     * 创建下拉框中options列表
     * @param enumClassName 已注册的码表类型
     * @param selectedCode 默认选择项
     * @param excludeCodeStr 不显示项
     * @param extName  option中额外显示的属性如：<option ext1=xxx ext2=xxx>
     * @return
     */
    public static String ctoptionExt(String enumClassName, String selectedCode,String excludeStr,String extName) {
    	return ctoptionExt(enumClassName, selectedCode, true, excludeStr, extName);
    }
    /**
     * 创建下拉框中options列表  ，
     * @param enumClassName 已注册的码表类型，必须已经在{@CT}
     * @param selectedCode 默认选择项
     * @param includeStr 显示项 
     * @param extName  option中额外显示的属性如：<option ext1=xxx ext2=xxx>
     * @return
     */
    public static String ctoptionExt2(String enumClassName, String selectedCode,String includeStr,String extName) {
    	return ctoptionExt(enumClassName, selectedCode, false, includeStr, extName);
    }
    /**
     *  显示给定条件下的码表下拉框
     * @param enumClassName
     * @param selectedCode
     * @param exClude
     * @param customCodeStr 
     * @param extName
     * @return
     */
    private static String ctoptionExt(String enumClassName, String selectedCode,boolean exClude,String customCodeStr,String extName) {
        Map<String, Map<String, String>> codeTable = CT.getCodeTable(enumClassName);
        if (StringUtils.hasText(selectedCode))
            selectedCode = selectedCode.trim();
        StringBuilder rp = new StringBuilder();
        List<String> customCodes = new ArrayList<String>();
        if(StringUtils.hasText(customCodeStr)){
        	customCodes.addAll(Arrays.asList(customCodeStr.split(",")));
        }
        if (codeTable != null) {
            for (Iterator<String> iterator = codeTable.keySet().iterator(); iterator.hasNext(); ) {
            	String code = iterator.next();
            	//排除条件下，在给定排除范围内
                if(exClude&&customCodes.contains(code))continue;
                //包含条件下，不在给定包含范围内
                if(!exClude&&!customCodes.contains(code))continue;
                Map<String, String> fields = codeTable.get(code);
                String name = fields.get(CT.CT_DEFAULT_NAME_KEY);
                rp.append("<option value=\"").append(code).append("\" ")
                        .append(code.equalsIgnoreCase(selectedCode) ? "selected" : "");
                if(StringUtils.hasText(extName)){
	                String[] extNames = extName.split(",");
	                for (int i = 0; i < extNames.length; i++) {
						rp.append(" "+extNames[i]+"=\""+fields.get(extNames[i])+"\"");
					}
                }
                rp.append(">").append(name)
                        .append("</option>\r\n");
            }
        }
        return rp.toString();
    }
    /**
     * 创建下拉框的通用方法
     *
     * @param enumClassName 在{@link CT}}里面注册过的码表的class名称
     * @param selectedCode  已选择的code
     * @param excludeCode 排除的code ,号隔开
     * @return
     */
    public static String ctoption(String enumClassName, String selectedCode,String excludeCodeStr) {
        return ctoptionExt(enumClassName, selectedCode, excludeCodeStr, null);
    }
    /**
     * 创建下拉框的通用方法
     *
     * @param enumClassName 在{@link CT}}里面注册过的码表的class名称
     * @param selectedCode  已选择的code
     * @return
     */
    public static String ctoption(String enumClassName, String selectedCode) {
    	return ctoptionExt(enumClassName, selectedCode, null, null);
    }
    /**
     * 根据给定的code获取name
     *
     * @param enumClassName
     * @param selectedCode
     * @return
     */
    public static String ctname(String enumClassName, String selectedCode) {
    	Map<String, Map<String, String>> codeTable = CT.getCodeTable(enumClassName);
        if (StringUtils.hasText(selectedCode))
            selectedCode = selectedCode.trim();
        if (codeTable != null) {
            for (Iterator<String> iterator = codeTable.keySet().iterator(); iterator.hasNext(); ) {
            	String code = iterator.next();
            	if(code.equalsIgnoreCase(selectedCode)){
	                Map<String, String> fields = codeTable.get(code);
	                return  fields.get(CT.CT_DEFAULT_NAME_KEY);
            	}
            }
        }
        return "";
    }
	/**
	 * useage:${si:createOpenColseOption("1")}
	 * 
	 * @param roleStatus
	 * @return
	 */
	public static String createOpenColseOption(String selectedCode) {
		StringBuilder rp = new StringBuilder();
		CommonStaticCode.OPENCLOSE[] roleStatus = CommonStaticCode.OPENCLOSE.values();
		for (CommonStaticCode.OPENCLOSE oc : roleStatus) {
			rp.append("<option value=\"")
					.append(oc.getStatus())
					.append("\" ")
					.append(oc.getStatus().equalsIgnoreCase(selectedCode) ? "selected"
							: "").append(">").append(oc.getDesc())
					.append("</option>\r\n");
		}
		return rp.toString();
	}
	/**
	 * useage:${si:createYesNoOption("1")}
	 * 
	 * @param roleStatus
	 * @return
	 */
	public static String createYesNoOption(String selectedCode) {
		StringBuilder rp = new StringBuilder();
		CommonStaticCode.YESNO[] yss = CommonStaticCode.YESNO.values();
		for (CommonStaticCode.YESNO ys : yss) {
			rp.append("<option value=\"")
					.append(ys.getStatus())
					.append("\" ")
					.append(ys.getStatus().equalsIgnoreCase(selectedCode) ? "selected"
							: "").append(">").append(ys.getDesc())
					.append("</option>\r\n");
		}
		return rp.toString();
	}

	public static FontMetrics metrics = new FontMetrics(new Font(
			"Microsoft 宋体", Font.PLAIN, 12)) {
		private static final long serialVersionUID = 1L;
	};
	// 表格的字体
	public static final int zh_px = 12;// 根据字体计算出的一个汉字的像素
	public static final int en_px = 7;// 根据字体计算出的一个英文的像素
	public static Map<String, Integer> punctuationLength = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("!", 3);
			put("\"", 4);
			put("#", 7);
			put("$", 7);
			put("%", 11);
			put("&", 8);
			put("'", 2);
			put("(", 4);
			put(")", 4);
			put("*", 5);
			put("+", 7);
			put(",", 3);
			put("-", 4);
			put(".", 3);
			put("/", 3);
			put("0", 7);
			put("1", 7);
			put("2", 7);
			put("3", 7);
			put("4", 7);
			put("5", 7);
			put("6", 7);
			put("7", 7);
			put("8", 7);
			put("9", 7);
			put(":", 3);
			put(";", 3);
			put("<", 7);
			put("=", 7);
			put(">", 7);
			put("?", 7);
			put("@", 12);
			put("A", 7);
			put("B", 8);
			put("C", 9);
			put("D", 9);
			put("E", 8);
			put("F", 7);
			put("G", 9);
			put("H", 9);
			put("I", 3);
			put("J", 6);
			put("K", 8);
			put("L", 7);
			put("M", 9);
			put("N", 9);
			put("O", 9);
			put("P", 8);
			put("Q", 9);
			put("R", 9);
			put("S", 8);
			put("T", 7);
			put("U", 9);
			put("V", 7);
			put("W", 11);
			put("X", 7);
			put("Y", 7);
			put("Z", 7);
			put("[", 3);
			put("\\", 3);
			put("]", 3);
		}
	};
	public static final String DEFAULT_HIDDENSTR = "...";
	public static final int DEFAULT_TD_PX = 200;

	public static String lmitStr(String rawStr) {
		return lmitStr(rawStr, DEFAULT_TD_PX, DEFAULT_HIDDENSTR);
	}

	public static String lmitStrPx(String rawStr, Integer px) {
		if (px == null || px <= 0)
			px = DEFAULT_TD_PX;
		return lmitStr(rawStr, px, DEFAULT_HIDDENSTR);
	}

	/**
	 * rawStr 需要进行截取的日期字符串
	 * 
	 * @param rawStr
	 * @param start
	 * @param end
	 * @return
	 */
	public static String lmitDateStr(String rawStr, Integer start, Integer end,Boolean bool) {
		String hiddenStr = DEFAULT_HIDDENSTR;
		if (start == null || start <= 0)
			start = 0;
		if (end == null || end <= 0)
			end = rawStr.length() - 1;

		return "<span tooltipid=\"" + UUIDGenerator.getUUID() + "\" rawstr=\""
				+ rawStr + "\" >" + rawStr.substring(start, end) + (bool?hiddenStr:"")
				+ "</span>";
	}

	/**
	 * @param rawStr
	 *            需要进行截取的字符串
	 * @param px
	 *            指定像素
	 * @param hiddenStr
	 *            省略字符占位符 默认...
	 * @return 截取后的字符串
	 */
	public static String lmitStr(String rawStr, int px, String hiddenStr) {
		if (!StringUtils.hasText(rawStr) || px <= 0)
			return rawStr;
		StringBuffer rsString = new StringBuffer();
		// 先计算下总像素如果小于指定像素 直接返回
		int allpx = getWidth(rawStr);
		if (allpx <= px)
			return rawStr;
		int sumPx = 0;
		// 如果大于，逐个字符进行像素想加，超过部分用hiddenStr代替
		char[] chars = rawStr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int cpx = 0;
			if (isChinese(chars[i])) {
				cpx = zh_px;
			} else {
				if (punctuationLength.containsKey(chars[i] + "")) {
					cpx = punctuationLength.get(chars[i] + "");
				} else {
					cpx = en_px;
				}
			}
			if ((sumPx + cpx) > px-5)
				break;
			sumPx += cpx;
			rsString.append(chars[i]);
			
		}
		if (!StringUtils.hasText(hiddenStr))
			hiddenStr = DEFAULT_HIDDENSTR;
		return "<span tooltipid=\"" + UUIDGenerator.getUUID() + "\" rawstr=\""
				+ rawStr + "\" >" + rsString.toString() + hiddenStr + "</span>";
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static int getWidth(String str) {
		Rectangle2D bounds = metrics.getStringBounds(str, null);
		int widthInPixels = (int) bounds.getWidth();
		return widthInPixels;
	}
	
	
	/**
	 * 将XML转义在HTML页面显示源码
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str) {
		String html = "";
		try{
			html = StringEscapeUtils.escapeHtml(str);
		}catch(Exception e){
			//ignored....
		}
		return html;
	}
	/**根据byte字符串优化显示
	 * ${si:formatByte("1000000")}
	 * @param byteVal
	 * @return
	 */
	public static String formatByte(String byteVal){
		return ComputeUtil.formatByte(byteVal);
	}

	/**
	 * 是否是用户初始化的资源id useage:${si:init("pub_sysmgr_list")}
	 * 
	 * @param resourceId
	 *            资源id
	 * @return
	 */
	public static boolean init(List<String> initResIdPath, String resourceId) {
		return initResIdPath != null && initResIdPath.contains(resourceId);
	}
	/**
	 * 时间格式化
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String fmDate(Date date,String pattern){
		return DateUtil.formatDate(date, pattern);
	}
}
