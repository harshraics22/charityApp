package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class userController {
	
	
	@Autowired
    private SendEmailService sendEmailService;
	
	
	
	

    @GetMapping("//")
    public String home() {
        return "harsh/index"; // Ensure the file exists at templates/harsh/index.html
    }

    @GetMapping("//shop.html")
    public String shop() {
        return "harsh/shop"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("//about.html")
    public String about() {
        return"harsh/about"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    
    @GetMapping("//services.html")
    public String sercices() {
        return "harsh/services"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("//blog.html")
    public String blog() {
        return "harsh/blog"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("//contact.html")
    public String contact() {
        return "harsh/contact"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("//cart.html")
    public String cart() {
        return"harsh/cart"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    
    @GetMapping("//checkout.html")
    public String checkout() {
        return "harsh/checkout"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("//thankyou.html")
    public String thankyou() {
        return "harsh/thankyou"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    
    
    
    @GetMapping("/services.html")
    public String Sercices() {
        return"harsh/services"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("/blog.html")
    public String Blog() {
        return "harsh/blog"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    
   
    
    @GetMapping("//index.html")
    public String redirectshoptohome() {
        return"harsh/index"; // Ensure the file exists at templates/harsh/index.html
    }
    

    
    
    
    
    @GetMapping("/index.html")
    public String redirectShopTOIndex() {
        return "harsh/index"; // Redirects to the /shop mapping
    }
    
    
    @GetMapping("/shop.html")
    public String Shop() {
        return "harsh/shop"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("/about.html")
    public String About() {
        return "harsh/about"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    @GetMapping("/contact.html")
    public String Contact() {
        return "harsh/contact"; // Ensure the file exists at templates/harsh/shop.html
    }
    
    
   
    
    
    // User registration handler
    @PostMapping("//")
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("password") String phno,
                               @RequestParam("name") String name) {

        // Optional: Save user details in the database if needed
        // userService.registerUser(email, password, name);

        // Send a registration email
        sendEmailService.sendEmail(
                email,
                "Registration Successful",
                "Hello " + name + ",\n\n" +
                "Your registration is successful!\n\n" +
                "Your email: " + email + "\n" +
                "Your password: " + phno + "\n\n" +
                "Welcome aboard!\n\n" +
                "Best Regards,\nYour Team"
        );

        return"harsh/index"; // Redirect to the home page or show a success message
    }
    
    
}
