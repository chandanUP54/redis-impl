package com.project_demo.redis.modal;


import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    private String userId;
    private String name;
    private String phone;
    private String email;

}