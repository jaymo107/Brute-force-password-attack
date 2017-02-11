package com.pcap.misc;

import java.util.TimerTask;

/**
 * @author JamesDavies
 * @date 08/02/2017
 * PCAP
 */
public class DisplayElapsedTime extends TimerTask {
    @Override
    public void run() {
        System.out.println("[NOTICE] Attempting to crack password. Please wait...");
    }

}
