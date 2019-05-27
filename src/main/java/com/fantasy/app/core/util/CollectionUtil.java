package com.fantasy.app.core.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 集合处理工具类
 * @日期：2013-3-5下午4:54:46
 * @作者：公众号：18岁fantasy
 */
public class CollectionUtil {

	/**
	 * 集合是否有数据
	 * @param collection
	 * @return
	 */
	public static boolean hasElement(@SuppressWarnings("rawtypes") Collection collection) {
		return (collection != null && !collection.isEmpty());
	}
	/**
	 * 集合是否有数据
	 * @param collection
	 * @return
	 */
	public static boolean hasElement(@SuppressWarnings("rawtypes") Map collection) {
		return (collection != null && !collection.isEmpty());
	}
	
	/**
	 * 初始化一个List，其泛型为Map<String,Object>
	 * @return
	 */
	public static List<Map<String, Object>> initMapList() {
		return new ArrayList<Map<String, Object>>();
	}
	
	
	/**
	 * 在给定的链表中在指定的元素前面插入新的元素
	 * 说明:尽量保持链表中的元素不重复，假如元素重复，匹配第一个查找找的元素，假如找不到匹配元素，插入在链表最前方。
	 * @param list
	 * @param targetElement --目标元素
	 * @param insertElement --插入元素
	 * e.g. [foo,bar,fooBar]
	 * insertBeforeElement([foo,bar,fooBar], "bar", "newValue")   ==>  [foo,newValue,bar,fooBar]
	 * insertBeforeElement([foo,bar,fooBar], "foo", "newValue")   ==>  [newValue,foo,bar,fooBar]
	 * insertBeforeElement([foo,bar,fooBar], "noFoo", "newValue") ==>  [newValue,foo,bar,fooBar]
	 * insertBeforeElement([foo,bar,foo,fooBar,foo,bar,bar,bar], "bar", "newValue") ==>  [foo,newValue,bar,foo,fooBar,foo,bar,bar,bar]
	 */
	@SuppressWarnings("unchecked")
	public static void insertBeforeElement(@SuppressWarnings("rawtypes")List list, Object targetElement, Object insertElement) {
		//System.out.println("UP：" + list);
		//System.out.println("targetElement：" + targetElement);
		if (!hasElement(list)) {
			throw new IllegalArgumentException("list can not be null");
		}
		if (targetElement == null) {
			throw new IllegalArgumentException("targetElement can not be null");
		}
		if (insertElement == null) {
			throw new IllegalArgumentException("insertElement can not be null");
		}
		int index = list.indexOf(targetElement);
		if (index == -1 || index == 0) {
			list.add(0, insertElement);
		} else {
			list.add(index, insertElement);
		}
	}
	
	
	/**
	 * 在给定的链表中在指定的元素后面插入新的元素
	 * 说明:尽量保持链表中的元素不重复，假如元素重复，匹配第一个查找找的元素，假如找不到匹配元素，插入在列表最后面。
	 * @param list
	 * @param targetElement --目标元素
	 * @param insertElement --插入元素
	 * e.g. [foo,bar,fooBar]
	 * insertAfterElement([foo,bar,fooBar], "bar", "newValue")   ==>  [foo,bar,newValue,fooBar]
	 * insertAfterElement([foo,bar,fooBar], "foo", "newValue")   ==>  [foo,newValue,bar,fooBar]
	 * insertAfterElement([foo,bar,fooBar], "noFoo", "newValue") ==>  [foo,bar,fooBar,newValue]
	 * insertAfterElement([foo,bar,foo,fooBar,foo,bar,bar,bar], "bar", "newValue") ==>  [foo,bar,newValue,foo,fooBar,foo,bar,bar,bar]
	 */
	@SuppressWarnings("unchecked")
	public static void insertAfterElement(@SuppressWarnings("rawtypes") List list, Object targetElement,Object insertElement) {
		//System.out.println("DOWN：" + list);
		//System.out.println("targetElement：" + targetElement);
		if (!hasElement(list)) {
			throw new IllegalArgumentException("list can not be null");
		}
		if (targetElement == null) {
			throw new IllegalArgumentException("targetElement can not be null");
		}
		if (insertElement == null) {
			throw new IllegalArgumentException("insertElement can not be null");
		}
		int index = list.indexOf(targetElement);
		int size = list.size();
		if (index == -1 || index == (size -1)) {
			list.add(size, insertElement);
		} else {
			list.add(index + 1, insertElement);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void printCollection(Collection t){
		if(t==null||t.isEmpty()){
			System.out.println("collection is empty or null...");
			return;
		}
		for (Iterator<Object> iterator = t.iterator(); iterator.hasNext();) {
			Object object =  iterator.next();
			if(object instanceof Map) {
				printMap((Map)object);
		    }else{
		    	System.out.println(object.toString());
		    }   
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void printMap(Map t){
		if(t==null||t.isEmpty())return;
		for (Iterator<Object> iterator = t.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
            System.out.println(key+"/"+t.get(key));
		}
	}
}
