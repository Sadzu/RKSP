import frames.MainFrame;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MainFrame frame1 = new MainFrame();
        MainFrame frame2 = new MainFrame();
        MainFrame frame3 = new MainFrame();
        frame1.init("localhost", 5000);
        frame2.init("localhost", 5000);
        frame3.init("localhost", 5000);
    }
}
