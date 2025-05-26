package com.harsh;

import com.harsh.application.CacheDemo;

public class Main {
    public static void main(String[] args) {
        try {
            CacheDemo.run();
        }
        catch (Exception e) {
            System.out.println("An error occurred while running the cache demo: " + e.getMessage());
        }
    }
}