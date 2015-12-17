package com.mvrt.frc2012;

import com.m3rcuriel.controve.api.Executable;
import com.m3rcuriel.controve.components.Motor;
import com.m3rcuriel.controve.components.Switch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

/**
 * Created by lee on 12/17/15.
 */
public class Logger implements Executable {
  public static final int WRITE_FREQUENCY = (int) (1000.0 / 30.0); // milliseconds per write
  public static final int RUNNING_TIME = 200; // number of seconds in ~ a match

  private final List<IntSupplier> suppliers = new ArrayList<>();
  private final List<String> names = new ArrayList<>();

  private volatile boolean running = false;

  private LogWriter logger;

  public void startup() {
    if (running) {
      return;
    }
    running = true;

    logger = new LogWriter(names, suppliers);
  }

  @Override
  public void execute(long time) {
    if (running) {
      logger.write(time);
    } else {
      logger.close();
    }
  }

  public void shutdown() {
    running = false;
  }

  public void register(String name, IntSupplier supplier) {
    if (supplier == null) {
      throw new IllegalArgumentException("The supplier may not be null");
    }
    if (running) {
      throw new UnsupportedOperationException("The logger is already running; unable to register");
    }
    names.add(name);
    suppliers.add(supplier);
  }

  public void registerSwitch(String name, Switch swtch) {
    if (swtch == null) {
      throw new IllegalArgumentException("The supplier may not be null");
    }
    if (running) {
      throw new UnsupportedOperationException("The logger is already running; unable to register");
    }
    names.add(name);
    suppliers.add(()->swtch.isTriggered() ? 1 : 0);
  }

  public void registerMotor(String name, Motor motor) {
    if (running)
      throw new UnsupportedOperationException("Cannot register while running");
    names.add(name + " speed");
    suppliers.add(() -> (short) (motor.getSpeed() * 1000));
  }

  public static short bitmask(boolean[] values) {
    if (values.length > 15)
      throw new IllegalArgumentException("Cannot combine more than 15 booleans");
    short value = 0;
    for (int i = 0; i < values.length; i++) {
      value = (short) (value | ((values[i] ? 1: 0) << i));
    }
    return value;
  }

  public static class LogWriter {
    private final List<IntSupplier> suppliers;

    private long recordLength;
    private MappedWriter writer;

    public LogWriter(List<String> names, List<IntSupplier> suppliers) {
      this.suppliers = suppliers;
      try {
        int numWrites = (int) ((1.0 / WRITE_FREQUENCY) * (RUNNING_TIME * 1000));

        recordLength = Integer.BYTES;
        recordLength += (Short.BYTES * suppliers.size());

        long minSize = numWrites * suppliers.size();

        writer = new MappedWriter("robot.log", minSize + 1024);

        writer.write("log".getBytes()); // write header
        writer.write((byte) (suppliers.size() + 1)); // write number of elements
        writer.write((byte) Integer.BYTES); // write the size of each element

        for (IntSupplier supplier : suppliers) {
          assert supplier != null;
          writer.write((byte) Short.BYTES);
        }

        // write the length of each element name and the name
        writer.write((byte) 4);
        writer.write(("Time").getBytes());

        for (String name : names) {
          writer.write((byte) name.length());
          writer.write(name.getBytes());
        }
      } catch (IOException e) {
        System.err.println("Failed to load logfile");
        e.printStackTrace();
      }
    }

    public void write(long time) {
      if (writer.getRemaining() < recordLength) {
        System.err.println("Not enough space to write all of next record, closing file.");
        writer.close();
      }

      writer.writeInt((int) time);
      suppliers.forEach((supplier -> writer.writeShort((short) supplier.getAsInt())));
    }

    public void close() {
      writer.close();
    }
  }
}
