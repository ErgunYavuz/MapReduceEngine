package core;
import java.io.*;
import java.util.*;

import core.model.Pair;
import core.node.Master;
import core.node.Worker;

public class MapReduceEngine {
    private String masterAddress;
    private int masterPort;
    private List<String> workerAddresses;
    private List<Integer> workerPorts;

    public MapReduceEngine(String masterAddress, int masterPort,
                                      List<String> workerAddresses, List<Integer> workerPorts) {
        this.masterAddress = masterAddress;
        this.masterPort = masterPort;
        this.workerAddresses = workerAddresses;
        this.workerPorts = workerPorts;
    }

    protected List<Pair<String, Integer>> mapFunction(String item) {
        // This should be overridden by the user
        throw new UnsupportedOperationException("Map function must be implemented");
    }

    protected int reduceFunction(String key, List<Integer> values) {
        // This should be overridden by the user
        throw new UnsupportedOperationException("Reduce function must be implemented");
    }

    public Map<String, Integer> run(List<String> data) throws IOException, ClassNotFoundException, InterruptedException {
        // Start worker threads
        List<Thread> workerThreads = new ArrayList<>();
        for (int i = 0; i < workerAddresses.size(); i++) {
            final int index = i;
            Thread workerThread = new Thread(() -> {
                try {
                    Worker worker = new Worker(workerAddresses.get(index), workerPorts.get(index), masterAddress, masterPort) {
                        @Override
                        protected List<Pair<String, Integer>> mapFunction(String item) {
                            return MapReduceEngine.this.mapFunction(item);
                        }
                    };
                    worker.run();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            workerThread.start();
            workerThreads.add(workerThread);
        }

        // Start master and distribute data
        Master master = new Master(masterAddress, masterPort, workerAddresses, workerPorts);
        master.distributeData(data);

        // Collect results
        List<Pair<String, Integer>> mappedData = master.collectResults();

        // Wait for workers to finish
        for (Thread thread : workerThreads) {
            thread.join();
        }

        // Shuffle phase
        Map<String, List<Integer>> shuffledData = new HashMap<>();
        for (Pair<String, Integer> pair : mappedData) {
            shuffledData.computeIfAbsent(pair.getKey(), k -> new ArrayList<>()).add(pair.getValue());
        }

        // Reduce phase
        Map<String, Integer> reducedData = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : shuffledData.entrySet()) {
            reducedData.put(entry.getKey(), reduceFunction(entry.getKey(), entry.getValue()));
        }

        return reducedData;
    }
}