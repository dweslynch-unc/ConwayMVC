import javax.swing.*;
import java.awt.*;

public class Life
{
    public static void main(String[] args) {

        JFrame main_frame = new JFrame();
        main_frame.setTitle("Conway's Game of Life");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BorderLayout());
        main_frame.setContentPane(top_panel);

        SpatialView space = new SpatialView();
        top_panel.add(space, BorderLayout.CENTER);

        main_frame.pack();
        main_frame.setVisible(true);
    }
}
