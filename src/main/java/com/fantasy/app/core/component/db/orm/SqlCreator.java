package com.fantasy.app.core.component.db.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fantasy.app.core.component.db.orm.valuebean.BetweenValue;
import com.fantasy.app.core.component.db.orm.valuebean.EqualValue;
import com.fantasy.app.core.component.db.orm.valuebean.NotEqualValue;
import com.fantasy.app.core.component.db.orm.valuebean.OrEqualValue;
import com.fantasy.app.core.component.db.orm.valuebean.OrValue;
import com.fantasy.app.core.component.db.orm.wherebean.JustAppend;
import com.fantasy.app.core.component.db.orm.wherebean.WhereBean;
import com.fantasy.app.core.component.db.orm.wherebean.WhereBean.ConditionType;


/**
 * 根据参数拼接where语句和参数
 * @author 公众号：18岁fantasy
 * @2014-4-17 @下午5:23:19
 */
public class SqlCreator {
	
	public static SqlArg createWhere(WhereCondition searchData, boolean excludeNullAndBlank) {
		SqlArg sqlArg = new SqlArg("", new Object[0]);
		if (searchData == null||searchData.getWhereCondition().isEmpty()){
			return sqlArg;
		}
		ArrayList<Object> conditions = searchData.getWhereCondition();
		List<Object> args_result = new ArrayList<Object>();
		StringBuilder where = new StringBuilder();
		Object preObject = null;
		for (Iterator<Object> iterator = conditions.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object instanceof JustAppend) {
				JustAppend append = (JustAppend)object;
				handleJustAppend(where,append);
			}else if(object instanceof WhereBean) {
				WhereBean whereBean = (WhereBean)object;
				handleWhereBean(preObject,whereBean,where,args_result,excludeNullAndBlank);
			}else{
				//
			}
			preObject = object;
		}
		sqlArg.setArgs(args_result.toArray());
		sqlArg.setSql(where.toString());
		return sqlArg;
	}

	/**
	 * 如果where 末尾已经有where and or 就不能再加
	 * @param where
	 * @param append
	 */
	private static void handleJustAppend(StringBuilder where,JustAppend append) {
		String content = append.getContent().trim();
		if("WHERE 1=1".equalsIgnoreCase(content)||"WHERE".equalsIgnoreCase(content)||"AND".equalsIgnoreCase(content)||"OR".equalsIgnoreCase(content)){
			if(!where.toString().trim().endsWith(content)){
				where.append(append.getContent());
			}
		}else{
			where.append(append.getContent());
		}
			
	}

	private static boolean betweenValueIsNull(Object value){
		if(value==null)return true;
		if(value instanceof BetweenValue){
			BetweenValue betweenValue = (BetweenValue)value;
			if(betweenValue.getBegin()==null&&betweenValue.getEnd()==null)return true;
		}
		return false;
	}
	private static void handleWhereBean(Object preObject, WhereBean whereBean,
			StringBuilder where, List<Object> args_result,boolean excludeNullAndBlank) {
		ConditionType conditionType = whereBean.getConditionType();
		String clumn = whereBean.getColumn();
		Object value = whereBean.getValue();
		//如果值为空 则取出前面的and或者or
		if((value==null||!StringUtils.hasText(value.toString())||betweenValueIsNull(value))&&excludeNullAndBlank){
			if(preObject!=null&&preObject instanceof JustAppend) {
				JustAppend append = (JustAppend)preObject;
				if("AND".equalsIgnoreCase(append.getContent().trim())||"OR".equalsIgnoreCase(append.getContent().trim())){
					int lastAppend = where.lastIndexOf(append.getContent());
					where.delete(lastAppend, where.length());	
				}
			}
			return;
		}
		/*EQ,
		NOT_EQ,
		GREATER_THAN,
		LESS_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN_OR_EQUAL,
		LIKE,
		NOT_LIKE,
		BETWEEN,
		NOT_BETWEEN,
		IN,
		NOT_IN;*/
		switch (conditionType) {
		case EQ:
			where.append(clumn).append(" = ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case NOT_EQ:
			where.append(clumn).append(" != ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case GREATER_THAN:
			where.append(clumn).append(" > ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case LESS_THAN:
			where.append(clumn).append(" < ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case GREATER_THAN_OR_EQUAL:
			where.append(clumn).append(" >= ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case LESS_THAN_OR_EQUAL:
			where.append(clumn).append(" <= ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case LIKE:
			where.append(clumn).append(" LIKE ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case NOT_LIKE:
			where.append(clumn).append(" NOT LIKE ").append("? ");
			args_result.add(wrapBean(value));
			break;
		case BETWEEN:
			BetweenValue betweenValue = (BetweenValue) value;
			where.append(handBetween(clumn,args_result,betweenValue,false));
			break;
		case NOT_BETWEEN:
			BetweenValue notBetweenValue = (BetweenValue) value;
			where.append(handBetween(clumn,args_result,notBetweenValue,true));
			break;
		case IN:
			Object[] obs = (Object[])value;
			where.append(handIn(clumn,args_result,obs,false));
			break;
		case NOT_IN:
			Object[] notobs = (Object[])value;
			where.append(handIn(clumn,args_result,notobs,true));
			break;
		default:
			break;
		}
		
	}


	private static Object handIn(String clumnName, List<Object> args_result,
			Object[] arrayObj, boolean not) {
		if(arrayObj!=null&&arrayObj.length!=0){
			StringBuffer where = new StringBuffer();
			if (arrayObj.length == 1) {
				where.append(clumnName).append(not?" !":" ").append("=? ");
				args_result.add(arrayObj[0]);
			} else {
				where.append(clumnName).append(not?" NOT ":" ").append(" IN (");
				int incount = 0;
				for (int i = 0; i < arrayObj.length; i++) {
					if (incount > 0)
						where.append(",");
					if (!isNullValue(arrayObj[i])) {
						where.append("?");
						args_result.add(arrayObj[i]);
					}
					incount++;
				}
				where.append(")");
			}
			return where.toString();
		}
		return "";
	}

	private static String handBetween(String clumnName, List<Object> args_result,BetweenValue bt,boolean not) {
		Object bv = bt.getBegin();
		Object ev = bt.getEnd();
		String includeBegin = (bt.isIncludeBegin()?"=":"");
		String includeEnd = (bt.isIncludeEnd()?"=":"");
		
		if (bv == null && ev == null){
			return "";
		}
		if ((bv != null && ev != null) && bv.getClass() != ev.getClass()){
			return "";
		}
		StringBuffer where = new StringBuffer();
		if (ev == null && bv != null) {
			where.append(clumnName).append(" >"+includeBegin+" ? ");
			args_result.add(bv);
		}
		if (ev != null && bv == null) {
			where.append(clumnName).append(" <"+includeEnd+" ? ");
			args_result.add(ev);
		}
		if (ev != null && bv != null) {
			where.append(" ( ");
		    if(bt.isIncludeBegin()&&bt.isIncludeEnd()){
		    	if(not){
		    		where.append(clumnName).append(" NOT BETWEEN ? AND ? ");
		    	}else{
		    		where.append(clumnName).append(" BETWEEN ? AND ? ");
		    	}
			}else{
				where.append(clumnName).append(" >"+includeBegin+" ? AND ").append(clumnName).append(" <"+includeEnd+" ?");
			}
		    where.append(" ) ");
		    args_result.add(bv);
			args_result.add(ev);
		}
		return where.toString();
	}

	private static Object wrapBean(Object value) {
		return value;
	}

	public static void handleEqualValue(String clumnName,Object valueW, 
			boolean excludeNullAndBlank,
			IncreaseInt count,
			StringBuilder where,
			List<Object> args_result,IncreaseInt orIndex,int orNum){
		Object value = valueW;
		if(valueW instanceof OrEqualValue){
			value = ((OrEqualValue)valueW).getValue();
		}
		if(valueW instanceof EqualValue){
			value = ((EqualValue)valueW).getValue();
		}
		boolean not = false;
		if(valueW instanceof NotEqualValue){
			not = true;
		}
		if (isNullValue(value) && excludeNullAndBlank) {
			return;
		}
		
		if (count.getValue() > 0) {
			if(orIndex.getValue()>0||(orIndex.getValue()==0&&orNum==1)){
				where.append(" OR ");
			}else{
			   where.append(" AND ");
			}
		}
		if(orIndex.getValue()==0){
			where.append("(");
			orIndex.increase();
		}
		if (value == null) {
			where.append(clumnName).append(not?" !":" ").append(" =? ");
			args_result.add(value);
		} else {
			if (value instanceof String && isLikeValue((String) value)) {
				where.append(clumnName).append(not?" NOT ":" ").append(" LIKE ? ");
				args_result.add(value);
			} else if (value instanceof Object[]) {//数组转化为in 类型：字符串，数值，时间..
				Object[] arrayObj = (Object[]) value;
				if (arrayObj.length == 1) {
					where.append(clumnName).append(not?" !":" ").append("=? ");
					args_result.add(arrayObj[0]);
				} else {
					where.append(clumnName).append(not?" NOT ":" ").append(" IN (");
					int incount = 0;
					for (int i = 0; i < arrayObj.length; i++) {
						if (incount > 0)
							where.append(",");
						if (!isNullValue(value)) {
							where.append("?");
							args_result.add(arrayObj[i]);
						}
						incount++;
					}
					where.append(")");
				}
			} else {
				where.append(clumnName).append(not?" !":" ").append("=? ");
				args_result.add(value);
			}
		}
		count.increase();
	}
	public static void handleBetweenValue(String clumnName,BetweenValue bt, 
			boolean excludeNullAndBlank,
			Map<String,OrValue> orValues,
			IncreaseInt count,
			StringBuilder where,
			List<Object> args_result,
			IncreaseInt orIndex){
		if (bt == null)
			return;
		Object bv = bt.getBegin();
		Object ev = bt.getEnd();
		if (bv == null && ev == null)
			return;
		if ((bv != null && ev != null) && bv.getClass() != ev.getClass())
			return;
        
		if(bt instanceof OrValue&&orIndex.getValue()==-1){
			orValues.put(clumnName, (OrValue)bt);
			return;
		}
		
		if (count.getValue() > 0) {
			if(orIndex.getValue()>0)where.append(" OR ");else where.append(" AND ");
		}
		if(orIndex.getValue()==0){
			where.append("(");
			orIndex.increase();
		}
		String includeBegin = (bt.isIncludeBegin()?"=":"");
		String includeEnd = (bt.isIncludeEnd()?"=":"");
		if (ev == null && bv != null) {
			where.append(clumnName).append(" >"+includeBegin+" ? ");
			args_result.add(bv);
		}
		if (ev != null && bv == null) {
			where.append(clumnName).append(" <"+includeEnd+" ? ");
			args_result.add(ev);
		}
		if (ev != null && bv != null) {
			where.append(" ( ");
		    if(bt.isIncludeBegin()&&bt.isIncludeEnd()){
				where.append(clumnName).append(" BETWEEN ? AND ? ");
			}else{
				where.append(clumnName).append(" >"+includeBegin+" ? AND ").append(clumnName).append(" <"+includeEnd+" ?");
			}
		    where.append(" ) ");
		    args_result.add(bv);
			args_result.add(ev);
		}
		count.increase();
	}

	public static boolean isLikeValue(String value) {
		return value.indexOf("%") != -1;
	}

	public static boolean isNullValue(Object value) {
		return value == null || (value instanceof String && !StringUtils.hasText((String) value))
				|| (value instanceof Object[] && ((Object[]) value).length == 0);
	}
//	public static void main(String[] args) {
//		WhereCondition condition = new WhereCondition();
//		condition
//		.and().eq("a", "1")
//		.and().eq("b", 2)
//		.and().eq("c", new Date())
//		.and().notEq("d", "2")
//		.and().leftBracket().eq("e", "1").or().greaterThan("f", "3").or().greaterThan("g", "5").rightBracket()
//		.and().between("dt", new BetweenValue(1, 2))
//		.and().lessThan("h", "7")
//		.and().notExists("select 1 from t_supt")
//		.groupBy("a,b,c")
//		.orderBy("a desc,b asc");
//		SqlArg sqlArg = SqlCreator.createWhere(condition, true);
//		String sql = sqlArg.getSql();
//		Object[] vas = sqlArg.getArgs();
//		System.out.println(sql);
//        System.out.println(Arrays.toString(vas));		
//		
//	}
}
