/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author admin
 */
public class TcpServer {

    private final int serverPort;
    private final List<ClientHandler> clients = new ArrayList<>();

    public TcpServer(int serverPort) {
        this.serverPort = serverPort;
    }

    // поток регистрации клиентов
    public void listenForClients() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                System.out.println("TCP Server listening on port " + serverPort);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    ClientHandler handler = new ClientHandler(clientSocket);
                    clients.add(handler);

                    // запускаем поток клиента
                    new Thread(handler).start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // распределённое вычисление
    public double calculateDistributed(double a, double b, double h) throws Exception {

        int K = clients.size();
        if (K == 0) {
            throw new Exception("Нет подключённых клиентов!");
        }

        double interval = (b - a) / K;

        // отправляем задачи
        for (int i = 0; i < K; i++) {
            ClientHandler c = clients.get(i);

            double start = a + i * interval;
            double end = (i == K - 1) ? b : start + interval;

            String msg = "TASK;a=" + start + ";b=" + end + ";h=" + h;
            c.send(msg);

            System.out.println("Sent to client → " + msg);
        }

        // принимаем результаты
        double total = 0;

        for (int i = 0; i < K; i++) {
            ClientHandler c = clients.get(i);
            String resp = c.waitForResponse();

            System.out.println("Received: " + resp);

            if (resp.startsWith("RESULT;value=")) {
                double value = Double.parseDouble(resp.substring("RESULT;value=".length()));
                total += value;
            } else if (resp.startsWith("RESULT;error=")) {
                throw new Exception("Ошибка клиента: " + resp.substring("RESULT;error=".length()));
            }
        }

        return total;
    }

    // обработчик клиента
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;

        private String lastResponse = null;

        public ClientHandler(Socket socket) throws Exception {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        public void send(String msg) {
            out.println(msg);
        }

        public String waitForResponse() throws Exception {
            // блокирующее ожидание строки
            lastResponse = in.readLine();
            return lastResponse;
        }

        @Override
        public void run() {
            try {
                // ждём HELLO
                String msg = in.readLine();
                if ("HELLO".equals(msg)) {
                    System.out.println("Client registered: " + socket.getInetAddress());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
