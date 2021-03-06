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

package uk.org.eastoe.duncan.itg.api;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Implementation of API operations
 */
public class ITGApi {

	public final static int CONTROL_PORT = 8998;
	
	private final static int PACKET_LENGTH = 300; /* Same as official C++ API */

	private DatagramSocket socket;

	/**
	 * Construct API object
	 * 
	 * @throws IOException If socket could not be created
	 */
	public ITGApi() throws IOException {
		this(new DatagramSocket());
	}
	
	/*
	 * Only called externally for unit tests
	 */
	protected ITGApi(DatagramSocket socket) {
		if (socket == null)
			throw new IllegalArgumentException("null argument");
		this.socket = socket;
	}

	/**
	* Send an D-ITG command to an ITGSend process
	* 
	* @param senderHost	The InetAddress representing the ITGSend host
	* @throws IllegalArgumentException If any parameters are null
	* @param command The command the sender should run
	* 
	* @throws IOException If the command could not be sent
	*/
	public void sendCmd(InetAddress senderHost, String command)
			throws IOException, IllegalArgumentException {
		if (senderHost == null || command == null)
			throw new IllegalArgumentException("null arguments");
		
		DatagramPacket packet = new DatagramPacket(command.getBytes(),
			command.length(), senderHost, CONTROL_PORT);
		socket.send(packet);
	}

	/**
	* Send an D-ITG command to an ITGSend process
	* 
	* @param senderHost		The IP address/hostname of the ITGSend host
	* @param command		The command the sender should run
	* 
	* @throws UnknownHostException If senderHost could not be resolved
	* @throws IllegalArgumentException If any parameters are null
	* @throws IOException If the command could not be sent
	*/
	public void sendCmd(String senderHost, String command)
			throws UnknownHostException, IllegalArgumentException, IOException {
		if (senderHost == null)
			throw new IllegalArgumentException("null arguments");
		
		InetAddress sender = InetAddress.getByName(senderHost);
		sendCmd(sender, command);
	}

	/**
	* Receive a message from the sender. This method is blocking
	* 
	* @return An {@link ITGMessage} object representing the message
	* 
	* @throws IOException If a message could not be received
	* @throws IllegalArgumentException If a message wasn't received correctly
	* @throws IndexOutOfBoundsException If a message was malformed
	*/
	public ITGMessage catchMsg()
			throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
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
