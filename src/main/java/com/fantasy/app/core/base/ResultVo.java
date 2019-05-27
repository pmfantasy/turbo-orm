package com.fantasy.app.core.base;

/**
 * 通用结果类
 *
 * @author 公众号：18岁fantasy
 * @2014-11-21 @下午5:30:09
 */
public class ResultVo<T> {

    private String code;
    private String msg;
    private boolean success;
    private T data;

    public ResultVo() {

    }

    public ResultVo(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(boolean success) {
        super();
        this.success = success;
    }

    public ResultVo(String code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResultVo [code=" + code + ", msg=" + msg + ", success=" + success + ", data=" + data
                + "]";
    }

}
