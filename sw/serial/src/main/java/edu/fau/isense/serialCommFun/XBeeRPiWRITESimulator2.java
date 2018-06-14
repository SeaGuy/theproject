package edu.fau.isense.serialCommFun;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class XBeeRPiWRITESimulator2 {
    static Enumeration<CommPortIdentifier> portList;
    static CommPortIdentifier portId;
    //static String messageString = "Hello, world!\n";
    static SerialPort serialPort;
    static OutputStream outputStream;

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM23")) { // USB with Sparkfun FTDI Breakout
                //if (portId.getName().equals("/dev/term/a")) {
                    try {
                        serialPort = (SerialPort)
                            portId.open("XBeeRPiWRITESimulator", 2000);
                    } catch (PortInUseException e) {}
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException|NullPointerException e) {}
                    if (outputStream != null) {
                    	try {
                    		serialPort.setSerialPortParams(115200,
                    				SerialPort.DATABITS_8,
                    				SerialPort.STOPBITS_1,
                    				SerialPort.PARITY_NONE);
                    	} catch (UnsupportedCommOperationException e) {}
                    } else {
                    	System.exit(1);
                    }
                    while(true) {
                    	try {
                    		byte[] message = new byte[8];
                    		message[0] = 0x7E;	//start of message byte
                    		message[1] = 1;		//orgID
                			message[2] = 3;		//networkID
                			message[3] = (byte) ThreadLocalRandom.current().nextInt(2, 4); //sourceID
                			message[4] = 1;		//destID
                			message[5] = 1;		//command
                			message[6] = 0;		//reserved
                			message[7] = 0x7F;	//end of message byte
                			System.out.println("message built ...");
                    		outputStream.write(message);
                    		try {
								Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 30000));
								} catch (InterruptedException e) {
									e.printStackTrace();
									}
                    		} catch (IOException e) {}
                    	}
                    }
                 }
            }
        }
    }

 


