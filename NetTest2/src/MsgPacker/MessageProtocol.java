package MsgPacker;

public class MessageProtocol{

	// ������ݵ��� ����

	public static final byte LOGIN = 0;
	public static final byte CREATE = 1;
	public static final byte JOIN = 2;
	public static final byte CLOSE = 3;
	
	public static final byte READY = 10;
	public static final byte READYCANCEL = 11;
	public static final byte READYLEAVE = 12;
	
	public static final byte BATTLE_START = 20;
	public static final byte BATTLE = 21;
	public static final byte BATTLE_END = 22;
}