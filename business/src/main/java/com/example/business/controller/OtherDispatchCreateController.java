package com.example.business.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

    @RestController
    @RequestMapping("/OtherDispatchCreateApi")
    @Slf4j
public class OtherDispatchCreateController {



    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/OtherDispatchCreate")
    public ResponseEntity<String>  OtherDispatchCreate(@RequestBody Map<String,Object> message){
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            System.out.println(jsonMessage);
            return new ResponseEntity<>(jsonMessage, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error converting message to JSON",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
