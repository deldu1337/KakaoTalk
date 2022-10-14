// ChatMsg.java 채팅 메시지 ObjectStream 용.
public class ChatMsg {
    public String code; // 100:로그인, 150:아이디 저장, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
    public String UserName;
    public String data;
    public String id;
    public String room_id;
    public byte[] imgbytes;

    public ChatMsg(String UserName, String code, String msg, String id, String room_id) {
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
        this.id = id;
        this.room_id = room_id;
    }
}