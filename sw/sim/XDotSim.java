/*
 * a comment
 */


package models;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import numbers.atmConverter;

public class XDotSim {
	static Enumeration<CommPortIdentifier> portList;
    static CommPortIdentifier portId;
    static SerialPort serialPort;
    static OutputStream outputStream = null;
    static InputStream inputStream = null;
    static Date startTime= new Date();
    static Date currentTime = new Date(startTime.getTime());
    static byte[] msg;
    static byte[] incoming = new byte[2048];
    static String msgString = "";
    public String deviceWISESessionId = "";
    static HttpURLConnectionExample httpConnection;
    
    public XDotSim() {
    	httpConnection = new HttpURLConnectionExample();
    	try {
			this.deviceWISESessionId = httpConnection.getDeviceWISESessionId();
			System.out.println("deviceWISE SessionId: " + deviceWISESessionId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("could not get SessionId, exiting ...");
			e.printStackTrace();
		}
    }
   
    //@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		String targetPortNumber = args[0];
		String messageType = args[1];
		System.out.println("args[0]: <" + targetPortNumber + ">  args[1]: <" + messageType + ">\n");    	

		for (int z = 0; z < args.length; z++) {
			System.out.println("args[" + z + "]: <" + args[z] + ">");    	
		}
    	System.out.println("Made it to main() ...");
    	System.out.println("StartTime: " + startTime.toString() + "  currentTime: " + currentTime.toString());
    	
		XDotSim sim = new XDotSim();

		if ((outputStream = sim.getOutputStream(targetPortNumber)) == null) {
			System.out.println("outputStream == null, closing ...");
			serialPort.close();
			System.exit(1);
		}
		System.out.println("serialPort: " + serialPort.getName());
		System.out.println("outputStream: " + outputStream.toString());
		
		if ((inputStream = sim.getInputStream(serialPort)) == null) {
			System.out.println("serialPort: " + serialPort.getName());
			System.out.println("inputStream: " + inputStream.toString());
			System.out.println("inputStream == null, closing ...");
			serialPort.close();
			System.exit(1);
		}
		
		if (messageType == null) {
			System.out.println("messageType == null, closing ...");
        	serialPort.close();
        	System.exit(1);
		};
		
		switch (messageType) {
			case "short": 
				System.out.println("switch->short ...");
				if (args[2] == null) {
					System.out.println("args[2] == null, closing ...");
		        	serialPort.close();
		        	System.exit(1);
				}
				msg = buildShortMessage(args[2]);
				try {
		    		outputStream.write(msg, 0, msg.length);
		    		outputStream.flush();
		    		outputStream.flush();
		    		outputStream.flush();
		    		System.out.println("Successful write ...");
		    		} catch (IOException e) {
		    			System.out.println("IOException e: " + e.getMessage() + "\n");
		    			System.out.println("IOException e: " + e.toString() + "\n");
		    			e.printStackTrace();
		    			}
				break;
			case "string":
				System.out.println("switch->string ...");
				if (args[2] == null) {
					System.out.println("args[2] == null, closing ...");
		        	serialPort.close();
		        	System.exit(1);
				}
				System.out.println("building String message with args[2]: " + args[2]);
				msg = buildStringMessage(args[2] + "\r" + "\n");
				try {
		    		outputStream.write(msg);
		    		outputStream.flush();
		    		System.out.println("Successful write ...");
		    		} catch (IOException e) {
		    			System.out.println("IOException e: " + e.getMessage() + "\n");
		    			System.out.println("IOException e: " + e.toString() + "\n");
		    			e.printStackTrace();
		    			}
				try {
		    		Thread.sleep(3000);
				} catch (InterruptedException e) {
					//
				}
				try {
					int k = 0;
					while ( (k = inputStream.available()) > 0) {
						System.out.println("k: " + k);
						inputStream.read(incoming, 0, k);
						String s = new String(incoming);
						System.out.println("incoming: " + s);
						}
					} catch (IOException e1) {
						System.out.println("cannot read input stream, closing ...");
			        	serialPort.close();
						e1.printStackTrace();
						}
				break;
			case "init": 
				System.out.println("switch->init ...");
				if (args[2] == null) {
					System.out.println("args[2] == null, closing ...");
		        	serialPort.close();
		        	System.exit(1);
				}
				msg = args[2].getBytes();
				try {
		    		outputStream.write(msg);
		    		outputStream.flush();
		    		System.out.println("Successful write ...");
		    		} catch (IOException e) {
		    			System.out.println("IOException e: " + e.getMessage() + "\n");
		    			System.out.println("IOException e: " + e.toString() + "\n");
		    			e.printStackTrace();
		    			}
				break;
			case "AT" :
				System.out.println("switch->AT ...");
				if (args.length <  2) {
					System.out.println("args[2] == null, closing ...");
		        	serialPort.close();
		        	System.exit(1);
				}
				msgString = args[2] + "\r";
				try {
					OutputStreamWriter osw = new OutputStreamWriter(outputStream);
					BufferedWriter bw = new BufferedWriter(osw);
					System.out.println("writing: <" + msgString + ">");
					osw.write(msgString, 0, msgString.length());
					osw.flush();
					//bw.write(msgString, 0, msgString.length());
					//bw.flush();
					try {
			    		Thread.sleep(3000);
					} catch (InterruptedException e) { 
					}
					
					bw.close();
					osw.close();
		    		System.out.println("Successful write ...");
		    		} catch (IOException e) {
		    			System.out.println("IOException e: " + e.getMessage() + "\n");
		    			System.out.println("IOException e: " + e.toString() + "\n");
		    			e.printStackTrace();
		    			}
				break;
			case "TEST" :
				System.out.println("switch->TEST ...");
				System.out.println("args.length: " + args.length);
				for (int y=0; y < args.length; y++) {
						System.out.println("args[" + y + "]: " + args[y]);
					}
				int K = (args.length <  3) ? (int)0 : Integer.parseInt(args[2]);
				try {
					OutputStreamWriter osw = new OutputStreamWriter(outputStream);
					BufferedWriter bw = new BufferedWriter(osw);
					for (int g = 0; g < K ; g++) {
						String randomMessageSequence = "";
						String theMessage = "";
						MessageGenerator mg = new MessageGenerator();
						randomMessageSequence = mg.generateMessageSectionSequence();
						System.out.println("randomMessage: " + randomMessageSequence);
						MessageGeneratorResult theResult = mg.generateMessage(randomMessageSequence);
						theMessage = theResult.resultMessage;
						theMessage = "AT+SEND=" + theMessage + "\r";
						
						if (theMessage.length() <= 129) {
							System.out.println("writing: <" + theMessage + ">");
							System.out.println("size: " + theMessage.length());
							
							osw.write(theMessage, 0, theMessage.length());
							osw.flush();
							
							//bw.write(msgString, 0, msgString.length());
							//bw.flush();
							System.out.println("Successful write ...");
							try {
								Thread.sleep(3000);
								httpConnection.runTests(theResult.testCases);
								} catch (InterruptedException e) {
									e.printStackTrace();
									}
						} else {
							g--;
							System.out.println("theMessage.length(): <" + theMessage.length() + "> is too long ...");
							}
						}
					bw.close();
					osw.close();
					} catch (IOException e) {
		    			System.out.println("IOException e: " + e.getMessage() + "\n");
		    			System.out.println("IOException e: " + e.toString() + "\n");
		    			e.printStackTrace();
		    			}
				break;
				
			default:
				System.out.println("switch->default ...");
				break;
		}

    	
    	try {
    		Thread.sleep(3000);
	        			/*try {
	        				outputStream.write(stop);
							outputStream.write(start);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
    		} catch (InterruptedException e) {
    			System.out.println("InterruptedException e: " + e.getMessage());
    			e.printStackTrace();
    			System.exit(1);
    			}
    	serialPort.close();
    	System.out.println("Serial port closed, shutting down ...");
    	System.exit(1);
    	}
	
	private static byte[] buildShortMessage(String arg) {
		byte[] message = null;
    	System.out.printf("\nMade it to buildShortMessage() ...");
		
		message = new byte[2];
		atmConverter conv = new atmConverter();
		short aShort = Short.parseShort(arg);
		message[0] = conv.short2bytes(aShort)[0];
		message[1] = conv.short2bytes(aShort)[1];
			
		System.out.printf("\nXDotSimulator()->main()->\ti: %d \tr[0]: %d(0x%02x)  \tr[1]: %d(0x%02x) \treassembled_short: %d(0x%04x)\n", 
				aShort, 
				conv.short2bytes(aShort)[0], 
				conv.short2bytes(aShort)[0], 
				conv.short2bytes(aShort)[1], 
				conv.short2bytes(aShort)[1], 
				conv.bytes2short(conv.short2bytes(aShort)), 
				conv.bytes2short(conv.short2bytes(aShort)));
		
		System.out.println("message built; size of message: " + message.length);
		return message;
		}
	
	private static byte[] buildStringMessage(String arg) {
		if (arg.isEmpty()) {
			System.out.printf("\nbuildStringMessage(); arg is empty ...");
			return null;
		}
		byte[] message = arg.getBytes();
    	System.out.printf("\nMade it to buildStringMessage() ...");
		System.out.printf("\nstringMessage: %s", arg);
		System.out.println("\nmessage built; size of message: " + message.length);
		return message;
		}
		

	private OutputStream getOutputStream(String targetPortNumber) {
		OutputStream result = null;
		// TODO Auto-generated method stub
		
		portList = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Got the portList ...");
        while (portList.hasMoreElements()) {
        	System.out.println("Inside the while portList.hasMoreElements loop ...");
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("Got the portID ...");
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	System.out.println("portType is serial and portID = " + portId.getName());
            	if (portId.getName().equals(targetPortNumber)) { // USB with Sparkfun FTDI Breakout // on Dell OptiPlex
            		System.out.println("port is <" + targetPortNumber + ">");
                    try {
                    	serialPort = (SerialPort) portId.open("xDotSimulator", 2000);
                        System.out.println("Serial port opened ... ");
                        } catch (PortInUseException e) {}
                    if (serialPort != null) {
                    	try {
                    		serialPort.setSerialPortParams(115200,
                    				SerialPort.DATABITS_8,
                    				SerialPort.STOPBITS_1,
                    				SerialPort.PARITY_NONE);
                    		} catch (UnsupportedCommOperationException e) {}
                    	System.out.println("Serial port params set ... ");
                    	
                    	}
                    try {
                    	outputStream = serialPort.getOutputStream();
                        } catch (IOException|NullPointerException e) {
                        	System.out.println("outputStream not obtained; closing ... ");
                        	serialPort.close();
                    		System.exit(1);
                        }
                    System.out.println("Obtained outputStream ... ");
                    result = outputStream;
                	break;
                    }
            	}
            }
        return result;
        }
	
	private InputStream getInputStream(SerialPort serialPort2) {
		InputStream result = null;
		try {
        	inputStream = serialPort.getInputStream();
            } catch (IOException|NullPointerException e) {
            	System.out.println("inputStream not obtained; closing ... ");
            	serialPort.close();
        		System.exit(1);
            }
		result = inputStream;
		System.out.println("Obtained inputStream ... ");
		return result;
		}
	}

