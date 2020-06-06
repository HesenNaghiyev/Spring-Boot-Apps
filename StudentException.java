package ada.wm2.jpa.exception;

public class StudentException extends Exception {

    public StudentException(String message){
        super(message);
    }
    @Override
    public String getMessage(){
        return "Entity error:" + super.getMessage();
    }
}
