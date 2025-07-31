package com.example.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class B_ProcessData {

    private String code;

    private String name;

    private List<B_Processes> processes;



}
