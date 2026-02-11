package dev.jayasurya.userauthservice.Exception;

public class IncorrectPasswordException extends  Exception{
    public  IncorrectPasswordException(){
        super();
    }
    public  IncorrectPasswordException(String message){
        super(message);
    }
}
