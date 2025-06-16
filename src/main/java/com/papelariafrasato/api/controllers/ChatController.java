package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.ChatDto;
import com.papelariafrasato.api.dtos.ResponseCreateChatDto;
import com.papelariafrasato.api.dtos.ResponseListChatsDto;
import com.papelariafrasato.api.models.Chat;
import com.papelariafrasato.api.services.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @ApiResponse(responseCode = "201", description = "Created chat", content = @Content(schema = @Schema(implementation = ResponseCreateChatDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> createChat(@PathVariable("id") String userId){
        if(userId.isEmpty()){
            return ResponseEntity.status(404).body("Missing some information");
        }

        String chatId = webSocketService.createChat(userId);
        return ResponseEntity.status(201).body(new ResponseCreateChatDto(chatId));
    }

    @GetMapping()
    @Operation(
            summary = "List chats",
            description = "Endpoint to list all activated chats"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Founded Chats", content = @Content(schema = @Schema(implementation = ResponseListChatsDto.class)))
    })
    public ResponseEntity<ResponseListChatsDto> listAllChats(){
        List<Chat> chats = webSocketService.getActiveChats();
        return ResponseEntity.ok().body(new ResponseListChatsDto(chats));
    }

    @GetMapping("/{chatId}/messages")
    @Operation(
            summary = "History chats",
            description = "Endpoint to list all messages history on a chat"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success on get history")
    })
    public ResponseEntity<List<ChatDto>> getChatMessages(@PathVariable String chatId){
        List<ChatDto> messages = webSocketService.getChatHistory(chatId);
        return ResponseEntity.ok(messages);
    }
}