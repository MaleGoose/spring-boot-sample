package com.example.sample_project.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TransferOwnershipCommand {

    @Email
    private String from;
    @Email
    private String to;
}
