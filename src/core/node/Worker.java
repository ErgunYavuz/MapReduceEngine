package core.node;
import java.io.*;
import java.util.*;

import core.model.DataChunk;
import core.model.Pair;

public class Worker extends Node {
    private String masterAddress;
    private int masterPort;

    public Worker(String address, int port, String masterAddress, int masterPort) throws IOException {
        super(address, port);
        this.masterAddress = masterAddress;
        this.masterPort = masterPort;
    }

    public void run() throws IOException, ClassNotFoundException {
        DataChunk chunk = (DataChunk) receive();
        List<String> data = chunk.getData();
        List<Pair<String, Integer>> mappedData = new ArrayList<>();
        for (String item : data) {
            mappedData.addAll(mapFunction(item));
        }
        send(masterAddress, masterPort, mappedData);
    }

    protected List<Pair<String, Integer>> mapFunction(String item) {
        // This should be overridden by the user
        throw new UnsupportedOperationException("Map function must be implemented");
    }
}