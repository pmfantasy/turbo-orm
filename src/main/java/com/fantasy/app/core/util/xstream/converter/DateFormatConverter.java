package com.fantasy.app.core.util.xstream.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fantasy.app.core.util.StrUtil;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
/**
 * 时间处理
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:20:48
 */
public class DateFormatConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Date.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		writer.setValue(dateFormat.format((Date) source));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String value = reader.getValue();
			if(StrUtil.isNotBlank(value)){
				return dateFormat.parse(reader.getValue());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
