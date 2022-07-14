package org.example.CommadsAnalyser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Command {
    private final List<CommandParameter> parameters;

    Command(CommandParameter... parameters) {
        this.parameters = new ArrayList<>(Arrays.asList(parameters));
    }

    List<CommandParameter> getParameters() {
        return parameters;
    }
}
