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

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ITGApi {

	public final static int
		CONTROL_PORT = 8998,
		PACKET_LENGTH = 300; /* Same as official C++ API */

	private DatagramSocket socket;

	public ITGApi() throws IOException {
		socket = new DatagramSocket();
	}

	/**
	* Send an D-ITG command to a sender
	* 
	* @param senderHost		The IP address/hostname of the sender
	* @param command		The command the sender should run
	* 
	* @throws UnknownHostException If senderHost could not be resolved
	* @throws IOException If the command could not be sent
	*/
	public void sendCmd(String senderHost, String command)
			throws UnknownHostException, IOException {
		InetAddress sender = InetAddress.getByName(senderHost);
		
		DatagramPacket packet = new DatagramPacket(command.getBytes(),
			command.length(), sender, CONTROL_PORT);
		socket.send(packet);
	}

	/**
	* Receive a message from the sender. This method is blocking
	* 
	* @return An {@link ITGMessage} object representing the message
	* 
	* @throws IOException If a message could not be received
	*/
	public ITGMessage catchMsg() throws IOException {
		byte[] buffer = new byte[PACKET_LENGTH];
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		return new ITGMessage(packet.getAddress(), buffer);
	}
	
	/**
	 * Closes the associated socket. This object may no longer be used once
	 * this method is called.
	 */
	public void close() {
		socket.close();
	}

}
