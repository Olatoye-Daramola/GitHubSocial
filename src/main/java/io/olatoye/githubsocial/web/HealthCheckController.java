package io.olatoye.githubsocial.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/github_social/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> pingForLife() {
        return ResponseEntity.ok("I'M ALIVE!");
    }
}
