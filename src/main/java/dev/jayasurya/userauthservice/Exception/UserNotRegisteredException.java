package dev.jayasurya.userauthservice.Exception;

public class UserNotRegisteredException extends Exception{
    public UserNotRegisteredException(String message) {
        super(message);
    }
    public  UserNotRegisteredException(){
        super();
    }
}
