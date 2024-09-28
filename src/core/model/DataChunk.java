package core.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class DataChunk implements Serializable {
    private List<String> data;

    public DataChunk(List<String> data) {
        this.data = new ArrayList<>(data); // Create a new ArrayList to ensure serializability
    }

    public List<String> getData() {
        return data;
    }
}