package edu.fau.isense.serialCommFun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SimpleRead2 implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration<CommPortIdentifier> portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	//portList = portId.getPortIdentifiers();
        portList = CommPortIdentifier.getPortIdentifiers();
        System.out.println("portList: " + portList.toString());

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("portId " + portId);
            if (portId.getName().equals("COM17") && portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                	 System.out.println("portId.getName(): " + portId.getName());
                 //if (portId.getName().equals("/dev/term/a")) {
                    try {
                    	@SuppressWarnings("unused")
						SimpleRead2 reader = new SimpleRead2();
                    } catch(Exception e) {
                    	System.out.println("Exception at SimpleRead()");
                    	System.out.println(e.getMessage());
                    }
                }
        }
    }

    public SimpleRead2() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {System.out.println(e);}
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(115200,
							                SerialPort.DATABITS_8,
							                SerialPort.STOPBITS_1,
							                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        
        System.out.println("getReceiveThreshold " + serialPort.getReceiveThreshold());
        System.out.println("getReceiveTimeout " + serialPort.getReceiveTimeout());

        readThread = new Thread(this);
        readThread.start();
        //serialPort.close();
    }

    public void run() {
        try {
            Thread.sleep(20000);
            System.out.println("inside run() ... ");
        } catch (InterruptedException e) {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event) {
    	//String str = "";
        switch(event.getEventType()) {
        	case SerialPortEvent.BI:
        	case SerialPortEvent.OE:
	        case SerialPortEvent.FE:
	        case SerialPortEvent.PE:
	        case SerialPortEvent.CD:
	        case SerialPortEvent.CTS:
	        case SerialPortEvent.DSR:
	        case SerialPortEvent.RI:
	        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	            break;
	        case SerialPortEvent.DATA_AVAILABLE:
	            //byte[] readBuffer = new byte[16];
	        	char[] readBuffer = new char[16];
	            int i3 = 0;
	            int i  = 0;
	            try {
	            	System.out.println("inputStream.available:  " + inputStream.available());
	                /*while (inputStream.available() > 0) {
	                	int numBytes = inputStream.read(readBuffer);
	                    int i = 0;
	                    for (byte b : readBuffer) {
	                    	str += (char) b;
	                    }
	                    System.out.println("str [" + str +"]");
	                    str = "";
	                    System.out.println("readbuffer [" + new String(readBuffer) + "]");
	                    System.out.println("inside serialEvent inputStream.available  > 0");
	                }*/
	                while ((i3 = inputStream.read()) != -1) {
	                    readBuffer[i++] = (char) i3;
	                    
	                }
	                System.out.println("readbuffer [" +  readBuffer.toString() + "]");
	                //System.out.println("readbuffer [" + new String(readBuffer) + "]");
	                i3 = 0;
	                i = 0;
	                System.out.println("after the while loop:  " + new String(readBuffer));
	                readBuffer[0] = 0;
	            } catch (IOException e) {System.out.println(e);}
	            break;
        }
    }
}