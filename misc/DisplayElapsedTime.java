package com.pcap.misc;

import java.util.TimerTask;

/**
 * @author JamesDavies
 * @date 08/02/2017
 * PCAP
 */
public class DisplayElapsedTime extends TimerTask {

    private int secondsElapsed = 0;

    @Override
    public void run() {
        secondsElapsed++;
        System.out.println("[NOTICE] Attempting to crack password. (" + secondsElapsed + " seconds elapsed)");
    }

}
