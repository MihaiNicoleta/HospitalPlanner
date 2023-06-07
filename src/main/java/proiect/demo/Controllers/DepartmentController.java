package proiect.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.Department;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;

import proiect.demo.Services.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    /**
     * Endpoint pentru obținerea tuturor departamentelor.
     *
     * @return Răspunsul HTTP care conține o listă de departamente și starea 200 OK în caz de succes.
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * Endpoint pentru obținerea unui departament după ID.
     *
     * @param id ID-ul departamentului care trebuie obținut.
     * @return Răspunsul HTTP care conține un obiect Optional care poate conține un departament sau poate fi gol,
     *         și starea 200 OK în caz de succes sau starea 404 Not Found dacă departamentul nu există.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Department>> getDepartmentById(@PathVariable int id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }
}



