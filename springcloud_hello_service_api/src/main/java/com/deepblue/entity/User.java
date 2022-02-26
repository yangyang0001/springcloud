package com.deepblue.entity;

import lombok.*;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    private Long userId;

    private String username;

    private String password;
}
