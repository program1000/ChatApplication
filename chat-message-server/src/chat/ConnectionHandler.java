package chat;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ConnectionHandler implements Callable<String>{
    
    //private Socket socket;
    private AsynchronousSocketChannel channel;
    private Future<AsynchronousSocketChannel> future;
    
//    public ConnectionHandler(Socket newSocket) {
//        socket = newSocket;
//    }
    
    public ConnectionHandler( AsynchronousSocketChannel newChannel) {
        channel =newChannel;
    }
    
    public ConnectionHandler( Future<AsynchronousSocketChannel> future ) {
        this.future = future;
    }

    public String old() throws Exception {
        //System.out.println("New Connection: "+ socket.getLocalPort()+ " on "+socket.getPort());
        System.out.println("New Connection: "+ channel.getLocalAddress() );
        
        ByteBuffer buffer = ByteBuffer.allocate(32);
        Future<Integer> readResult  = channel.read(buffer);
        
        // perform other computations
        
        readResult.get();
        
        buffer.flip();
        Future<Integer> writeResult = channel.write(buffer);

        // perform other computations

        writeResult.get();
        buffer.clear();
        
        return null;
    }

    @Override
    public String call() throws Exception {
        channel = future.get();
        System.out.println("New Connection: "+ channel.getLocalAddress() );
        
        ByteBuffer buffer = ByteBuffer.allocate(32);
        Future<Integer> readResult  = channel.read(buffer);
        int intLength = readResult.get();
        System.out.println("S1: "+intLength);
        buffer.flip();
        String s = new String(buffer.array()).trim();
        int r = Integer.valueOf(s);

        Future<Integer> writeResult = channel.write(buffer);
        intLength = writeResult.get();
        System.out.println("S2: "+intLength);
        buffer.clear();
        
        ByteBuffer buffer2 = ByteBuffer.allocate(r);
        readResult  = channel.read(buffer2);
        intLength = readResult.get();
        System.out.println("S3: "+intLength);

        buffer2.clear();
        String result = new String(buffer2.array());
        System.out.println("S4: "+result);
        //return new String(buffer2.array());
        return result;
    }

}
