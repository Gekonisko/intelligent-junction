package org.example.traffic.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Command> loadCommands(String path) throws Exception {
        Map<String, List<Command>> root = mapper.readValue(new File(path),
                new TypeReference<>() {});
        return root.get("commands");
    }
}