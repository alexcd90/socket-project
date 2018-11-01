import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
//        设置超时时间
        socket.setSoTimeout(3000);
//        连接本地，端口2000，超时3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);

        System.out.println("已发起服务器连接，进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + " P: " + socket.getLocalPort());
        System.out.println("服务端信息：" + socket.getInetAddress() + " P: " + socket.getPort());

        try {
//            发送数据
            todo(socket);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常关闭");
        }
//        释放资源
        socket.close();
        System.out.println("客户端退出~");
    }

    private static void todo(Socket client) throws IOException {
//        构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

//        得到Socket输出流，并转化为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

//        得到服务器输入流
        InputStream inputStream = client.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean flag = true;
        do {
//        键盘读取一行
            String str = input.readLine();
//        发送到服务器
            printStream.println(str);

            String echo = bufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }

        } while (flag);

//        释放资源
//        input.close();
        printStream.close();
        bufferedReader.close();


    }
}
