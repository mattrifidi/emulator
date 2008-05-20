package sandbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import edu.uark.csce.llrp.AccessCommand;
import edu.uark.csce.llrp.AccessReportSpec;
import edu.uark.csce.llrp.AccessSpec;
import edu.uark.csce.llrp.AccessSpecStopTrigger;
import edu.uark.csce.llrp.AddAccessSpec;
import edu.uark.csce.llrp.C1G2Read;
import edu.uark.csce.llrp.C1G2TagSpec;
import edu.uark.csce.llrp.C1G2TargetTag;
import edu.uark.csce.llrp.Message;

public class Sandbox implements Observer {

	boolean done = false;
	int second = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sandbox s = new Sandbox();
		s.testMessage();
	}

	private void timerTest() {
		TestTimer t = new TestTimer(10000, "testTimer");
		t.addObserver(this);
		Thread thread = new Thread(t, "timer thread");
		thread.start();

		while (!done) {
			System.out.println("tick " + second++);
			try {
				Thread.sleep(999);
				if (second == 1)
					t.suspend();
				if (second == 2)
					t.resume();
				if (second == 8)
					t.suspend();
				if (second == 12)
					t.resume();
				if (second == 13)
					t.suspend();
				if (second == 20)
					t.resume();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void update(Observable arg0, Object arg1) {
		System.out.println("timer up");
		done = true;

	}

	public void testMessage() {
		byte[] message = { (byte) 0x04, (byte) 0x28, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0xCF,
				(byte) 0x00, (byte) 0x56, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x01,
				(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xD0,
				(byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0xD1, (byte) 0x00,
				(byte) 0x3A, (byte) 0x01, (byte) 0x52, (byte) 0x00,
				(byte) 0x27, (byte) 0x01, (byte) 0x53, (byte) 0x00,
				(byte) 0x23, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60,
				(byte) 0x17, (byte) 0x4A, (byte) 0x03, (byte) 0x3F,
				(byte) 0x15, (byte) 0x6F, (byte) 0x53, (byte) 0x1A,
				(byte) 0x61, (byte) 0x31, (byte) 0x2C, (byte) 0x57,
				(byte) 0x01, (byte) 0x55, (byte) 0x00, (byte) 0x0F,
				(byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00,
				(byte) 0xEF, (byte) 0x00, (byte) 0x05, (byte) 0x01 };

		ByteArrayInputStream is = new ByteArrayInputStream(message);
		try {
			Message m = Message.receive(is);
			System.out.println(m.toXMLString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
