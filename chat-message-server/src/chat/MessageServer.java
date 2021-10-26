package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MessageServer implements Runnable {
    
    private AsynchronousServerSocketChannel server;
    //private ServerSocket serverSocket;
    private ThreadPoolExecutor pool;
    private int port = 8090;

    
    public void initServer() {
        try {
            
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("localhost", port));
            //serverSocket = new ServerSocket(port);
        } catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.setKeepAliveTime( 60, TimeUnit.SECONDS );
        
//        assertEquals(2, executor.getPoolSize());
//        assertEquals(1, executor.getQueue().size());
    }
    
    public void start() {
        Thread t = new Thread( this );
        t.start();
    }

    @Override
    public void run() {
        Future<AsynchronousSocketChannel> future = server.accept();
        //AsynchronousSocketChannel worker = future.get();
        //Socket socket = serverSocket.accept();
        
        //pool.submit(() ->  new ConnectionHandler( worker ));
        
        Future<String> result = pool.submit(new ConnectionHandler( future ));
    }
}
