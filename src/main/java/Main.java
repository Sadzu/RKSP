import frames.MainFrame;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MainFrame frame1 = new MainFrame();
        MainFrame frame2 = new MainFrame();
        frame1.init(true);
        frame2.init(false);
    }
}
