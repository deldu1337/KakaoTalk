// ChatMsg.java ä�� �޽��� ObjectStream ��.
public class ChatMsg {
    public String code; // 100:�α���, 150:���̵� ����, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event
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