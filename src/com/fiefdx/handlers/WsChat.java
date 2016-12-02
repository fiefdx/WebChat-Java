package com.fiefdx.handlers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.UUID;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.codec.binary.Base64;

import com.fiefdx.models.Room;
import com.fiefdx.utils.Tea;

/**
 * Servlet implementation class WsChat
 */
@ServerEndpoint(value = "/websocket/chat/{room-id}")
public class WsChat {
    private static final String GUEST_PREFIX = "Guest_";
    private static final int RoomNum = 100;
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static Room[] rooms = {};

    private final String nickname;
    private Session session;
    private Integer roomId;
    private UUID uuid = UUID.randomUUID();

    public WsChat() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
        if (rooms.length == 0) {
        	rooms = new Room[RoomNum + 1];
        	for (int i = 0; i <= RoomNum; i++) {
        		rooms[i] = new Room(i);
        	}
        }
    }


    @OnOpen
    public void start(@PathParam("room-id") Integer roomid, Session session) throws NoSuchAlgorithmException {
        this.session = session;
        this.roomId = roomid;
        rooms[roomId].add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        JSONObject obj = new JSONObject();
        obj.put("cmd", "init_rooms_list");
        obj.put("msg", new String(Base64.encodeBase64(message.getBytes())));
        JSONArray rooms_list = new JSONArray();
        for (int i = 1; i <= RoomNum; i++) {
        	Map<String, Object> m = new HashMap<String, Object>();
        	m.put("room_id", rooms[i].getID());
        	m.put("current_members", rooms[i].getCurrentMembers());
        	rooms_list.add(m);
        }
        obj.put("password", uuid.toString());
        obj.put("rooms_list", rooms_list);
        obj.put("default_nick_name", new String(Base64.encodeBase64(nickname.getBytes())));
        
        singlecast(obj);
    }


    @OnClose
    public void end() throws NoSuchAlgorithmException {
    	if (roomId == 0) {
    		rooms[roomId].remove(this);
    	} else {
    		rooms[0].remove(this);
    		rooms[roomId].remove(this);
    		JSONObject obj_out_join = new JSONObject();
    		Date date = new Date();
    		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		obj_out_join.put("cmd", "new_msg");
    		String message = String.format("System ( %s ) : %s has exited.", ft.format(date), nickname);
    		obj_out_join.put("msg", new String(Base64.encodeBase64(message.getBytes())));
    	    rooms[roomId].broadcast(obj_out_join);
    	    
    	    JSONObject obj_out_refresh = new JSONObject();
    	    obj_out_refresh.put("cmd", "refresh_rooms_list");
    	    JSONArray rooms_list = new JSONArray();
            for (int i = 1; i <= RoomNum; i++) {
            	Map<String, Object> m = new HashMap<String, Object>();
            	m.put("room_id", rooms[i].getID());
            	m.put("current_members", rooms[i].getCurrentMembers());
            	rooms_list.add(m);
            }
            obj_out_refresh.put("rooms_list", rooms_list);
            broadcastAll(obj_out_refresh);
    	}
    }


    @OnMessage
    public void incoming(String message) throws NoSuchAlgorithmException {
        // Never trust the client
    	JSONParser parser = new JSONParser();
    	try {
    		JSONObject obj = (JSONObject)parser.parse(message);
    		// System.out.println(obj);
    		String cmd = (String)obj.get("cmd");
    		JSONObject obj_out = new JSONObject();
    		JSONObject obj_out_refresh = new JSONObject();
    		String msg = "";
    		if (cmd.equals("change_room")) {
    			String room_id_s = (String)obj.get("room_id");
        		Integer room_id = new Integer(room_id_s);
        		// System.out.println(String.format("%s to %d", cmd, room_id));
        		
        		if (roomId != 0) {
        			rooms[roomId].remove(this);
        			JSONObject obj_out_join = new JSONObject();
                    Date date = new Date();
        			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        			obj_out_join.put("cmd", "new_msg");
        			msg = String.format("System ( %s ) : %s has exited.", ft.format(date), nickname);
        			obj_out_join.put("msg", new String(Base64.encodeBase64(msg.getBytes())));
                    rooms[roomId].broadcast(obj_out_join);
        			
            		roomId = room_id;
            		rooms[roomId].add(this);
                    obj_out_join = new JSONObject();
        			obj_out_join.put("cmd", "new_msg");
        			msg = String.format("System ( %s ) : %s has joined.", ft.format(date), nickname);
        			obj_out_join.put("msg", new String(Base64.encodeBase64(msg.getBytes())));
                    rooms[roomId].broadcast(obj_out_join);
        		} else {
        			roomId = room_id;
            		rooms[roomId].add(this);
            		JSONObject obj_out_join = new JSONObject();
                    Date date = new Date();
        			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        			obj_out_join.put("cmd", "new_msg");
        			msg = String.format("System ( %s ) : %s has joined.", ft.format(date), nickname);
        			obj_out_join.put("msg", new String(Base64.encodeBase64(msg.getBytes())));
                    rooms[roomId].broadcast(obj_out_join);
        		}
        		
        		obj_out.put("cmd", "change_rooms_list");
        		obj_out_refresh.put("cmd", "refresh_rooms_list");
                JSONArray rooms_list = new JSONArray();
                for (int i = 1; i <= RoomNum; i++) {
                	Map<String, Object> m = new HashMap<String, Object>();
                	m.put("room_id", rooms[i].getID());
                	m.put("current_members", rooms[i].getCurrentMembers());
                	rooms_list.add(m);
                }
                obj_out.put("rooms_list", rooms_list);
                obj_out_refresh.put("rooms_list", rooms_list);
                broadcastAll(obj_out_refresh);
                
                obj_out.put("room_id", roomId);
                obj_out.put("nick_name", new String(Base64.encodeBase64(nickname.getBytes())));
                singlecast(obj_out);
    		} else if (cmd.equals("send_msg")) {
    			String room_id_s = (String)obj.get("room_id");
        		Integer room_id = new Integer(room_id_s);
        		String nick_name_base64 = (String)obj.get("nick_name");
        		String default_name_base64 = (String)obj.get("default_nick_name");
        		String msg_base64 = (String)obj.get("msg");
        		
        		// System.out.println(String.format("mag_base64: %s", msg_base64));
        		String msg_string = decryptMsg(msg_base64);
        		// System.out.println(String.format("msg: %s", msg_string));
        		// System.out.println(String.format("%s to %d", cmd, room_id));
        		
        		if (roomId != 0) {
        			Date date = new Date();
        			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        			obj_out.put("cmd", "new_msg");
        			obj_out.put("date_time", ft.format(date));
        			obj_out.put("nick_name", nick_name_base64);
        			obj_out.put("default_nick_name", default_name_base64);
        			obj_out.put("encrypt", msg_string);
        			obj_out.put("msg", msg_string);
                    rooms[roomId].broadcast(obj_out);
        		}
    		}
    	} catch (ParseException e) {
    		System.out.println(String.format("parse error: %s", e));
    	}
    }


    @OnError
    public void onError(Throwable t) throws Throwable {
        
    }
    
    
    public Session getSession() {
    	return this.session;
    }
    
    
    public String getNickName() {
    	return this.nickname;
    }
    
    
    public String encryptMsg(String msg) throws NoSuchAlgorithmException {
    	String result = "";
    	String crypt_key = Tea.md5twice(uuid.toString());
    	// System.out.println(String.format("key: %s", crypt_key));
    	// byte[] msg_byte = Base64.encodeBase64(msg.getBytes());
		byte[] result_byte = Tea.strEncrypt(msg.getBytes(), Tea.hexToByteArray(crypt_key));
		result = new String(Base64.encodeBase64(result_byte));
    	return result;
    }
    
    
    public String decryptMsg(String msg) throws NoSuchAlgorithmException {
    	String result = "";
		String crypt_key = Tea.md5twice(uuid.toString());
		byte[] msg_byte = Base64.decodeBase64(msg);
		byte[] result_byte = Tea.strDecrypt(msg_byte, Tea.hexToByteArray(crypt_key));
		// result = new String(Base64.decodeBase64(result_byte));
		result = new String(result_byte);
		// System.out.println(String.format("r_mag: %s", result));
    	return result;
    }
    
    
    private void singlecast(String msg) {
    	try {
            synchronized (this) {
            	this.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
        	if (roomId == 0) {
        		rooms[roomId].remove(this);
        	} else {
        		rooms[0].remove(this);
        		rooms[roomId].remove(this);
        		String message = String.format("* %s %s",
                        this.nickname, "has been disconnected.");
                broadcast(message);
        	}
            try {
                this.session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }
    
    
    private void singlecast(JSONObject msg) throws NoSuchAlgorithmException {
    	try {
            synchronized (this) {
            	this.session.getBasicRemote().sendText(msg.toJSONString());
            }
        } catch (IOException e) {
        	if (roomId == 0) {
        		rooms[roomId].remove(this);
        	} else {
        		rooms[0].remove(this);
        		rooms[roomId].remove(this);
        		JSONObject obj_out_join = new JSONObject();
                Date date = new Date();
    			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			obj_out_join.put("cmd", "new_msg");
    			String message = String.format("System ( %s ) : %s has been disconnected.", ft.format(date), nickname);
    			obj_out_join.put("msg", new String(Base64.encodeBase64(message.getBytes())));
                rooms[roomId].broadcast(obj_out_join);
        	}
            try {
                this.session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }


    private static void broadcast(String msg) {
    	for (int i = 1; i <= RoomNum; i++) {
    		rooms[i].broadcast(msg);
    	}
    }
    
    private static void broadcast(JSONObject msg) throws NoSuchAlgorithmException {
    	for (int i = 1; i <= RoomNum; i++) {
    		rooms[i].broadcast(msg);
    	}
    }
    
    private static void broadcastAll(String msg) {
    	for (int i = 0; i <= RoomNum; i++) {
    		rooms[i].broadcast(msg);
    	}
    }
    
    private static void broadcastAll(JSONObject msg) throws NoSuchAlgorithmException {
    	for (int i = 0; i <= RoomNum; i++) {
    		rooms[i].broadcast(msg);
    	}
    }
}