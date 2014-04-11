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

import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.util.Arrays;

public class ITGApiTest {

	private ITGApi mockApi;
	private MockDatagramSocket mockSocket;
	private InetAddress sender;

	@Before
	public void setUp() {
		try {
			sender = InetAddress.getLocalHost();
		} catch (Exception e) {
			fail("Failed to get localhost InetAddress");
		}
		
		try {
			mockSocket = new MockDatagramSocket();
		} catch (Exception e) {
			fail("Failed to create MockDatagramSocket");
		}
		
		testConstructor();
	}

	/*
	 * Test the ITGApi constructors
	 */
	public void testConstructor() {
		try {
			new ITGApi();
		} catch (Exception e) {
			fail("Failed to create ITGApi");
		}
		
		try {
			mockApi = new ITGApi(mockSocket);
		} catch (Exception e) {
			fail("Failed to create ITGApi with mock socket");
		}
		
		try {
			new ITGApi(null);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	/*
	 * Calls various tests on the sendCmd methods
	 */
	@Test
	public void testSendCmds() {
		String nullString = null;
		InetAddress nullInetAddress = null;
		
		testSendCmdBadParams(nullString, ITGTestUtils.commandOne);
		testSendCmdBadParams(nullInetAddress, ITGTestUtils.commandOne);
		testSendCmdBadParams(nullInetAddress, nullString);
		testSendCmdBadParams(sender, nullString);
		testSendCmdBadParams("localhost", nullString);
		
		DatagramPacket packet = new DatagramPacket(ITGTestUtils.commandOne.getBytes(),
			ITGTestUtils.commandOne.length(), sender, ITGApi.CONTROL_PORT);
		
		testSendCmd(sender, ITGTestUtils.commandOne, packet);
		testSendCmd(sender.getHostName(), ITGTestUtils.commandOne, packet);
		testSendCmd(sender.getHostAddress(), ITGTestUtils.commandOne, packet);
	}
	
	/*
	 * Tests sendCmd in cases of bad parameters
	 */
	private void testSendCmdBadParams(Object sender, String command) {
		try {
			runSendCmd(sender, command);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	/*
	 * Tests the function of the sendCmd method
	 */
	private void testSendCmd(Object sender, String command, DatagramPacket packet) {		
		try {
			runSendCmd(sender, command);
		} catch (Exception e) {
			fail("Unexpected exception" + e);
		}
		
		comparePackets(mockSocket.getLastSentPacket(), packet);
	}
	
	/*
	 * Run appropriate version of sendCmd method
	 */
	private void runSendCmd(Object senderArg, String command) throws Exception {
		if (senderArg instanceof String)
			mockApi.sendCmd((String) senderArg, command);
		else
			mockApi.sendCmd((InetAddress) senderArg, command);
	}
	
	/*
	 * Compare DatagramPackets for data, length, port and address
	 */
	private void comparePackets(DatagramPacket one, DatagramPacket two) {
		assertTrue(Arrays.equals(one.getData(), two.getData()));
		assertEquals(one.getLength(), two.getLength());
		assertEquals(one.getPort(), two.getPort());
		assertEquals(one.getAddress(), two.getAddress());
	}
	
	/*
	 * Test the catchMsg method
	 */
	@Test
	public void testCatchMsg() {
		/* Create a data buffer */
		byte[] buffer = ITGTestUtils.createBuffer(1,
			ITGTestUtils.commandOne.length(), ITGTestUtils.commandOne);
		
		/* Create packet and provide to socket */
		DatagramPacket packet = new DatagramPacket(buffer,
			buffer.length);
		packet.setAddress(sender);
		mockSocket.setPacketForRetrieval(packet);
		
		ITGMessage itgMessage = null;
		try {
			itgMessage = mockApi.catchMsg();
		} catch (Exception e) {
			fail("Unexpected exception" + e);
		}
		
		/* Test ITGMessage values are expected according to packet */
		assertEquals(itgMessage.getType(), ITGMessage.Type.GEN_START);
		assertEquals(itgMessage.getSender(), sender);
		assertEquals(itgMessage.getMessage(), ITGTestUtils.commandOne);
	}
	
	/*
	 * Test the close method
	 */
	@Test
	public void testClose() {
		mockApi.close();
		
		try {
			mockApi.catchMsg();
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IOException);
		}
		
		try {
			mockApi.sendCmd("localhost", ITGTestUtils.commandOne);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IOException);
		}
	}
	
}
