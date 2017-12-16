package MsgPacker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessagePacker {

	private int bufferSize = 1024; // 버퍼의 초기사이즈(변경가능)
	private static ByteBuffer buffer; // 한번만 생성해서 사용하고자 함.
	private int offset = 0;
	
	public MessagePacker() {
		// TODO Auto-generated constructor stub
		buffer = ByteBuffer.allocate(bufferSize);
		buffer.clear();
	}

	public MessagePacker(int size) {
		buffer = ByteBuffer.allocate(size);
		buffer.clear();
	}

	public MessagePacker(byte[] data){
		buffer = ByteBuffer.allocate(data.length);
		buffer.clear();
		buffer = ByteBuffer.wrap(data); // Byte Array를 ByteBuffer로  Wrapping
	}
	
	public byte[] Finish(){
		
		offset = buffer.position(); // 마지막 포인터 위치 기억
		byte[] data = {};
		
		if(buffer.hasArray()){ // Array가 존재하는 경우에만
			data = buffer.array();
		}
		
		byte[] result = new byte[offset];
		System.arraycopy(data, 0, result, 0, offset); // offset만큼 복사한다
		
		buffer.flip();
		return result;
	}
	
	public void SetProtocol(byte protocol){
		buffer.put(protocol);
	}
	
	public void SetEndianType(ByteOrder option){
		if(option == ByteOrder.BIG_ENDIAN){
			buffer.order(ByteOrder.BIG_ENDIAN);
		}
		else{
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
	}
	
	public void add(int param){		
		if(buffer.remaining() > Integer.SIZE / Byte.SIZE) // 남은 공간이 있을 경우
			buffer.putInt(param);
	}

	public void add(float param){
		if(buffer.remaining() > Float.SIZE / Byte.SIZE) // 남은 공간이 있을 경우
			buffer.putFloat(param);
	}
	
	public void add(double param){
		if(buffer.remaining() > Double.SIZE / Byte.SIZE) // 남은 공간이 있을 경우
			buffer.putDouble(param);
	}
	
	public void add(String param){
		int len = param.getBytes().length;
		if(buffer.remaining() > len){ // 남은 공간이 있을 경우
			buffer.putInt(len);		
			buffer.put(param.getBytes());	
		}
	}
	
	public ByteBuffer getBuffer(){
		return buffer;
	}
	
	public byte getProtocol(){
		return buffer.get();
	}
	
	public int getInt(){
		return buffer.getInt();
	}
	
	public float getFloat(){
		return buffer.getFloat();
	}
	
	public double getDouble(){
		return buffer.getDouble();
	}
	
	public String getString(){
		int len = buffer.getInt();
		byte[] temp = new byte[len];
		
		buffer.get(temp);
		String result = new String(temp);
		return result;
	}
	
}