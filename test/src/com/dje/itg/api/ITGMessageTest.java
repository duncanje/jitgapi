package com.dje.itg.api;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import com.dje.itg.api.ITGMessage;
import com.dje.itg.api.ITGApi;

import java.net.InetAddress;

public class ITGMessageTest {

	private InetAddress sender;
	private byte[] goodStartBuffer, goodEndBuffer, badBuffer;
	
	private ITGMessage startMessage, endMessage;
	
	private String message;

	@Before
	public void setUp() {
		try {
			sender = InetAddress.getLocalHost();
		} catch (Exception e) {
			fail("Failed to get localhost InetAddress");
		}
		
		message = "-a localhost -rp 10000 VoIP";
		
		goodStartBuffer = new byte[ITGTestConstants.BUFFER_LENGTH];
		goodStartBuffer[ITGTestConstants.TYPE_OFFSET] = 1;
		goodStartBuffer[ITGTestConstants.LENGTH_OFFSET] = (byte) message.length();
		insertIntoBuffer(message, ITGTestConstants.MESSAGE_OFFSET, goodStartBuffer);
		
		goodEndBuffer = new byte[ITGTestConstants.BUFFER_LENGTH];
		goodEndBuffer[ITGTestConstants.TYPE_OFFSET] = 2;
		goodEndBuffer[ITGTestConstants.LENGTH_OFFSET] = (byte) message.length();
		insertIntoBuffer(message, ITGTestConstants.MESSAGE_OFFSET, goodEndBuffer);
		
		badBuffer = new byte[ITGTestConstants.BUFFER_LENGTH];
		badBuffer[ITGTestConstants.TYPE_OFFSET] = 1;
		badBuffer[ITGTestConstants.LENGTH_OFFSET] = (byte) 400;
		
		testConstructor();
	}

	public void testConstructor() {
		try {
			startMessage = new ITGMessage(sender, goodStartBuffer);
		} catch (Exception e) {
			fail("Failed to create ITGMessage with goodStartBuffer");
		}
		
		try {
			endMessage = new ITGMessage(sender, goodEndBuffer);
		} catch (Exception e) {
			fail("Failed to create ITGMessage with goodEndBuffer");
		}
		
		try {
			new ITGMessage(sender, badBuffer);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
		
		try {
			new ITGMessage(sender, null);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof NullPointerException);
		}
		
		try {
			new ITGMessage(null, goodStartBuffer);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof NullPointerException);
		}
		
		try {
			new ITGMessage(null, null);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof NullPointerException);
		}
	}
	
	@Test
	public void testGetType() {
		assertEquals(startMessage.getType(), ITGMessage.Type.GEN_START);
		assertEquals(endMessage.getType(), ITGMessage.Type.GEN_END);
	}
	
	@Test
	public void testGetSender() {
		assertEquals(startMessage.getSender(), sender);
		assertEquals(endMessage.getSender(), sender);
	}
	
	@Test
	public void testGetMessage() {
		assertEquals(startMessage.getMessage(), message);
		assertEquals(endMessage.getMessage(), message);
	}
	
	@Test
	public void testToString() {
		assertEquals(startMessage.toString(), "[Start message from " +
			sender.getHostName() + "] " + startMessage.getMessage());
			
		assertEquals(endMessage.toString(), "[End message from " +
			sender.getHostName() + "] " + endMessage.getMessage());
	}
	
	private static void insertIntoBuffer(String message, int offset, byte[] buffer) {
		for (byte character : message.getBytes())
			buffer[offset++] = character;
	}
	
}
