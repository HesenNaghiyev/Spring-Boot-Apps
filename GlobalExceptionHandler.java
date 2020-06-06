package ada.wm2.jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;



@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ModelAndView handleErrorswith400(Exception ex) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/errorPages/errorswith400");
        view.addObject("exception", ex.getMessage());
        return view;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleError(Exception ex) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/errorPages/errorswith400");
        view.addObject("exception", ex.getMessage());
        return view;
    }
}
