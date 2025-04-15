package io.tanibilet.server;

import io.tanibilet.server.auth.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// controller for testing purposes, can be remove later
@RestController
@RequestMapping("/")
public class HelloWorldController {


    @GetMapping
    public String helloWorld(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getPreferredUsername();
    }

    @PreAuthorize("hasRole('event_creator')")
    @GetMapping("creator")
    public String helloWorldCreator(@AuthenticationPrincipal UserPrincipal user) {
        return "Hello creator," + user.firstname() + " you are able to create events";
    }
}
