package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.models.Chat;
import com.papelariafrasato.api.services.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(
        name = "Chat",
        description = "Chat endpoints"
)
public class ChatController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/{id}")
    @Operation(
            summary = "Create chat",
            description = "Create a new chat"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created chat"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<String> createChat(@PathVariable("id") String userId){
        if(userId.isEmpty()){
            return ResponseEntity.status(404).body("Missing some information");
        }

        String chatId = webSocketService.createChat(userId);
        return ResponseEntity.status(201).body(chatId);
    }

    @GetMapping()
    @Operation(
            summary = "List chats",
            description = "Endpoint to list all activated chats"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Founded Chats")
    })
    public ResponseEntity<List<Chat>> listAllChats(){
        List<Chat> chats = webSocketService.getActiveChats();
        return ResponseEntity.ok().body(chats);
    }

}
