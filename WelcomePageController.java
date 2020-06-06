package ada.wm2.jpa.Controller;

import ada.wm2.jpa.entity.User;
import ada.wm2.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomePageController {

    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/")
    public String getChoice(){
        logger.info("Succesfully accessed to welcome Page");
        return "welcome";

    }

    @PostMapping("/register")
    public  String getLogin(Model model, @RequestParam("username") String us, @RequestParam("psw") String psw, User user){

       Iterable<User> users =userRepository.findAll();
       System.out.println("Haoijediocn "+ users);
       model.addAttribute("users", users);
        if (us.equals(user.getUsername()) && psw.equals(user.getPassword())){
            System.out.println(user.getPassword()+"helloo");
            return "students/index";
        }
        else {
            model.addAttribute("message","Please enter correct username or password");
            System.out.println("Niye bura duwur "+ user.getUsername()+" "+ user.getPassword());
            return "welcome";
        }

    }
}
