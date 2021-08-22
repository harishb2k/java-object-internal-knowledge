package org.example;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.atomic.AtomicBoolean;

public class AgeOfObject {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    /**
     * A simple object - to show its age. This will show age = 0
     */
    public static void ageOfObject() {
        Object someObject = new Object();
        ClassLayout layout = ClassLayout.parseInstance(someObject);
        System.out.println(layout.toPrintable());
    }

    /**
     * A simple object which will change age as time passes (due to GC)
     */
    public static void ageOfObjectSeeItIncreasing() {
        AtomicBoolean keepRunning = new AtomicBoolean(true);
        Object someObject = new Object();
        new Thread(() -> {
            while (keepRunning.get()) {
                for (int i = 0; i < 10; i++) {
                    sleep(1000);
                    ClassLayout layout = ClassLayout.parseInstance(someObject);
                    System.out.println("---------------- Age Of Object (" + i + ") ----------------");
                    System.out.println(layout.toPrintable());
                }
                keepRunning.set(false);
            }
        }).start();

        new Thread(() -> {
            while (keepRunning.get()) {
                for (int i = 0; i < 100000; i++) {
                    byte[] n = new byte[100000];
                    sleep(1);
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("---------------- Age Of Object ----------------");
        AgeOfObject.ageOfObject();

        System.out.println("---------------- Age Of Object (changing as GC runs) ----------------");
        ageOfObjectSeeItIncreasing();

        Thread.sleep(10000);
    }
}
