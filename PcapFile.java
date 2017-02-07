package com.pcap;

import edu.gatech.sjpcap.*;


/**
 * @author JamesDavies
 * @date 05/02/2017
 * PCAP
 */
public class PcapFile {

    private String input;

    public PcapFile(String input) throws Exception {
        this.input = input;
    }

    /**
     * Extract the file and return the contents
     */
    public String read() {

        try {
            PcapParser parser = new PcapParser();

            if (parser.openFile(this.input) < 0) {
                System.err.println("Failed to open " + this.input + ", exiting.");
                return null;
            }

            Packet packet = parser.getPacket();

            String fileContents = null;

            while (packet != Packet.EOF) {
                if (!(packet instanceof IPPacket)) {
                    packet = parser.getPacket();
                    continue;
                }

                IPPacket ipPacket = (IPPacket) packet;
                byte[] payload = new byte[0];

                if (ipPacket instanceof TCPPacket) {
                    TCPPacket tcpPacket = (TCPPacket) ipPacket;
                    payload = tcpPacket.data;
                }

                String request = new String(payload, "UTF-8");

                if (request.contains("ENCFILE")) {
                    // Decode the file to be decrypted
                    fileContents = request.substring(request.indexOf("#") + 11, request.indexOf("####"));
                    break;
                }

                packet = parser.getPacket();
            }

            parser.closeFile();

            return fileContents;

        } catch (Exception e) {
            System.out.println("Couldn't read the PCAP file.");
        }

        return null;
    }
}
