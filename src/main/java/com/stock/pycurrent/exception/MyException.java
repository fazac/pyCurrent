package com.stock.pycurrent.exception;

import lombok.Data;

/**
 * @author fzc
 * @date 2023/6/29 14:50
 * @description
 */
@Data
public class MyException extends RuntimeException {
    private int code = 500;
    private String msg;

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public MyException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public MyException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
