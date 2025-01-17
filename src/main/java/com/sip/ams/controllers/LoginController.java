package com.sip.ams.controllers;

import com.sip.ams.entities.User;
import com.sip.ams.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final UserServices userServices;

    @Autowired
    public LoginController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) boolean error, Model model) {

        if(error){
            model.addAttribute("error", "error");
        }
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }


    @GetMapping("/registration")
    public String RegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);

        return "registration";
    }


    @PostMapping("/login")
    public String UserLogin(){
        System.out.println("User Logged In");
        return "home";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userServices.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }else if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
            return modelAndView;
        }

        userServices.saveUser(user);

        modelAndView.setViewName("redirect:/login");
        return modelAndView;

    }

    @GetMapping("/403")
    public String error403(){
        return "/error/403";
    }
}
