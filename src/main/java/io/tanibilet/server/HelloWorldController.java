package io.tanibilet.server;

import io.tanibilet.server.auth.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// controller for testing purposes, can be removed later
@RestController
@RequestMapping("/")
public class HelloWorldController {


    @GetMapping
    public String helloWorld(@AuthenticationPrincipal UserPrincipal user) {
        return "Hello " + user.firstname() + " " + user.lastname();
    }

    @PreAuthorize("hasRole('event_creator')")
    @GetMapping("creator")
    public String helloWorldCreator(@AuthenticationPrincipal UserPrincipal user) {
        return "Hello creator," + user.firstname() + " you are able to create events";
    }
}
