/*
*	This file is part of JITGApi.
* 
*	Copyright 2014 Duncan Eastoe <duncaneastoe@gmail.com>
*
*   JITGApi is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   JITGApi is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with JITGApi.  If not, see <http://www.gnu.org/licenses/>.
*/

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

	@Before
	public void setUp() {
		try {
			sender = InetAddress.getLocalHost();
		} catch (Exception e) {
			fail("Failed to get localhost InetAddress");
		}
		
		goodStartBuffer = ITGTestUtils.createBuffer(ITGTestUtils.START_TYPE,
			ITGTestUtils.commandOne.length(), ITGTestUtils.commandOne);
			
		goodEndBuffer = ITGTestUtils.createBuffer(ITGTestUtils.END_TYPE,
			ITGTestUtils.commandOne.length(), ITGTestUtils.commandOne);
			
		badBuffer = ITGTestUtils.createBuffer(ITGTestUtils.START_TYPE,
			400, ITGTestUtils.commandOne);
		
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
		
		/*
		 * Test passing bad parameters
		 */
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
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			new ITGMessage(null, goodStartBuffer);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			new ITGMessage(null, null);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
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
		assertEquals(startMessage.getMessage(), ITGTestUtils.commandOne);
		assertEquals(endMessage.getMessage(), ITGTestUtils.commandOne);
	}
	
	@Test
	public void testEquals() {
		assertFalse(startMessage.equals(null));
		assertFalse(startMessage.equals(sender));
		
		assertFalse(startMessage.equals(endMessage));
		assertFalse(endMessage.equals(startMessage));
		
		assertTrue(startMessage.equals(startMessage));
		assertTrue(endMessage.equals(endMessage));
		
		assertTrue(startMessage.equals(new ITGMessage(sender, goodStartBuffer)));
	}
	
	/*
	 * Test output of toString() by comparing to expected output
	 */
	@Test
	public void testToString() {
		assertEquals(startMessage.toString(), "[Start message from " +
			sender.getHostName() + "] " + startMessage.getMessage());
			
		assertEquals(endMessage.toString(), "[End message from " +
			sender.getHostName() + "] " + endMessage.getMessage());
	}
	
}
