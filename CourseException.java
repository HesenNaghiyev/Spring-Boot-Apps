package ada.wm2.jpa.exception;

public class CourseException extends Exception {
  Integer errornumber;
    public CourseException(String message, Integer errornumber){
        super(message);
        this.errornumber=errornumber;
    }
    @Override
    public String getMessage(){
        return "Error :" + errornumber+ " "+  super.getMessage();
    }
}