package com.fiefdx.models;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.json.simple.JSONObject;
import org.apache.commons.codec.binary.Base64;

import com.fiefdx.handlers.WsChat;

public class Room {
	private final Set<WsChat> connections = new CopyOnWriteArraySet<>();
	private int RoomID, CurrentMembers = 0;
	
	public Room(int id) {
		this.RoomID = id;
	}
	
	public int getID() {
		return this.RoomID;
	}
	
	public int getCurrentMembers() {
		return this.CurrentMembers;
	}
	
	public Set<WsChat> getConnections() {
		return this.connections;
	}
	
	public void add(WsChat conn) {
		connections.add(conn);
		CurrentMembers++;
	}
	
	public void remove(WsChat conn) {
		connections.remove(conn);
		CurrentMembers--;
	}
	
	public void broadcast(String msg) {
        for (WsChat client : connections) {
            try {
                synchronized (client) {
                    client.getSession().getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.getSession().close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.getNickName(), "has been disconnected.");
                broadcast(message);
            }
        }
    }
	
	public void broadcast(JSONObject msg) throws NoSuchAlgorithmException {
		String encrypt = "";
		if (msg.containsKey("encrypt")) {
			encrypt = (String)msg.get("encrypt");
			msg.put("encrypt", "");
    	} 
        for (WsChat client : connections) {
            try {
                synchronized (client) {
                	if (msg.containsKey("encrypt")) {
                		msg.put("msg", client.encryptMsg(encrypt));
                	} 
                	client.getSession().getBasicRemote().sendText(msg.toJSONString());
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.getSession().close();
                } catch (IOException e1) {
                    // Ignore
                }
                JSONObject obj_out_join = new JSONObject();
                Date date = new Date();
    			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			obj_out_join.put("cmd", "new_msg");
    			String message = String.format("System ( %s ) : %s has been disconnected.", 
						                       ft.format(date), 
						                       client.getNickName());
    			obj_out_join.put("msg", new String(Base64.encodeBase64(message.getBytes())));
                broadcast(obj_out_join);
            }
        }
    }
}
