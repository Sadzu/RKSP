package ru.nstu.rksp_lab_4_server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nstu.rksp_lab_4_server.core.service.ObjectService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ObjectController {

    @Autowired
    private ObjectService objectService;

    @GetMapping("/register")
    public Integer register() {
        return objectService.register();
    }

    @GetMapping("/connections")
    public List<Integer> connections() {
        return objectService.connections();
    }

    @PostMapping("/send/{targetId}")
    public Boolean send(
            @PathVariable Integer targetId,
            @RequestBody String json
    ) {
        return objectService.send(targetId, json);
    }

    @GetMapping("/receive")
    public String receive(@RequestHeader("X-Client-Id") String clientId) {
        return objectService.receive(clientId);
    }

}
