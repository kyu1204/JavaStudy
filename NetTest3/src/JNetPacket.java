import java.nio.ByteBuffer;

public class JNetPacket {
	// ================
	// ������.
	// ================
	// ������ ������
	private final static int INTEGER = 899;
	// Ÿ�Ե�
	public static final short DEFAULT = 999;
	public static final short LOGIN = 10;
	public static final short LOGININFOR = 11;
	public static final short DISCONNECT = 12;
	public static final short RESEND = 13;
	public static final short CHAT = 20;
	public static final short WHISPER = 21;
	public static final short FIGHT = 30;
	public static final short TESTPACKET = 41;
	public static final short GROUPWAIT = 50;
	public static final short GROUPSEND = 51;

	private int Size = 1000;
	private static ByteBuffer Buf;
	// ������ ó�� �޽���
	public static String RecvMsg = "";
	// ��Ŷ ó��
	public static boolean RecvSucess = true;

	// ����� ByteBuffer ������� �ʱ�ȭ
	public JNetPacket() {
		Buf = ByteBuffer.allocate(Size);
		Buf.clear();
	}

	// ������ ���� �����ϱ�
	public JNetPacket(int CSize) {
		Size = CSize;
		Buf = ByteBuffer.allocate(Size);
		Buf.clear();
	}

	// ������
	public int Size() {
		return Size;
	}

	// �������� Ÿ�� ����
	public void Type(short Type) {
		// short ������/ byte����� ���� ���۸� ����Ѱͺ��� �����ÿ��� ������ ���ְ� Error
		if (Buf.remaining() > Short.SIZE / Byte.SIZE) {
			Buf.putShort(Type);
		} else {
			OverPacket();
		}
	}

	// ������ ����
	public void Add(String Data) {
		int Size = Data.getBytes().length;
		if (Buf.remaining() > (Size + Integer.SIZE / Byte.SIZE)) {
			Buf.putInt(Size);
			Buf.put(Data.getBytes());
		} else {
			OverPacket();
		}
	}

	public void Add(int _Data) {
		int IntegerSize = INTEGER;
		if (Buf.remaining() > (Integer.SIZE * 2) / Byte.SIZE) {
			Buf.putInt(IntegerSize);
			Buf.putInt(_Data);
		} else {
			OverPacket();
		}
	}

	public ByteBuffer getBuf() {
		// ������ �ʱ�ȭ
		Buf.rewind();
		return Buf;
	}

	// ���ۻ������ Ŭ��� �ʱ�ȭ�� �̺�Ʈó��
	public boolean OverPacket() {
		Buf.clear();
		return false;
	}

	public void Clear() {
		Buf.flip();
		Buf.clear();
	}

	// ������ ��ȯ ����->���ڿ�
	public static String RecvOut(ByteBuffer RecvBuf) {
		RecvMsg = "";
		RecvSucess = true;
		int trySize = RecvBuf.getInt();
		switch (trySize) {
		case INTEGER:
			RecvMsg += RecvBuf.getInt();
			break;
		default:
			byte[] StrBuf = new byte[trySize];
			RecvBuf.get(StrBuf);
			RecvMsg += new String(StrBuf);
			break;
		}
		return RecvMsg;
	}
	// �߰������� ��ȣȭ �߰��� ����...
}