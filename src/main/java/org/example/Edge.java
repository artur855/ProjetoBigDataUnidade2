package org.example;

import java.io.Serializable;
import java.util.UUID;

public class Edge implements Serializable {
    private UUID id;
    private String src;
    private String dst;
    private int distance;
    private int weight;


    public Edge(String src, String dst){
        this(src, dst, 1);
    }


    public Edge(String src, String dst, int distance) {
        this.src = src;
        this.dst = dst;
        this.distance = distance;
        this.weight = distance;
        this.id = UUID.randomUUID();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
