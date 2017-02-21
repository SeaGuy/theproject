package edu.fau.isense.serialCommFun;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SimpleWrite {
    
    static CommPortIdentifier portId;
    static Enumeration<CommPortIdentifier> portList;
    static String messageString = "Hello, world!\n";
    static SerialPort serialPort;
    static OutputStream outputStream;

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM8")) {
                //if (portId.getName().equals("/dev/term/a")) {
                    try {
                        serialPort = (SerialPort)
                            portId.open("SimpleWriteApp", 2000);
                    } catch (PortInUseException e) {}
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException|NullPointerException e) {}
                    if (outputStream != null) {
                    	try {
                    		serialPort.setSerialPortParams(9600,
                    				SerialPort.DATABITS_8,
                    				SerialPort.STOPBITS_1,
                    				SerialPort.PARITY_NONE);
                    	} catch (UnsupportedCommOperationException e) {}
                    } else {
                    	System.exit(1);
                    }
                    try {
                        outputStream.write(messageString.getBytes());
                    } catch (IOException e) {}
                }
            }
        }
    }
    //serialPort.close();
}
