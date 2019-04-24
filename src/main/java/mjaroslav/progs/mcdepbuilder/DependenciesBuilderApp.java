package mjaroslav.progs.mcdepbuilder;

import java.awt.*;

import javax.swing.*;

import mjaroslav.util.UtilsMonitor;

// WIP
public class DependenciesBuilderApp extends JFrame {
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
        DependenciesBuilderApp app = new DependenciesBuilderApp();
        app.pack();
        app.setLocation(UtilsMonitor.getCenterHorizontal(app.getWidth() / 2),
                UtilsMonitor.getCenterVertical(app.getHeight() / 2));
        app.setVisible(true);
    }

    public DependenciesBuilderApp() {
        super("Dependencies Builder");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setResizable(false);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        JPanel left = initLeftSide();
        c.add(left);
        JScrollPane right = initRightSide();
        c.add(right);
    }

    public JPanel initLeftSide() {
        JPanel result = new JPanel();
        // result.setLayout(new GridLayout(rows, cols));
        JLabel label1 = new JLabel("Modid:");
        result.add(label1);
        result.add(new JTextField());
        result.add(new JCheckBox("Is required"), BorderLayout.WEST);
        result.add(new JLabel("Min version:"), BorderLayout.WEST);
        result.add(new JTextField());
        result.add(new JCheckBox("Only this version"), BorderLayout.WEST);
        result.add(new JLabel("Priority:"), BorderLayout.WEST);
        result.add(new JComboBox<String>(new String[] { "NONE", "BEFORE", "AFTER" }));
        result.add(new JButton("Add"));
        result.add(new JButton("Remove"));
        result.add(new JButton("Clipboard"));
        JButton b = new JButton("View result");
        result.add(b);
        return result;
    }

    public JScrollPane initRightSide() {
        JScrollPane result = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result.setPreferredSize(new Dimension(200, 200));
        return result;
    }
}
