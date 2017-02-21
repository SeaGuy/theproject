package edu.fau.isense.serialCommFun;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class XBeeRPiWRITESimulator {
    static Enumeration<CommPortIdentifier> portList;
    static CommPortIdentifier portId;
    //static String messageString = "Hello, world!\n";
    static SerialPort serialPort;
    static OutputStream outputStream = null;

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	System.out.println("Made it to main() ...");
        portList = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Got the portList ...");
        while (portList.hasMoreElements()) {
        	System.out.println("Inside the while portList.hasMoreElements loop ...");
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("Got the portID ...");
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	System.out.println("portType is serial and portID = " + portId.getName());
            	//if (portId.getName().equals("/dev/term/a")) {
            	if (portId.getName().equals("COM17")) { // USB with Sparkfun FTDI Breakout
            		System.out.println("port is COM17 ...");
                    try {
                        serialPort = (SerialPort) portId.open("XBeeRPiWRITESimulator", 2000);
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
                    	System.out.println("outputStream == null");
                    	System.exit(1);
                    }
                    while(true) {
                    	//try {
                    	System.out.println("Made it to the while loop ...");
                    		byte[] message = new byte[8];
                    		message[0] = 0x7E;	//start of message byte
                    		message[1] = 1;		//orgID
                			message[2] = 2;		//networkID
                			message[3] = (byte) ThreadLocalRandom.current().nextInt(2, 4); //sourceID
                			message[4] = 1;		//destID
                			message[5] = 1;		//command
                			message[6] = 0;		//reserved
                			message[7] = 0x7F;	//end of message byte
                			System.out.println("message built ...");
                    		try {
                    			outputStream.write(message);
                    		} catch (IOException e) {
                    			System.out.println("IOException e: " + e.getMessage() + "\n");
                    			System.out.println("IOException e: " + e.toString() + "\n");
								e.printStackTrace();
								try {
									serialPort.close();
			                        serialPort = (SerialPort) portId.open("XBeeRPiWRITESimulator", 2000);
			                    } catch (PortInUseException e1) {
			                    	System.out.println("PortInUseException ...");
			                    }
			                    try {
			                        outputStream = serialPort.getOutputStream();
			                    } catch (IOException|NullPointerException e2) {
			                    	System.out.println("IOException| NullPointerException");
			                    }
			                    if (outputStream != null) {
			                    	try {
			                    		serialPort.setSerialPortParams(115200,
			                    				SerialPort.DATABITS_8,
			                    				SerialPort.STOPBITS_1,
			                    				SerialPort.PARITY_NONE);
			                    	} catch (UnsupportedCommOperationException e3) {
			                    		System.out.println("UnsupportedCommOperationException");
			                    	}
			                    } else {
			                    	System.out.println("outputStream == null");
			                    	System.exit(1);
			                    }
                    		}
                    		try {
								Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 30000));
								} catch (InterruptedException e) {
									System.out.println("InterruptedException e: " + e.getMessage());
									e.printStackTrace();
									System.exit(1);
									}
                    		/*} catch (IOException e) {
                    			System.out.println("Exception e: " + e.getMessage());
                    			System.out.println("Exception e: " + e.toString());
								e.printStackTrace();
								System.exit(1);
								}*/
								
                    		}
                    	}
                    }
                 }
            }
        }

