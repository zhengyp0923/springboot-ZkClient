package com.example.demo.domain;

import java.io.Serializable;

public class Node implements Serializable{
    private static final long serialVersionUID = 5658260886902799655L;
    private String host;
    private Integer port;

    public Node() {
    }

    public Node(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Node{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
