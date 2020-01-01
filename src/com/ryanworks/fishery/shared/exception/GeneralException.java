package com.ryanworks.fishery.shared.exception;
 
public class GeneralException extends Exception
{
    public GeneralException() {}
 
    public GeneralException(String message) {
        super(message);
    }
 
    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public GeneralException(Throwable cause) {
        super(cause);
    }
 
    public String getLocalizedRootCauseMessage() {
        String msg = getCause().toString();
        int lastIndexOfColon = msg.lastIndexOf(":");
 
        if (lastIndexOfColon == -1) {
            return "Unknown";
        }
     
        return msg.substring(lastIndexOfColon + 1);
    }
 }
