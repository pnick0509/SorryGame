package com.company;

public class Timer {

    long markTime; //The time when the timer was started
    long waitFor; //How much time to wait for in nano seconds

    Timer(double secs){
        set(secs);
    }

    public void restart(){
        markTime = System.nanoTime();
        //System.out.println("Nano: "+markTime+" "+waitFor);
    }

    public void set(double secs){
        restart();
        waitFor = (long)(secs*1000000000);
    }

    public boolean checkTime(){
        return (System.nanoTime() > markTime+waitFor);
    }
}
