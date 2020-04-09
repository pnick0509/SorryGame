package com.company;

public class Timer {

    long markTime; //The time when the timer was started
    long waitFor; //How much time to wait for in nano seconds

    Timer(){
        restart();
    }

    Timer(double secs){
        set(secs);
    }

    public void restart(){
        markTime = System.nanoTime();
    }

    public void set(double secs){
        restart();
        waitFor = toNano(secs);
    }

    public double percent(){
        return System.nanoTime()-waitFor/(markTime+waitFor);
    }

    public boolean checkTime(){
        return (System.nanoTime() > markTime+waitFor);
    }

    private long toNano(double seconds){
        return (long)seconds*1000000000;
    }
}
