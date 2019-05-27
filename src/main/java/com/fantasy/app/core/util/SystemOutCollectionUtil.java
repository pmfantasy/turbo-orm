package com.fantasy.app.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 打印工具
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:19:32
 */
public class SystemOutCollectionUtil {

	public static void print(Object obj){
//		System.out.println(obj.toString());
	}
	
	public  static <K,V> void printMap(Map<K,V> map){
		if(map!=null){
			for (Iterator<K> iterator = map.keySet().iterator(); iterator.hasNext();) {
				K k =  iterator.next();
				V v = map.get(k);
				print(k.toString()+" / "+v.getClass()+" / "+v.toString());
			}
		}
	}
	public  static <E> void printCollection(Collection<E> col){
		if(col!=null){
			for (Iterator<E> iterator = col.iterator(); iterator.hasNext();) {
				E e =  iterator.next();
				print(e);
			}
		}
	}
}
