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

import java.net.InetAddress;

/**
 * Represents a message received from an ITGSend instance
 */
public class ITGMessage {

	/**
	 * Types of message that may be received
	 */
	public enum Type {
		/** Traffic generation has started */
		GEN_START,
		
		/** Traffic generation has ended */
		GEN_END
	}

	/* Message type codes */
	private final static int
		GEN_START_CODE = 1,
		GEN_END_CODE = 2;
		
	/* Byte offsets for message buffer */
	private final static int
		MSG_TYPE_OFFSET	= 0,
		MSG_LENGTH_OFFSET = 4,
		MSG_OFFSET = 8;

	private InetAddress sender;
	private Type type;
	private String message;

	/** 
	 * Parses message contents from buffer
	 * 
	 * @throws IllegalArgumentException If any parameters are null
	 * @throws IndexOutOfBoundsException If message length is larger than buffer
	 */
	protected ITGMessage(InetAddress sender, byte[] buffer)
			throws IllegalArgumentException, IndexOutOfBoundsException {
			
		if (sender == null || buffer == null)
			throw new IllegalArgumentException("null argument(s)");
		
		switch (buffer[MSG_TYPE_OFFSET]) {
			case GEN_START_CODE:
				this.type = Type.GEN_START;
				break;
			
			case GEN_END_CODE:
				this.type = Type.GEN_END;
				break;
		}
			
		this.sender = sender;
		this.message = new String(buffer, MSG_OFFSET, buffer[MSG_LENGTH_OFFSET]);
	}
	
	/**
	 * Get the message type
	 * 
	 * @return The type of message
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Get the message sender
	 * 
	 * @return The sender of the message
	 */
	public InetAddress getSender() {
		return sender;
	}
	
	/**
	 * Get the message contents
	 * 
	 * @return The content of the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Compare ITGMessage objects
	 * 
	 * @param o The object to compare
	 * 
	 * @return true if the passed ITGMessage's type, sender and message are equal
	 * to this ITGMessage. Otherwise false.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ITGMessage) {
			ITGMessage comparison = (ITGMessage) o;
			
			return getType().equals(comparison.getType()) &&
				getSender().equals(comparison.getSender()) &&
				getMessage().equals(comparison.getMessage());
		}
		
		return false;
	}

	@Override
	public String toString() {
		String out = "[";

		switch (type) {
			case GEN_START:
				out += "Start";
				break;
		
			case GEN_END:
				out += "End";
				break;
		}

		out += " message from " + sender.getHostName() + "] " + message;
		return out;
	}

}
