package com.is4tech.invoicemanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.is4tech.invoicemanagement.dto.UserDto;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto getUserById(Integer userId) {
        String url = "http://{IP_DEL_PROYECTO_1}/api/users/" + userId;
        return restTemplate.getForObject(url, UserDto.class);
    }
}
