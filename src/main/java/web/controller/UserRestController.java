package web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.Model.Role;
import web.Model.User;
import web.service.RoleService;
import web.service.UserService;
import java.security.Principal;
import java.util.*;

@RestController
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("admin")
    public ModelAndView adminPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("allUsers", userService.findAll());
        modelAndView.addObject("allRoles", roleService.findAllRoles());
        modelAndView.addObject("currentUser", userService.findByLastName(principal.getName()));
        modelAndView.addObject("newUser", new User());
        return modelAndView;
    }

    @GetMapping("user")
    public ModelAndView userPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        modelAndView.addObject("allUsers", userService.findAll());
        modelAndView.addObject("currentUser", userService.findByLastName(principal.getName()));
        return modelAndView;
    }

    @GetMapping(value = "/users/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> adminPage() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.findById(userId).get(), HttpStatus.OK);
    }

    @PostMapping(value = "/edit/{id}/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String editUser(@PathVariable("id") Long id, @RequestBody Map<String, Object> payload) {

        User user = new User();
        List<String> roleIds = (ArrayList)payload.get("roles");

        Set<Role> currentRoles = userService.findById(id).get().getRoles();
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (String i : roleIds) {
                roles.add(roleService.findById(Long.parseLong(i)).get());
                user.setRoles(roles);
            }
        } else {
            user.setRoles(currentRoles);
        }

        user.setId(id);
        user.setFirstName((String)payload.get("firstName"));
        user.setLastName((String)payload.get("lastName"));
        user.setEmail((String)payload.get("email"));
        user.setPassword((String)payload.get("password"));

        userService.saveUser(user);

        return "200";
    }

    @GetMapping("/delete/{id}/")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "200";
    }

    @PostMapping(value = "/create/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@RequestBody Map<String, Object> payload) {
        User user = new User();
        List<String> roleIds = (ArrayList)payload.get("roles");

        Set<Role> roles = new HashSet<>();
            for (String i : roleIds) {
                roles.add(roleService.findById(Long.parseLong(i)).get());
                user.setRoles(roles);
            }

        user.setFirstName((String)payload.get("firstName"));
        user.setLastName((String)payload.get("lastName"));
        user.setEmail((String)payload.get("email"));
        user.setPassword((String)payload.get("password"));

        userService.saveUser(user);
        return "200";
    }
}