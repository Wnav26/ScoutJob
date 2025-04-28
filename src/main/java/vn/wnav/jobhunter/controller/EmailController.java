package vn.wnav.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.wnav.jobhunter.service.EmailService;
import vn.wnav.jobhunter.service.SubscriberService;
import vn.wnav.jobhunter.util.annotation.ApiMessage;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    @Scheduled(cron = "0 0 9 */7 * *")
    @Transactional
    public String sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("wnav20@gmail.com", "test",
        // "<h1><b>HELLO</b></h1>", false, true);
        // this.emailService.sendEmailFromTemplateSync("wnav20@gmail.com", "test",
        // "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }

}
