package ru.nstu.rksp_lab_4_server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nstu.rksp_lab_4_server.core.service.ObjectService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Main app controller")
public class ObjectController {

    private final ObjectService objectService;

    @Operation(
            summary = "register client",
            description = "returns client's id"
    )
    @GetMapping("/register")
    public Integer register() {
        return objectService.register();
    }

    @Operation(
            summary = "get list of connections",
            description = "returns list of ids of connected client"
    )
    @GetMapping("/connections")
    public List<Integer> connections() {
        return objectService.connections();
    }

    @Operation(
            summary = "send objects to client by id",
            description = "sends objects to client with path variable id and returns boolean as success flag"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objects need to be send",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "simple user",
                            value = """
                            [
                            {
                            "width": 25,
                            "height": 13,
                            "type": "Rectangle",
                            "x": 38,
                            "y": 71
                            }
                            ]
                            """
                    )
            )
    )
    @PostMapping("/send/{targetId}")
    public Boolean send(
            @PathVariable Integer targetId,
            @RequestBody String json
    ) {
        return objectService.send(targetId, json);
    }

    @Operation(
            summary = "get objects from storage",
            description = "returns list of objects from storage, send to client id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "objects has",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "simple user",
                            value = """
                            [
                            {
                            "width": 25,
                            "height": 13,
                            "type": "Rectangle",
                            "x": 38,
                            "y": 71
                            }
                            ]
                            """
                    )
            )
    )
    @GetMapping("/receive")
    public String receive(@RequestHeader("X-Client-Id") String clientId) {
        return objectService.receive(clientId);
    }

    @Operation(
            summary = "get all objects of client by client's id",
            description = "returns all objects of client"
    )
    @ApiResponse(
            responseCode = "200",
            description = "objects has",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "simple user",
                            value = """
                            [
                            {
                            "width": 25,
                            "height": 13,
                            "type": "Rectangle",
                            "x": 38,
                            "y": 71
                            }
                            ]
                            """
                    )
            )
    )
    @GetMapping("/get-all/{clientId}")
    public String getAll(@PathVariable Integer clientId) {
        return objectService.getAll(clientId);
    }

    @Operation(
            summary = "refreshes permanent object storage",
            description = "refreshes permanent storage on the server with the gotten request body"
    )
    @PostMapping("/put-new")
    public boolean putNew(@RequestHeader("X-Client-Id") String clientId, @RequestBody String object) {
        return objectService.putNew(Integer.parseInt(clientId), object);
    }

}
