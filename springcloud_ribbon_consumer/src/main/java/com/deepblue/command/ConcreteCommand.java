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
public class ConcreteCommand implements Command{

    private Receiver recevier;

    @Override
    public void execute() {
        recevier.action();
    }
}
