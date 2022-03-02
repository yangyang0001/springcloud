package com.deepblue.command;

import lombok.*;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Invoker {

    private Command command;

    public void inaction() {
        command.execute();
    }

}
