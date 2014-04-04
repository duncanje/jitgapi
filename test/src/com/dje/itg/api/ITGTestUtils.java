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

public class ITGTestUtils {

	public final static int
		BUFFER_LENGTH = 300,
		TYPE_OFFSET = 0,
		LENGTH_OFFSET = 4,
		MESSAGE_OFFSET = 8,
		CONTROL_PORT = 8998,
		START_TYPE = 1,
		END_TYPE = 2;

	public final static String commandOne = "-a localhost -rp 10000 VoIP";

	public static byte[] createBuffer(int type, int lengthOffset, String command) {
		byte[] buffer = new byte[BUFFER_LENGTH];
		buffer[TYPE_OFFSET] = (byte) type;
		buffer[LENGTH_OFFSET] = (byte) lengthOffset;
		insertCommandIntoBuffer(command, MESSAGE_OFFSET, buffer);
			
		return buffer;
	}

	public static void insertCommandIntoBuffer(String command, int offset, byte[] buffer) {
		for (byte character : command.getBytes())
			buffer[offset++] = character;
	}
		
}
