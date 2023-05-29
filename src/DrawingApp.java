import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class DrawingApp extends JFrame {
    private Canvas canvas;
    private JMenuBar menuBar;
    private JMenu drawMenu;
    private JMenu mosaicMenu;

    public DrawingApp() {
        setTitle("Drawing App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        drawMenu = new JMenu("Draw");
        menuBar.add(drawMenu);

        JMenuItem drawObjectItem = new JMenuItem("Draw Object");
        drawObjectItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDrawObjectDialog();
            }
        });
        drawMenu.add(drawObjectItem);

        mosaicMenu = new JMenu("Mosaic");
        menuBar.add(mosaicMenu);

        JMenuItem generateMosaicItem = new JMenuItem("Generate Mosaic");
        generateMosaicItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMosaicDialog();
            }
        });
        mosaicMenu.add(generateMosaicItem);
    }

    private void showDrawObjectDialog() {
        Dialog dialog = new Dialog(this, "Draw Object", true);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel widthLabel = new JLabel("Width:");
        JTextField widthField = new JTextField();

        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField();

        JLabel colorLabel = new JLabel("Color:");
        JComboBox<String> colorCombo = new JComboBox<>(new String[]{"Red", "Green", "Blue"});

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                Color color = getColorFromCombo(colorCombo);
                canvas.drawObject(width, height, color);
                dialog.dispose();
            }
        });

        dialog.add(widthLabel);
        dialog.add(widthField);
        dialog.add(heightLabel);
        dialog.add(heightField);
        dialog.add(colorLabel);
        dialog.add(colorCombo);
        dialog.add(new JLabel()); // Empty label for spacing
        dialog.add(drawButton);

        dialog.pack();
        dialog.setVisible(true);
    }
    private Color[] getColorsFromCombo(JComboBox<String> combo) {
        Color[] colors = new Color[4];
        for (int i = 0; i < 4; i++) {
            String selectedColor = (String) combo.getItemAt(i);
            switch (selectedColor) {
                case "Red":
                    colors[i] = Color.RED;
                    break;
                case "Green":
                    colors[i] = Color.GREEN;
                    break;
                case "Blue":
                    colors[i] = Color.BLUE;
                    break;
                case "Yellow":
                    colors[i] = Color.YELLOW;
                    break;
                default:
                    colors[i] = Color.BLACK;
                    break;
            }
        }
        return colors;
    }

    private void showMosaicDialog() {
        Dialog dialog = new Dialog(this, "Generate Mosaic", true);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel widthLabel = new JLabel("Width:");
        JTextField widthField = new JTextField();

        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField();

        JLabel blockSizeLabel = new JLabel("Block Size:");
        JComboBox<String> blockSizeCombo = new JComboBox<>(new String[]{"2x2", "4x4", "8x8"});

        JLabel colorLabel = new JLabel("Colors:");
        JComboBox<String> colorCombo = new JComboBox<>(new String[]{"Red", "Green", "Blue", "Yellow"});

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int blockSize = getBlockSizeFromCombo(blockSizeCombo);
                Color[] colors = getColorsFromCombo(colorCombo);
                canvas.generateMosaic(width, height, blockSize, colors);
                dialog.dispose();
            }
        });

        dialog.add(widthLabel);
        dialog.add(widthField);
        dialog.add(heightLabel);
        dialog.add(heightField);
        dialog.add(blockSizeLabel);
        dialog.add(blockSizeCombo);
        dialog.add(colorLabel);
        dialog.add(colorCombo);
        dialog.add(new JLabel()); // Empty label for spacing
        dialog.add(generateButton);

        dialog.pack();
        dialog.setVisible(true);
    }
    private int getBlockSizeFromCombo(JComboBox<String> combo) {
        String selectedSize = (String) combo.getSelectedItem();
        switch (selectedSize) {
            case "2x2":
                return 2;
            case "4x4":
                return 4;
            case "8x8":
                return 8;
            default:
                return 1;
        }
    }


    private Color getColorFromCombo(JComboBox<String> combo) {
        String selectedColor = (String) combo.getSelectedItem();
        switch (selectedColor) {
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DrawingApp app = new DrawingApp();
                app.setVisible(true);
            }
        });
    }
}

class Canvas extends JPanel {
    private BufferedImage image;
    private Graphics2D graphics;

    public Canvas() {
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public void drawObject(int width, int height, Color color) {
        graphics.setColor(color);
        graphics.fillRect(400 - width / 2, 300 - height / 2, width, height);
        repaint();
    }

    public void generateMosaic(int width, int height, int blockSize, Color[] colors) {
        Random random = new Random();

        int blockWidth = width / blockSize;
        int blockHeight = height / blockSize;

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        for (int x = 0; x < blockWidth; x++) {
            for (int y = 0; y < blockHeight; y++) {
                int colorIndex = random.nextInt(colors.length);
                Color color = colors[colorIndex];
                graphics.setColor(color);
                graphics.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}

class Dialog extends JDialog {
    public Dialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }
}
