package edu.pet;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("parallel working module yeeeah");
        for (int i = 0; i < 100; i++) {
            System.out.println("\t(working)");
            Thread.sleep(1000);
        }
    }
}