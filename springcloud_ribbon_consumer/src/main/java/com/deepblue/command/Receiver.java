package com.deepblue.command;

import lombok.*;

/**
 *
 */
@Data
@Builder
@ToString
public class Receiver {

    public void action() {
        System.out.println("receiver action method invoke");
    }
}
