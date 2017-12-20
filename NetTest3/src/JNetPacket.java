import java.nio.ByteBuffer;

public class JNetPacket {
	// ================
	// 변수들.
	// ================
	// 정수형 데이터
	private final static int INTEGER = 899;
	// 타입들
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
	// 받을때 처리 메시지
	public static String RecvMsg = "";
	// 패킷 처리
	public static boolean RecvSucess = true;

	// 선언시 ByteBuffer 사이즈와 초기화
	public JNetPacket() {
		Buf = ByteBuffer.allocate(Size);
		Buf.clear();
	}

	// 사이즈 직접 조정하기
	public JNetPacket(int CSize) {
		Size = CSize;
		Buf = ByteBuffer.allocate(Size);
		Buf.clear();
	}

	// 사이즈
	public int Size() {
		return Size;
	}

	// 데이터의 타입 설정
	public void Type(short Type) {
		// short 사이즈/ byte사이즈가 현재 버퍼를 사용한것보다 작을시에는 데이터 못넣게 Error
		if (Buf.remaining() > Short.SIZE / Byte.SIZE) {
			Buf.putShort(Type);
		} else {
			OverPacket();
		}
	}

	// 데이터 삽입
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
		// 포지션 초기화
		Buf.rewind();
		return Buf;
	}

	// 버퍼사이즈보다 클경우 초기화후 이벤트처리
	public boolean OverPacket() {
		Buf.clear();
		return false;
	}

	public void Clear() {
		Buf.flip();
		Buf.clear();
	}

	// 데이터 변환 버퍼->문자열
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
	// 추가적으로 암호화 추가할 예정...
}