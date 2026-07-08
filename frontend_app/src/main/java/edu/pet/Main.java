package edu.pet;

import edu.pet.networking.NetworkHandler;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");

        System.out.printf("id=2:\n\t%s\n", networkHandler.getById(2L));
        System.out.printf("mark done 2:\n\t%s\n", networkHandler.markDone(2L));
    }
}