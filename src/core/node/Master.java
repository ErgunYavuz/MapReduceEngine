package core.node;

import java.io.*;
import java.util.*;

import core.model.DataChunk;
import core.model.Pair;

public class Master extends Node {
    private List<String> workerAddresses;
    private List<Integer> workerPorts;

    public Master(String address, int port, List<String> workerAddresses, List<Integer> workerPorts) throws IOException {
        super(address, port);
        this.workerAddresses = workerAddresses;
        this.workerPorts = workerPorts;
    }

    public void distributeData(List<String> data) throws IOException {
        int chunkSize = data.size() / workerAddresses.size();
        for (int i = 0; i < workerAddresses.size(); i++) {
            int start = i * chunkSize;
            int end = (i == workerAddresses.size() - 1) ? data.size() : (i + 1) * chunkSize;
            DataChunk chunk = new DataChunk(data.subList(start, end));
            send(workerAddresses.get(i), workerPorts.get(i), chunk);
        }
    }

    public List<Pair<String, Integer>> collectResults() throws IOException, ClassNotFoundException {
        List<Pair<String, Integer>> results = new ArrayList<>();
        for (int i = 0; i < workerAddresses.size(); i++) {
            results.addAll((List<Pair<String, Integer>>) receive());
        }
        return results;
    }
}