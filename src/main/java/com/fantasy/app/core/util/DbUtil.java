package com.fantasy.app.core.util;
/**
 * 数据库util
 * @author 公众号：18岁fantasy
 * 2017-5-26 上午11:22:16
 */
public class DbUtil {

	/**
	 * 从拦截字符串中查看是否是mysql
	 * @param connectUrl
	 * @return
	 */
	public static boolean isMysqlFromConnectUrl(String connectUrl){
		if(connectUrl!=null&&!("".equals(connectUrl))){
			if(connectUrl.toLowerCase().indexOf("mysql")!=-1){
				return true;
			}
		}
		return false;
	}
	/**
	 * 从拦截字符串中查看是否是oracle
	 * @param connectUrl
	 * @return
	 */
	public static boolean isOracleFromConnectUrl(String connectUrl){
		if(connectUrl!=null&&!("".equals(connectUrl))){
			if(connectUrl.toLowerCase().indexOf("oracle")!=-1){
				return true;
			}
		}
		return false;
	}
	/**
	 * 从拦截字符串中查看是否是sqlserver
	 * @param connectUrl
	 * @return
	 */
	public static boolean isSqlServerFromConnectUrl(String connectUrl){
		if(connectUrl!=null&&!("".equals(connectUrl))){
			if(connectUrl.toLowerCase().indexOf("sqlserver")!=-1){
				return true;
			}
		}
		return false;
	}
	/**
     * 从拦截字符串中查看是否是sqlserver
     * @param connectUrl
     * @return
     */
    public static boolean isPostgresqlFromConnectUrl(String connectUrl){
        if(connectUrl!=null&&!("".equals(connectUrl))){
            if(connectUrl.toLowerCase().indexOf("postgresql")!=-1){
                return true;
            }
        }
        return false;
    }
	public static void main(String[] args) {
		System.out.println(isMysqlFromConnectUrl("jdbc:mysql://localhost:3306/birs?useUnicode=true&characterEncoding=UTF-8"));
		System.out.println(isOracleFromConnectUrl("jdbc:oracle:thin:@localhost:1521"));
		System.out.println(isSqlServerFromConnectUrl("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName"));
	}
}
