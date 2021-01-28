package com.hindsight.authdemo.advice.exception;

public class CComunicationException extends RuntimeException{
    public CComunicationException(String msg, Throwable t){
        super(msg, t);
    }
    public CComunicationException(String msg){
        super(msg);
    }
    public CComunicationException(){
        super();
    }

}
