import java.util.concurrent.Semaphore;

//import java.util.Random;

// Readers-Writers with Writer Priority

public class RW_Semaphore { 
	
	public static void main(String[] args) {

		Database d = new Database();

		Writer w1 = new Writer(d,1);
		Writer w2 = new Writer(d,10);
		Writer w3 = new Writer(d,100);
		Writer w4 = new Writer(d,1000);
		Writer w5 = new Writer(d,10000);

		Reader r1 = new Reader(d);
		Reader r2 = new Reader(d);
		Reader r3 = new Reader(d);
		Reader r4 = new Reader(d);
		Reader r5 = new Reader(d);
		
		w1.start();
		r1.start();
		r2.start();
		r3.start();
		w2.start();
		w3.start();
		w4.start();
		w5.start();
		r4.start();
		r5.start();
		
	}
}

class Reader extends Thread {
	Database d;

	public Reader(Database d) {
		this.d = d;
	}

	public void run() {

		for (int i = 0; i < 5; i++){		
			d.request_read();
			System.out.println(d.read());
			d.done_read();
			
		}
	}
}

class Writer extends Thread {

	Database d;
	int x;

	public Writer(Database d, int x) {
		this.d = d;
		this.x = x;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {			
			d.request_write();
			d.write(x);
			try { Thread.sleep(50); }
			catch (Exception e) {}
			d.done_write();
		}
	}
}



class Database {
	int data = 0;
	int r = 0;   // # active readers
	int w = 0;   // # active writers (0 or 1)
	int ww = 0;  // # waiting writers
	int wr = 0;  // # waiting readers
	
	Semaphore sr = new Semaphore(0);
	Semaphore sw = new Semaphore(0);
	Semaphore s = new Semaphore(1);

	public void request_read() {
		try {
			s.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (w == 1 || ww > 0) {
//			try { wr++;  wait();  wr--; }   
//		    catch (Exception e) {
//			}
			wr++;
			s.release();
			try {
				sr.acquire();
				s.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			wr--;
		}
		r++;
		s.release();
	}

	public void done_read() {
		try {
			s.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		r--;
//		notifyAll();
		int len = sr.getQueueLength();
		sr.release(len);
		int len1 = sw.getQueueLength();
		sw.release(len1);
		
		s.release();
	}

	public void request_write() {
// 		while (r > 0 || w == 1) {
//			try {
//				 ww++;  wait();  ww--;
//			} catch (Exception e) {}
// 		}
//		w = 1;
		
		try {
			s.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while (r > 0 || w == 1) {
			ww++;
			s.release();
			try {
				sw.acquire();
				s.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ww--;
		}
		w = 1;
		
		s.release();
	}

	public void done_write() {
//		w = 0;
//		notifyAll();
		try {
			s.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		w=0;
//		notifyAll();
		int len = sw.getQueueLength();
		sw.release(len);
		int len1 = sr.getQueueLength();
		sr.release(len1);
		
		s.release();
	} 

	int read() {
		return data;
	}

	void write(int x) {
		data = data + x;
	}
}
