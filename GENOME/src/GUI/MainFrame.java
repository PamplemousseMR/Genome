package GUI;

import Utils.Logs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import static GUI.Constant.*;

public final class MainFrame extends ResizibleFrame {

    private static final String s_TITLE = "GENOME";
    private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Toolkit s_TOOLKIT = Toolkit.getDefaultToolkit();
    private static final int s_DEFAULT_FRAME_WIDTH = 300;
    private static final int s_DEFAULT_FRAME_HEIGHT = 300;
    private static final Point s_INITIAL_LOCATION = new Point((int) s_TOOLKIT.getScreenSize().getWidth() / 2 - s_DEFAULT_FRAME_WIDTH / 2, (int) s_TOOLKIT.getScreenSize().getHeight() / 2 - s_DEFAULT_FRAME_HEIGHT / 2);
    private static final Dimension s_INITIAL_DIMENSION = new Dimension(s_DEFAULT_FRAME_WIDTH, s_DEFAULT_FRAME_HEIGHT);
    private static MainFrame s_mainFrame;

    private JPanel m_header;
    private JPanel m_main;
    private TitlePanel m_footer;

    private JPanel m_menuPanel;
    private JLabel m_mainTitle;
    private JLabel m_secondTitle;

    private JButton m_closeB;
    private JButton m_maximizeB;
    private JButton m_minimizeB;

    private JSplitPane m_splitPanel_main;
    private JSplitPane m_rightContainer;
    private JPanel m_leftContainer;
    private JPanel m_leftSouthContainer;

    private ProgressBar m_progessBar;
    private TreePanel m_treePanel;
    private InformationPanel m_informationsPanel;
    private ActivityPanel m_activityPanel;
    private LogsPanel m_logsPanel;

    /**
     * Constructor
     */
    private MainFrame() {
        super(s_INITIAL_DIMENSION, s_INITIAL_LOCATION, s_TITLE);
        initFrame();
        initComponents();
        initLayout();
        addComponents();
        swagComponents();
        addListener();
    }

    /**
     * Get the singleton
     *
     * @return the singleton
     */
    public static MainFrame getSingleton() {
        if (s_mainFrame == null) {
            s_mainFrame = new MainFrame();
        }
        return s_mainFrame;
    }

    /**
     * Update JTree
     *
     * @param _path the path use to updateTree JTree
     */
    public void updateTree(String _path) {
        SwingUtilities.invokeLater(() -> m_treePanel.update(_path));
    }

    /**
     * Display log
     *
     * @param _log to display
     */
    public void updateLog(String _log, Logs.Type _type) {
        SwingUtilities.invokeLater(() -> m_logsPanel.updateLog(_log, _type));
    }

    /**
     * Update the value of the progress bar
     *
     * @param _value the value to set
     */
    public void updateProgresse(int _value) {
        SwingUtilities.invokeLater(() -> m_progessBar.setValue(_value));
    }

