package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import in.ashokit.dto.ContactUs;
import in.ashokit.repo.ContactUsRepo;


@Controller
@RequestMapping("/api/contactus")
public class ContactUsController {

    @Autowired
    private ContactUsRepo contactUsRepo;

    @Autowired
    private SendEmailService sendEmailService;

    @PostMapping("/submit")
    public String submitContactForm(@ModelAttribute ContactUs contactUs) {
        // Save contact form details in the database
        contactUsRepo.save(contactUs);

        // Send an email notification to the admin
        String emailSubject = "New Contact Us Submission";
        String emailBody = String.format(
            "Dear Admin,\n\n" +
            "You have received a new message from the Contact Us form:\n\n" +
            "Name: %s " + "%s \n\n" +
            "Email: %s\n" +
            "Message:\n%s\n\n" +
            "Best regards,\n" +
            "Your Application",
            contactUs.getFname(),
            contactUs.getLname(),
            contactUs.getEmail(),
            contactUs.getMessage()
        );

        try {
            sendEmailService.sendEmail("harshrai976@gmail.com", emailSubject, emailBody);    //email jispe message send karana hai 
        } catch (Exception e) {
            // Handle email-sending failure (log it or notify the admin)
            System.err.println("Failed to send email: " + e.getMessage());
        }

        // Return success page
        return "harsh/MessageSuccess";
    }
}
