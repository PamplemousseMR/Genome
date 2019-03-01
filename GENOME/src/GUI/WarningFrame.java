package GUI;

import Utils.Logs;
import Utils.Options;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static GUI.Constant.*;

public class WarningFrame extends JFrame {

    /**
     * Class constructor
     */
    public WarningFrame() {
        super();
        final JPanel panel = new IPanel(s_TITLE) {

            private JButton m_closeB;
            private JPanel m_menuPanel;
            private JPanel m_container;
            private JLabel m_label1;
            private JLabel m_label2;
            private JLabel m_label3;

            protected void createComponent() {
                m_closeB = new JButton();
                m_menuPanel = new JPanel();
                m_container = new JPanel();
                m_label1 = new JLabel("Impossible de relancer le programme.");
                m_label2 = new JLabel("- Soit le programme est encore en cours d'exécution et attend la fin des threads.");
                m_label3 = new JLabel("- Soit la précédente exécution a été interrompue prématurément. Dans ce cas,veuillez supprimer le fichier  \"" + Options.getMutexFileName() + "\".");

                m_closeB.addActionListener(e -> System.exit(0));
            }

            protected void initLayout() {
                m_container.setLayout(new BoxLayout(m_container, BoxLayout.Y_AXIS));
                m_menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            }

            protected void addComponents() {
                m_container.add(m_label1);
                m_container.add(m_label2);
                m_container.add(m_label3);
                m_menuPanel.add(m_closeB);
                super.add(m_menuPanel, BorderLayout.NORTH);
                super.add(m_container, BorderLayout.CENTER);
            }

            protected void swagComponent() {
                m_label1.setBackground(s_LIGHTGRAY);
                m_label1.setForeground(s_WHITE);
                m_label1.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_label2.setBackground(s_LIGHTGRAY);
                m_label2.setForeground(s_WHITE);
                m_label2.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_label3.setBackground(s_LIGHTGRAY);
                m_label3.setForeground(s_WHITE);
                m_label3.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_container.setBackground(s_LIGHTGRAY);
                m_container.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
                m_menuPanel.setBackground(s_DARKGRAY);
                swagMenuButton(m_closeB, "Ressources/close.png");
            }

            /**
             * Swag menu button
             *
             * @param _button button to swag
             * @param _path   path to the icon
             */
            private void swagMenuButton(JButton _button, String _path) {
                _button.setMargin(s_INSETS);
                _button.setBackground(s_DARKGRAY);
                _button.setForeground(s_WHITE);
                _button.setFont(new Font(s_FONT, Font.BOLD, 16));
                _button.setBorder(BorderFactory.createEmptyBorder());
                _button.setHorizontalAlignment(SwingConstants.CENTER);
                _button.setVerticalAlignment(SwingConstants.CENTER);
                _button.setPreferredSize(new Dimension(30, 30));

                _button.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        _button.setBackground(s_BLUEGRAY);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        _button.setBackground(null);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }
                });

                try {
                    Image img = ImageIO.read(getClass().getResource(_path)).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
                    _button.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    Logs.exception(e);
                }
            }
        };
        initIcone();
        setUndecorated(true);
        setResizable(false);
        setSize(830, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setVisible(true);
    }

    /**
     * Initialize the icon of the app
     */
    private void initIcone() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("Ressources/logo.png"))));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }
}