    /**
     * Add start action
     *
     * @param _actionListener the start action
     */
    public void setStartAction(ActionListener _actionListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.setStartAction(_actionListener));
    }

    /**
     * Add stop action
     *
     * @param _actionListener the stop action
     */
    public void setStopAction(ActionListener _actionListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.setStopAction(_actionListener));
    }

    /**
     * Add pause action
     *
     * @param _actionListener the pause action
     */
    public void setPauseAction(ActionListener _actionListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.setPauseAction(_actionListener));
    }

    /**
     * Add resume action
     *
     * @param _actionListener the resume action
     */
    public void setResumeAction(ActionListener _actionListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.setResumeAction(_actionListener));
    }

    /**
     * Set the maximum of the progress bar
     *
     * @param _max the max to set
     */
    public void setProgresseMax(int _max) {
        SwingUtilities.invokeLater(() -> m_progessBar.setMaximum(_max));
    }

    /**
     * Basic frame inits
     */
    private void initFrame() {
        initIcone();
        setUndecorated(true);
        setVisible(true);
        setSize((s_DIM.width / 2), (s_DIM.height / 2));
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Initialize the icon of the app
     */
    private void initIcone() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("Ressources/logo.bioinfo.png"))));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }

    /**
     * Initialize the components
     */
    private void initComponents() {
        m_header = new JPanel();
        m_main = new JPanel();
        m_footer = new TitlePanel("Application cree par -- Romain M. -- Florian H. -- Vincent H. -- Sami F. -- Arthur D.  -- Romain T. -- Adele M.", 12, s_WHITE, s_DARKGRAY);

        m_menuPanel = new JPanel();
        m_mainTitle = new JLabel("Projet de Bio-Informatique");
        m_secondTitle = new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");

        m_closeB = new JButton();
        m_maximizeB = new JButton();
        m_minimizeB = new JButton();

        m_progessBar = new ProgressBar();
        m_treePanel = new TreePanel();
        m_informationsPanel = new InformationPanel();
        m_logsPanel = new LogsPanel();
        m_activityPanel = new ActivityPanel();

        m_leftContainer = new JPanel();
        m_leftSouthContainer = new JPanel();
        m_rightContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_informationsPanel, m_logsPanel);
        m_splitPanel_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_leftContainer, m_rightContainer);
    }

    /**
     * Initialize layout types
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        m_main.setLayout(new BorderLayout());
        m_header.setLayout(new BorderLayout());

        m_menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        m_leftContainer.setLayout(new BorderLayout());
        m_leftSouthContainer.setLayout(new BorderLayout());
    }

    /**
     * Add components into layouts
     */
    private void addComponents() {
        super.add(m_header, BorderLayout.NORTH);
        super.add(m_main, BorderLayout.CENTER);
        super.add(m_footer, BorderLayout.SOUTH);

        m_header.add(m_menuPanel, BorderLayout.NORTH);
        m_header.add(m_mainTitle, BorderLayout.CENTER);
        m_header.add(m_secondTitle, BorderLayout.SOUTH);

        m_menuPanel.add(m_minimizeB);
        m_menuPanel.add(m_maximizeB);
        m_menuPanel.add(m_closeB);

        m_main.add(m_splitPanel_main, BorderLayout.CENTER);
        m_leftContainer.add(m_treePanel, BorderLayout.CENTER);
        m_leftContainer.add(m_leftSouthContainer, BorderLayout.SOUTH);

        m_leftSouthContainer.add(m_progessBar, BorderLayout.NORTH);
        m_leftSouthContainer.add(m_activityPanel, BorderLayout.SOUTH);
    }

    /**
     * Swag the interface
     */
    private void swagComponents() {
        getRootPane().setBorder(BorderFactory.createEmptyBorder());

        m_menuPanel.setBackground(s_DARKGRAY);
        m_menuPanel.setPreferredSize(new Dimension(s_DEFAULT_FRAME_WIDTH, 35));
        m_mainTitle.setFont(new Font(s_FONT, Font.PLAIN, 28));
        m_mainTitle.setForeground(s_WHITE);
        m_secondTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
        m_secondTitle.setForeground(s_BLUE);

        swagMenuButton(m_closeB, "Ressources/close.png");
        swagMenuButton(m_minimizeB, "Ressources/minimize.png");
        swagMenuButton(m_maximizeB, "Ressources/maximize.png");

        m_header.setBackground(s_DARKGRAY);
        m_main.setBackground(s_LIGHTGRAY);

        m_splitPanel_main.setResizeWeight(.15d);
        m_splitPanel_main.setDividerSize(3);
        m_splitPanel_main.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_rightContainer.setResizeWeight(.80d);
        m_rightContainer.setDividerSize(3);
        m_rightContainer.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_leftContainer.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));

        m_activityPanel.setPreferredSize(new Dimension(120, 30));
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

    /**
     * Add listener on components
     * Manage the action to do when a listener is triggered
     */
    private void addListener() {
        m_menuPanel.addMouseListener(this);
        m_menuPanel.addMouseMotionListener(this);

        m_closeB.addActionListener(e -> System.exit(0));
        m_minimizeB.addActionListener(e -> setState(Frame.ICONIFIED));
        m_maximizeB.addActionListener(e -> {
            if (getExtendedState() == MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }

}

