package app.Client.GUImanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * AdminPageGUI is the page for an administrator after they log in.
 * The idea is very similar to StudentPageGUI, but here we show six buttons
 * for the common admin tasks:
 *   - Create a course
 *   - Edit a course
 *   - Delete a course
 *   - Generate report
 *   - View students schedules
 *   - Student holds
 *
 * Right now, each button just shows a simple "coming soon" dialog.
 * Later, these buttons will be connected to real methods in SystemManager
 * so that an admin can actually manage courses and student holds.
 */
public class AdminPageGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // gray background for the whole window, same style as the student page.
    private static final Color BACKGROUND = new Color(230, 230, 230);

    // Blue color for the main admin buttons so they match the overall theme.
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    // We keep the welcome message so we can show something like "Welcome Admin"
    // at the top of the window. This makes the page super friendly.
    private String welcomeMessage;

    /*
     * Constructor: this should be called right after the admin logs in.
     * The welcomeMessage can be used to show a personalized header, e.g.
     * "Welcome Alice (Admin)".
     */
    public AdminPageGUI(String welcomeMessage) {
        super("Administrator Dashboard");
        this.welcomeMessage = welcomeMessage;
        initializeFrame();
    }

    /*
     * This method sets up the main admin window.
     * It is also very similar to the login and student pages:
     *   - set a fixed size,
     *   - center the window on the screen,
     *   - set a background color,
     *   - use BorderLayout so we can put the title at the top and buttons in the center.
     *
     * We use DISPOSE_ON_CLOSE so closing the admin window does not kill the entire program.
     */
    private void initializeFrame() {
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    /*
     * This method builds the content of the admin page.
     *
     * Layout idea:
     *  top-> a welcome label using the welcomeMessage string
     *  (center) -> a 2 x 3 grid of large buttons for each admin function
     */
    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));

        JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        wrapper.add(title, BorderLayout.NORTH);

        /*
         * Create the 2 x 3 grid for the six admin buttons.
         * GridLayout is useful here because it makes all six buttons the same size
         * and puts them in two rows with three columns.
         */
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        // Each button is created with the same helper method.

        grid.add(createActionButton("Create a course"));
        grid.add(createActionButton("Edit a course"));
        grid.add(createActionButton("Delete a course"));
        grid.add(createActionButton("Generate report"));
        grid.add(createActionButton("View students schedules"));
        grid.add(createActionButton("Student holds"));

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    /*
     * Helper method to create one of the admin buttons.
     * We using the same size, colors, and font
     * for all six buttons.
     *
     * The ActionListener simply calls showComingSoon(label), which shows a
     * message dialog saying that this feature will be connected later.
     */
    private JButton createActionButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setOpaque(true);

        // For now, all admin actions are still TODO, so we just show a placeholder.
        button.addActionListener(e -> showComingSoon(label));
        return button;
    }

    /*
     * This method is used for all six buttons right now.
     * Instead of leaving the button empty, we show a message explaining
     * that the real logic will be wired to SystemManager later.

     */
    private void showComingSoon(String action) {
        JOptionPane.showMessageDialog(
                this,
                action + " will be connected to the SystemManager in a later phase.",
                "Feature placeholder",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
