package com.fantasy.app.core.component.log;

/**
 * 日志记录器
 * @author 公众号：18岁fantasy
 * @2015-6-5 @下午3:46:59
 */
public class Logger extends org.apache.log4j.Logger{
    
   
    org.apache.log4j.Logger log4j = null;
    /**
     * use Log.getLogger instand
     * @param name
     * @return
     */
    @Deprecated
    public static  Logger getLogger(String name){
        return new Logger(name);
    }
    protected Logger(String name) {
        super(name);
        log4j = Log.getLogger(name);
    }
    
    public static String getTraceInfoCurrent() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        sb.append("class: ").append(stacks[1].getClassName())
                .append("; method: ").append(stacks[1].getMethodName())
                .append("; number: ").append(stacks[1].getLineNumber());
        return sb.toString();
    }
    public static String getPathInfo(){  
        StringBuffer sb = new StringBuffer(); 
        
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();   
        if (null == stacks || stacks.length < 4){
            return sb.toString();
        }
        StackTraceElement cus = stacks[3];
        sb.append(cus.getClassName()+"."+cus.getMethodName()+"("+cus.getFileName()+":"+cus.getLineNumber()+")" ); 
        return sb.toString();
    }
     
    @Override
    public void error(Object message, Throwable t) {
        log4j.error(message, t);
    }
    
    @Override
    public void error(Object message) {
        Object msg = null;
        if(message instanceof Throwable){
            msg = ((Throwable)message).getMessage();
            log4j.error(msg, (Throwable)message);
        }else{
            msg=message;
            log4j.error(msg);
        }
    }
    @Override
    public void debug(Object message) {
        log4j.debug(message);
    }

    @Override
    public void info(Object message) {
        log4j.info(message);
    }

    @Override
    public void info(Object message, Throwable t) {
        log4j.info(message, t);
    }

    @Override
    public void warn(Object message) {
        log4j.warn(message);
    }

    @Override
    public void warn(Object message, Throwable t) {
        log4j.warn(message, t);
    }
    @Override
    public boolean isDebugEnabled() {
        return log4j.isDebugEnabled();
    }
    @Override
    public boolean isInfoEnabled() {
        return log4j.isInfoEnabled();
    }
}
