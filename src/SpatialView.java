
// View for cell space

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class SpatialView extends JPanel implements ActionListener
{
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;

    private int _width;
    private int _height;

    private CellViewModel[][] _space;

    private LifeController _controller;

    private JPanel _board;

    // ToDo other widgets

    private JButton resetButton;
    private JButton randomizeButton;
    private JTextField sizeField;
    private JButton resizeButton;
    private JButton stepButton;
    private JTextField incrementField;
    private JButton startButton;
    private JButton stopButton;
    private JCheckBox taurusBox;
    private JLabel minSurvivalLabel;
    private JTextField minSurvivalField;
    private JLabel maxSurvivalLabel;
    private JTextField maxSurvivalField;
    private JLabel minBirthLabel;
    private JTextField minBirthField;
    private JLabel maxBirthLabel;
    private JTextField maxBirthField;
    private JButton applyButton;

    public SpatialView()
    {
        _controller = new LifeController(this, 2, 3, 3, 3, false, 50);

        _width = 50;
        _height = 50;

        setLayout(new BorderLayout());

        _board = new JPanel();
        populateBoard();

        add(_board, BorderLayout.CENTER);

        // ToDo initialize and add other widgets

        {
            resetButton = new JButton("Clear");
            randomizeButton = new JButton("Randomize");
            sizeField = new JTextField("100");
            resizeButton = new JButton("Resize");
            stepButton = new JButton("Step >");
            incrementField = new JTextField("1000");
            startButton = new JButton("Start >");
            stopButton = new JButton("Stop");
            taurusBox = new JCheckBox("Taurus");
            minSurvivalLabel = new JLabel("Survival Min:");
            minSurvivalField = new JTextField("2");
            maxSurvivalLabel = new JLabel("Survival Max:");
            maxSurvivalField = new JTextField("3");
            minBirthLabel = new JLabel("Reprod. Min:");
            minBirthField = new JTextField("3");
            maxBirthLabel = new JLabel("Reprod. Max:");
            maxBirthField = new JTextField("3");
            applyButton = new JButton("Apply");

            JPanel controlPanel = new JPanel(new BorderLayout());
            JPanel cPanel = new JPanel();
            JPanel cPanel2 = new JPanel();

            cPanel.add(resetButton);
            cPanel.add(randomizeButton);
            cPanel.add(sizeField);
            cPanel.add(resizeButton);
            cPanel.add(stepButton);
            cPanel.add(incrementField);
            cPanel.add(startButton);
            cPanel.add(stopButton);
            cPanel2.add(taurusBox);
            cPanel2.add(minSurvivalLabel);
            cPanel2.add(minSurvivalField);
            cPanel2.add(maxSurvivalLabel);
            cPanel2.add(maxSurvivalField);
            cPanel2.add(minBirthLabel);
            cPanel2.add(minBirthField);
            cPanel2.add(maxBirthLabel);
            cPanel2.add(maxBirthField);
            cPanel2.add(applyButton);

            resetButton.addActionListener(this);
            randomizeButton.addActionListener(this);
            resizeButton.addActionListener(this);
            stepButton.addActionListener(this);
            startButton.addActionListener(this);
            stopButton.addActionListener(this);
            applyButton.addActionListener(this);

            controlPanel.add(cPanel, BorderLayout.NORTH);
            controlPanel.add(cPanel2, BorderLayout.CENTER); // Previously SOUTH

            add(controlPanel, BorderLayout.SOUTH);
        }
    }

    public void populateBoard()
    {

        _board.removeAll();
        _board.setLayout(new GridLayout(_height, _width));
        _space = new CellViewModel[_width][_height];

        Dimension cellSize = new Dimension(DEFAULT_WIDTH/_width, DEFAULT_HEIGHT/_height);

        // Number of cells we'll need to populate with
        int xcells = _width * _height;

        for (int y = 0; y < _height; y++)
        {
            for (int x = 0; x < _width; x++)
            {
                _space[x][y] = new CellViewModel(_controller, x, y);
                _space[x][y].setPreferredSize(cellSize);
                _board.add(_space[x][y]);
            }
        }


        _board.revalidate();
        _board.repaint();
    }

    public int getSpaceWidth()
    {
        return _width;
    }

    public int getSpaceHeight()
    {
        return _height;
    }

    public CellViewModel getCell(int x, int y)
    {
        return _space[x][y];
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // ToDo handle button clicks

        if (e.getSource() == resetButton)
        {
            _controller.stop();
            _controller.clear();
        }
        else if (e.getSource() == randomizeButton)
        {
            _controller.stop();
            _controller.randomize();
        }
        else if (e.getSource() == resizeButton)
        {
            _controller.stop();
            int size = parseInt(sizeField.getText());
            _width = size;
            _height = size;
            populateBoard();
        }
        else if (e.getSource() == stepButton)
        {
            _controller.stop();
            _controller.step();
        }
        else if (e.getSource() == startButton)
        {
            _controller.stop();
            int step = parseInt(incrementField.getText());
            _controller.setStep(step);
            _controller.start();
        }
        else if (e.getSource() == stopButton)
        {
            _controller.stop();
        }
        else if (e.getSource() == applyButton)
        {
            // ToDo apply values
            boolean taurus = taurusBox.isSelected();
            int minSurvival = parseInt(minSurvivalField.getText());
            int maxSurvival = parseInt(maxSurvivalField.getText());
            int minBirth = parseInt(minBirthField.getText());
            int maxBirth = parseInt(maxBirthField.getText());

            _controller.setTaurus(taurus);
            _controller.setMinSurvival(minSurvival);
            _controller.setMaxSurvival(maxSurvival);
            _controller.setMinBirth(minBirth);
            _controller.setMaxBirth(maxBirth);
        }
    }
}
