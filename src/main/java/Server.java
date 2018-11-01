import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息："+server.getInetAddress() +" P: "+server.getLocalPort());

//        等待客户端连接
        for (;;){
//            得到客户端
            Socket client = server.accept();
//            客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }
    }

    /**
     * 客户端处理消息
     */
    private static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接："+socket.getInetAddress() +
                    " P: "+socket.getPort());

            try {
//                得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutPut = new PrintStream(socket.getOutputStream());
//                得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)){
                        flag=false;
                        socketOutPut.println("bye");
                    }else{
                        System.out.println(str);
                        socketOutPut.println("回送: "+str.length());
                    }
                }while (flag);

                socketInput.close();
                socketOutPut.close();

            } catch (IOException e) {
                System.out.println("连接异常断开");
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("客户端已退出："+socket.getInetAddress()+
                    " P: " +socket.getPort());
        }
    }

}
