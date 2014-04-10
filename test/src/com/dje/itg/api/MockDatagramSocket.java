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

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

public class MockDatagramSocket extends DatagramSocket {
	
	private DatagramPacket lastSentPacket, packetForRetrieval;
	private boolean closed = false;

	public MockDatagramSocket() throws SocketException {}
	
	@Override
	public void close() {
		closed = true;
	}
	
	/*
	 * Stores the passed packet for later retrieval
	 */
	@Override
	public void send(DatagramPacket packet) throws IOException {
		if (closed)
			throw new IOException();
		
		lastSentPacket = packet;
	}
	
	/*
	 * Retrieve the last packet from send(packet)
	 */
	public DatagramPacket getLastSentPacket() {
		return lastSentPacket;
	}
	
	/* 
	 * Copies the previously provided packet's data to the passed packet
	 */
	@Override
	public void receive(DatagramPacket packet) throws IOException {
		if (closed)
			throw new IOException();
		
		System.arraycopy(packetForRetrieval.getData(), 0,
			packet.getData(), 0, packetForRetrieval.getData().length);
		packet.setLength(packetForRetrieval.getLength());
		packet.setAddress(packetForRetrieval.getAddress());
	}
	
	/*
	 * Set the packet that will be retrieved later via receive(packet)
	 */
	public void setPacketForRetrieval(DatagramPacket packet) {
		packetForRetrieval = packet;
	}

}
