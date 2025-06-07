package org.example.traffic;

import org.example.traffic.cli.TrafficCli;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new TrafficCli()).execute(args);
    }
}