package com.is4tech.invoicemanagement.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Message {
    private String note;
    private Object object;
}