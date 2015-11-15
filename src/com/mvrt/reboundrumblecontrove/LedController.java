package com.mvrt.reboundrumblecontrove;

import com.mvrt.lib.ShortColor;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LedController implements Runnable {

  private DatagramSocket clientSocket;
  private InetAddress ipaddr;
  private int port;
  private Thread running;

  private Robot.RobotState previousState;

  public LedController(String ip, int port) {
    this.port = port;
    try {
      clientSocket = new DatagramSocket(5803);
      ipaddr = InetAddress.getByName(ip);
    } catch (UnknownHostException e) {
      System.err.println("Could not get LED IP Address");
      e.printStackTrace();
    } catch (SocketException e1) {
      System.err.println("Could not bind socket");
      e1.printStackTrace();
    }
  }

  @Override
  public void run() {
    if (!clientSocket.isConnected()) {
      clientSocket.connect(ipaddr, port);
    }
    if (previousState != Robot.getState()) {
      running.stop();
      switch (Robot.getState()) {
        case AUTONOMOUS:
          running = new Thread(this::autonomous);
          break;
        case TELEOP:
          running = new Thread(this::teleop);
          break;
        case DISABLED:
          running = new Thread(this::disabled);
          break;
        default:
          running = new Thread(this::disabled);

      }
      running.start();
    }

    previousState = Robot.getState();
  }

  public void sendLedPacket(short r, short g, short b, short a) throws PortUnreachableException {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.order(ByteOrder.BIG_ENDIAN);
      buffer.putShort(r);
      buffer.putShort(g);
      buffer.putShort(b);
      buffer.putShort(a);
      byte[] sendData = buffer.array();
      clientSocket.send(new DatagramPacket(sendData, sendData.length));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendLedPacket(ShortColor color) {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.order(ByteOrder.BIG_ENDIAN);
      buffer.putShort(color.getRed());
      buffer.putShort(color.getGreen());
      buffer.putShort(color.getBlue());
      buffer.putShort(color.getAlpha());
      byte[] sendData = buffer.array();
      clientSocket.send(new DatagramPacket(sendData, sendData.length));
    } catch (PortUnreachableException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void disabled() {
    sendLedPacket(ShortColor.fromColor(Color.GREEN));
  }

  public void teleop() {
    sendLedPacket(ShortColor.fromColor(Color.RED));
  }

  public void autonomous() {
    sendLedPacket(ShortColor.fromColor(Color.BLUE));
  }
}
