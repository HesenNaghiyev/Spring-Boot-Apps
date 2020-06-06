package ada.wm2.jpa.Controller;


import ada.wm2.jpa.entity.Course;
import ada.wm2.jpa.entity.Student;
import ada.wm2.jpa.exception.StudentException;
import ada.wm2.jpa.repository.CourseRepository;
import ada.wm2.jpa.repository.StudentRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    @Qualifier("studentWelcomeMessage")
    String student;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", student);
        logger.info("Index Page succesfully accessed");
        return "students/index";
    }



    @GetMapping("/list")
    public String getAllStudentsList(Model model) {
       logger.debug("Get Student List ");
        Iterable<Student> students = studentRepository.getAllStudents();
        model.addAttribute("students", students);
        logger.info("Succesfully got Student List "+ students);
        return "students/StudentList";
    }

    @GetMapping("/id")
    public String getStudentById(Model mode, @RequestParam String id) throws StudentException {
        if (!id.matches("^[0-9]*$")) {
            logger.error("You entered Invalid type for ID");
            throw new StudentException("Invalid id");
        }
        Integer stId = Integer.valueOf(id);

        Optional<Student> result = studentRepository.getStudentByID(stId);
        if (result.isEmpty()) {
            logger.error("Entered id is not found in the list");
            throw new StudentException("Id is not found ");
        }

            Student student = result.get();
            List<Student> stList = new ArrayList<Student>();
            stList.add(student);
            logger.info("Find this student for entered Id"+student);
            mode.addAttribute("students", stList);
            return "students/StudentList";


    }

    @GetMapping("/new")
    public String newPage(Model m) {
        m.addAttribute("student", new Student());
        m.addAttribute("Courses",courseRepository.findAll());
        return "students/add";
    }

    @PostMapping("/add")
    public String addNewStudents( Model model,@Valid Student studentData,  BindingResult br){
        logger.debug("Save method");
        if(br.hasErrors()){
            logger.error("You have error on  adding student -->  "+studentData.getFirstname());
            model.addAttribute("Courses",courseRepository.findAll());
            return "students/add";
        }
        studentRepository.save(studentData);
        logger.info("Saved student -->"+ studentData.getFirstname()+" "+ studentData.getLastname());
        Iterable<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("message", "Student " + studentData.getFirstname() + " " + studentData.getLastname() + " is new to Student List");
        return "students/StudentList";
    }

    @GetMapping("/{update-delete}")
    public String updatePage(Model m, @RequestParam Integer id) {
        Optional<Student> result = studentRepository.findById(id);
        if (result.isPresent()) {
            m.addAttribute("student", result.get());
            m.addAttribute("sId", result.get().getId());
            m.addAttribute("Courses",courseRepository.findAll());
        }
        return "students/update-delete";
    }

    @PostMapping("/update")
    public String UpdateStudents( Model model, @Valid Student studentData, BindingResult br){
        logger.debug("Save method");
        if(br.hasErrors()){
            logger.error("You have error on  updating  student -->  "+studentData.getFirstname());
            model.addAttribute("Courses",courseRepository.findAll());


            return "students/update-delete";
               }
        studentRepository.save(studentData);
        logger.info("Saved student -->"+ studentData.getFirstname()+" "+ studentData.getLastname());
        Iterable<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("message", "Student " + studentData.getFirstname() + " " + studentData.getLastname() + " is new to Student List");
        return "students/StudentList";
    }
    @PostMapping("/delete")
    public String deleteStudent(Model model,  Integer delId) {
       logger.debug(" Delete Method is in process");
        Optional<Student> find = studentRepository.findById(delId);
        studentRepository.delete(find.get());
        logger.info(find.get().getFirstname()+ " "+ find.get().getLastname() +"Succesfully deleted form Student List " );
        Iterable<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("message", "Student " + find.get().getFirstname() + " " + find.get().getLastname() + " is deleted from StudentList");

        return "students/StudentList";
    }

    @GetMapping("/gethighgpa")
    public  String getHighGpaStudents(Model model){
        logger.debug("GetHighGpaStudents");
        Iterable<Student> students= studentRepository.getStudentsMoreThan3Gpa();
        model.addAttribute("students", students);
        logger.info("Successfuly got students with high gpa");
        return "students/StudentList";
    }



}
