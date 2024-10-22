package com.is4tech.invoicemanagement.utils;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String note;
    private Object object;
}