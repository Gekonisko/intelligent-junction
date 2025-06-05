package org.example.traffic.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.traffic.model.StepStatus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWriter {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeToJsonFile(String path, List<StepStatus> results) throws Exception {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("stepStatuses", results);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), wrapper);
    }

    public static String toJsonString(List<StepStatus> results) throws Exception {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("stepStatuses", results);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
    }
}
