import java.io.IOException;
import java.util.*;

import core.MapReduceEngine;
import core.model.Pair;

public class WordCount {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String masterAddress = "localhost";
        int masterPort = 5000;
        List<String> workerAddresses = Arrays.asList("localhost", "localhost", "localhost");
        List<Integer> workerPorts = Arrays.asList(5001, 5002, 5003);

        MapReduceEngine engine = new MapReduceEngine(masterAddress, masterPort, workerAddresses, workerPorts) {
            @Override
            protected List<Pair<String, Integer>> mapFunction(String item) {
                List<Pair<String, Integer>> result = new ArrayList<>();
                for (String word : item.toLowerCase().split("\\s+")) {
                    result.add(new Pair<>(word, 1));
                }
                return result;
            }

            @Override
            protected int reduceFunction(String key, List<Integer> values) {
                return values.stream().mapToInt(Integer::intValue).sum();
            }
        };

        List<String> data = Arrays.asList(
            "Hello world",
            "Hello MapReduce",
            "MapReduce is awesome",
            "Hello again world",
            "Distributed systems are complex",
            "But they are very powerful"
        );

        Map<String, Integer> result = engine.run(data);
        System.out.println(result);
    }
}