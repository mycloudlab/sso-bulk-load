package org.acme;

import java.util.UUID;

public class SSOID {
    
    public static String generate() {
        return UUID.randomUUID().toString();
    }
    public static void main(String[] args) {
        System.out.println();
        System.out.println("SSOID.generate() = " + SSOID.generate());
        System.out.println();
    }
}
