package ada.wm2.jpa.Controller;

import ada.wm2.jpa.entity.Course;
import ada.wm2.jpa.entity.Student;
import ada.wm2.jpa.exception.CourseException;
import ada.wm2.jpa.exception.StudentException;
import ada.wm2.jpa.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    @Qualifier("courseWelcomeMessage")
    String course;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message",course);
        logger.info("Index Page succesfully accessed");
        return "courses/index";
    }

    @GetMapping("/list")
    public String getCourseList(Model model){
       logger.debug("Entered method to get list of courses");
        Iterable<Course> courses= courseRepository.getAllCourses();
        model.addAttribute("courses", courses);
        logger.info("Got the list of courses");
        return "courses/CourseList";
    }

    @GetMapping("/id")
    public String getStudentById(Model mode, @RequestParam String id) throws CourseException {
       logger.debug("Entered Course Find By id Method");
        if (!id.matches("^[0-9]*$")) {
            logger.error("Invalid type for Id");
            throw new CourseException("Invalid Type of id",1);
        }
        Integer stId = Integer.valueOf(id);

        Optional<Course> result= courseRepository.getCourseByID(stId);
        if (result.isEmpty()) {
            logger.error("Entered id is not found in the list");
            throw new CourseException("Given Id is not defined for any Course ",2);
        }
            List<Course> courselist =new ArrayList<Course>();
            courselist.add(result.get());
         logger.info("Find this student for entered Id"+result.get());
            mode.addAttribute("courses",courselist);
            return "courses/CourseList";

    }

    @PostMapping("/delete")
    public String deleteCourse(Model model,  Integer cId, Course data) throws CourseException{
     logger.debug("Entered Delete course Method");
       try {
           Optional<Course> find = courseRepository.findById(cId);
           courseRepository.delete(find.get());
           Iterable<Course> courses= courseRepository.findAll();
           model.addAttribute("courses", courses);
           return "courses/CourseList";

       }
   catch (Exception ex) {
       Optional<Course> find = courseRepository.findById(cId);
       logger.info("You can not delete this course");
       throw new CourseException("You cannot Delete this course ID which is Foreign key In Enrollment Table", 3);

   }

    }
    @GetMapping("/new")
    public String newCoursePage(Model model){
        logger.debug("Clicked Add Course method. ");
        model.addAttribute("course",new Course());
        logger.info("");
        return "courses/add";
    }

    @PostMapping("/add")
    public String addNewCourses(Model model, @Valid Course courseData, BindingResult bindingResult){
        logger.debug("Debug Update and Save Method: ");
        if (bindingResult.hasErrors()){
            logger.error("Did you get Error  " +courseData.getCoursename());
            return "courses/add";
        }
        courseRepository.save(courseData);
        logger.info("You succesfully added :"+courseData.getCoursename()+" "+courseData.getCourseID()+" to courselist ");
        Iterable<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/CourseList";
    }

    @GetMapping("/{goupdate}")
    public String updateCoursePage(Model model, @RequestParam Integer id){
      logger.debug("Return all course data for update");
        Optional<Course> result= courseRepository.findById(id);
        if (result.isPresent()){
            model.addAttribute("course", result.get());
            model.addAttribute("cId", result.get().getCourseID());
            logger.info("Got The all course data");

        }
        return "courses/update-delete";
    }

     @PostMapping("/update")
        public String updateCourse(Model model, @Valid Course courseData, BindingResult bindingResult){
         logger.debug("Debug Update and Save Method: ");
         if (bindingResult.hasErrors()){
             logger.error("Did you get Error  " +courseData.getCoursename());
             return "courses/update-delete";
         }
            courseRepository.save(courseData);
         logger.info("You succesfully added :"+courseData.getCoursename()+" "+courseData.getCourseID()+" to courselist ");
            Iterable<Course> courses = courseRepository.findAll();
            model.addAttribute("courses", courses);
            return "courses/CourseList";
    }
    @GetMapping("/courselevel")
    public  String getHighGpaStudents(Model model){
        logger.debug("DEBUGGING THE METHOD COURSE BY LEVEL");
        Iterable<Course> courses= courseRepository.getCourseByLevel();
        model.addAttribute("courses", courses);
        logger.info("Succefully get course by level"+courses);
        return "courses/CourseList";
    }
    @GetMapping("/prerequisite")
    public  String getprerequisite(Model model){
        logger.debug("Debugging by checking it is prerequisite");
        Iterable<Course> courses= courseRepository.getCourseByPrerequisite();
        model.addAttribute("courses", courses);
        logger.info("Successfully got courses by prerequisite " +courses);
        return "courses/CourseList";
    }

}
