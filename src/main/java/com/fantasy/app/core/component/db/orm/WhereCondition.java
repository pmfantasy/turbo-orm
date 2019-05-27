package com.fantasy.app.core.component.db.orm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fantasy.app.core.component.db.orm.valuebean.BetweenValue;
import com.fantasy.app.core.component.db.orm.valuebean.LikeValue;
import com.fantasy.app.core.component.db.orm.wherebean.JustAppend;
import com.fantasy.app.core.component.db.orm.wherebean.WhereBean;
import com.fantasy.app.core.component.db.orm.wherebean.WhereBean.ConditionType;
import com.fantasy.app.core.util.StrUtil;

/**
 * 拼sql where条件语句 
 * @see {@link test.core.WhereConditionTest}
 * @author 公众号：18岁fantasy
 * @2014-4-17 @下午1:19:11
 */
public class WhereCondition {

	private ArrayList<Object> whereCondition = new ArrayList<Object>();
	
	public static WhereCondition newInstance(){
		return new WhereCondition();
	}
	/**
	 * add "where " 必须是第一个添加的 
	 * @return
	 */
	public WhereCondition where (){
		whereCondition.add(new JustAppend(" WHERE "));
		return this;
	}
	/**
	 * add "where 1=1 " 必须是第一个添加的
	 * @return
	 */
	public WhereCondition where1Eq1 (){
		whereCondition.add(new JustAppend(" WHERE 1=1"));
		return this;
	}
	/**
	 * add "and" 每两个条件之间必须用and 或者 or分开
	 * @return
	 */
	public WhereCondition and(){
		whereCondition.add(new JustAppend(" AND "));
		return this;
	}
	/**
	 * add "or"   如果需要使用括号，需调用 {@link #leftBracket()} 和 {@link #rightBracket()} 
	 * @return
	 */
	public WhereCondition or(){
		whereCondition.add(new JustAppend(" OR "));
		return this;
	}
	/**
	 * add "("  
	 * @return
	 */
	public WhereCondition leftBracket(){
		whereCondition.add(new JustAppend(" ( "));
		return this;
	}
	/**
	 * add ")"  
	 * @return
	 */
	public WhereCondition rightBracket(){
		whereCondition.add(new JustAppend(" ) "));
		return this;
	}
	/**
	 * add "is null"   
	 * @return
	 */
	public WhereCondition isNull(String clumnName){
		if(StrUtil.isNotBank(clumnName)){
			whereCondition.add(new JustAppend(clumnName+" IS NULL "));
		}
		return this;
	}
	/**
	 * add "is not null"   
	 * @return
	 */
	public WhereCondition isNotNull(String clumnName){
		if(StrUtil.isNotBank(clumnName)){
			whereCondition.add(new JustAppend(clumnName+" IS NOT NULL "));
		}
		return this;
	}
	/**
	 * add "exists"   
	 * @return
	 */
	public WhereCondition exists(String sql){
		if(StrUtil.isNotBank(sql)){
			whereCondition.add(new JustAppend(" EXISTS ("+sql+")" ));
		}
		return this;
	}
	/**
	 * add "not exists"   
	 * @return
	 */
	public WhereCondition notExists(String sql){
		if(StrUtil.isNotBank(sql)){
			whereCondition.add(new JustAppend(" NOT EXISTS ("+sql+")" ));
		}
		return this;
	}
	/**
	 * add "="   
	 * @return
	 */
	public WhereCondition eq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.EQ, clumnName, value));
		}
		return this;
	}
	/**
	 * add "and" 每两个条件之间必须用and 或者 or分开
	 * @return
	 */
	public WhereCondition andEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().eq(clumnName,value);
		}
		return this;
	}
	/**
	 * add "and" 每两个条件之间必须用and 或者 or分开
	 * @return
	 */
	public WhereCondition orEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			or().eq(clumnName,value);
		}
		return this;
	}
	/**
	 * add "<>"   
	 * @return
	 */
	public WhereCondition notEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.NOT_EQ, clumnName, value));
		}
		return this;
	}
	/**
	 * add "and col<>?"   
	 * @return
	 */
	public WhereCondition andNotEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().notEq(clumnName,value);
		}
		return this;
	}
	/**
	 * add "and" 每两个条件之间必须用and 或者 or分开
	 * @return
	 */
	public WhereCondition orNotEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			or().notEq(clumnName,value);
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public WhereCondition greaterThan(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.GREATER_THAN, clumnName, value));
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public WhereCondition andGreaterThan(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().greaterThan(clumnName,value);
		}
		return this;
	}
	/**
	 * add ">="   
	 * @return
	 */
	public WhereCondition greaterThanOrEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.GREATER_THAN, clumnName, value));
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public WhereCondition andGreaterThanOrEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().greaterThanOrEq(clumnName,value);
		}
		return this;
	}
	/**
	 * add "<"   
	 * @return
	 */
	public WhereCondition lessThan(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.LESS_THAN, clumnName, value));
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public WhereCondition andLessThan(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().lessThan(clumnName,value);
		}
		return this;
	}
	/**
	 * add "<="   
	 * @return
	 */
	public WhereCondition lessThanOrEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.LESS_THAN_OR_EQUAL, clumnName, value));
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public WhereCondition andLessThanOrEq(String clumnName,Object value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().lessThanOrEq(clumnName,value);
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition like(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			whereCondition.add(new WhereBean(ConditionType.LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition andLike(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			and().like(clumnName, value);
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition orLike(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			or().like(clumnName, value);
		}
		return this;
	}
	/**
	 * add "like"   
	 * @return
	 */
	public WhereCondition like(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition andLike(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().like(clumnName, value);
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition orNotLike(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			or().notLike(clumnName, value);
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public WhereCondition orLike(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			or().like(clumnName, value);
		}
		return this;
	}
	/**
	 * add " not like "   
	 * @return
	 */
	public WhereCondition notLike(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			whereCondition.add(new WhereBean(ConditionType.NOT_LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " not like "   
	 * @return
	 */
	public WhereCondition andNotLike(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			and().notLike(clumnName, value);
		}
		return this;
	}
	/**
	 * add " not like "   
	 * @return
	 */
	public WhereCondition orNotLike(String clumnName,String value){
		if(StrUtil.isNotBank(clumnName)&&StrUtil.isNotBank(value)){
			or().notLike(clumnName, value);
		}
		return this;
	}
	/**
	 * add "not like"   
	 * @return
	 */
	public WhereCondition notLike(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.NOT_LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " not like "   
	 * @return
	 */
	public WhereCondition andNotLike(String clumnName,LikeValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().notLike(clumnName, value);
		}
		return this;
	}
	/**
	 * add " between"   
	 * @return
	 */
	public WhereCondition between(String clumnName,BetweenValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.BETWEEN, clumnName, value));
		}
		return this;
	}
	/**
	 * add " between"   
	 * @return
	 */
	public WhereCondition andBetween(String clumnName,BetweenValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().between(clumnName, value);
		}
		return this;
	}
	/**
	 * add "not between"   
	 * @return
	 */
	public WhereCondition notBetween(String clumnName,BetweenValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			whereCondition.add(new WhereBean(ConditionType.NOT_BETWEEN, clumnName, value));
		}
		return this;
	}
	/**
	 * add " between"   
	 * @return
	 */
	public WhereCondition andNotBetween(String clumnName,BetweenValue value){
		if(StrUtil.isNotBank(clumnName)&&value!=null){
			and().notBetween(clumnName, value);
		}
		return this;
	}
	/**
	 * add "in"   
	 * @return
	 */
	public WhereCondition in(String clumnName,Object[] value){
		if(clumnNotNull(clumnName)){
			whereCondition.add(new WhereBean(ConditionType.IN, clumnName, value));
		}
		return this;
	}
	/**
	 * add "in"   
	 * @return
	 */
	public <T> WhereCondition in(String clumnName,List<T> values){
		if(clumnNotNull(clumnName)){
			if(values!=null&&!values.isEmpty()){
				whereCondition.add(new WhereBean(ConditionType.IN, clumnName, values.toArray()));
			}
		}
		return this;
	}
	/**
	 * add "in"   
	 * @return
	 */
	public WhereCondition andIn(String clumnName,Object[] value){
		if(StrUtil.isNotBank(clumnName)&&value!=null&&value.length!=0){
			and().in(clumnName, value);
		}
		return this;
	}
	/**
	 * add "not in"   
	 * @return
	 */
	public WhereCondition notIn(String clumnName,Object[] value){
		if(StrUtil.isNotBank(clumnName)&&value!=null&&value.length!=0){
			whereCondition.add(new WhereBean(ConditionType.NOT_IN, clumnName, value));
		}
		return this;
	}
	/**
	 * add "in"   
	 * @return
	 */
	public WhereCondition andNotIn(String clumnName,Object[] value){
		if(StrUtil.isNotBank(clumnName)&&value!=null&&value.length!=0){
			and().notIn(clumnName, value);
		}
		return this;
	}
	/**
	 * add "groupby"
	 * @param clumnNames  such as "a,b,c"
	 * @return
	 */
	public WhereCondition groupBy(String clumnNames){
		if (StringUtils.hasText(clumnNames)&&clumnNames.indexOf("null")==-1&&clumnNames.indexOf("NULL")==-1) {
			whereCondition.add(new JustAppend(" GROUP BY "+clumnNames));
		}
		return this;
	}
	/**
	 * add "order by "
	 * @param oderClumnNames 排序内容 such " a desc,basc"
	 * @param order 排序方式
	 */
	public WhereCondition orderBy(String orderBysql) {
		if (StringUtils.hasText(orderBysql)&&(orderBysql.indexOf("null")==-1)&&orderBysql.indexOf("NULL")==-1) {
			StringBuffer orderBy = new StringBuffer();
			orderBy.append(" ORDER BY ").append(orderBysql);
			whereCondition.add(new JustAppend(orderBy.toString()));
		}
		return this;
	}
	
	private boolean clumnNotNull(String clumnName) {
		return StringUtils.hasText(clumnName);
	}
	public ArrayList<Object> getWhereCondition() {
		return whereCondition;
	}
	
	
	
	@Override
	public String toString() {
		return "WhereCondition [whereCondition=" + whereCondition + "]";
	}
	
}
