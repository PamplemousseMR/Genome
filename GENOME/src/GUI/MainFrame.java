package GUI;

import Utils.Logs;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends ResizibleFrame implements ActionListener {
    private static final long serialVersionUID = -6768656055410219611L;
    private static final String s_TITLE = "BioInformatique ILC";//. Realise par Adele M. -- Arthur D. --  Florian H. -- Romain M. -- Romain T. -- Sami F. -- Vincent H."
    private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color s_DARKGRAY = new Color(32, 34, 37);
    private static final Color s_LIGHTGRAY = new Color(54, 57, 62);
    private static Border s_basicEmptyBorder = BorderFactory.createEmptyBorder(0, 20, 20, 20);
    private static Insets s_insets = new Insets(1, 1, 1, 1);
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static int s_defaultFrameWidth = 300;
    private static int s_defaultFrameHeight = 300;
    private static Point m_initialLocation = new Point((int) toolkit.getScreenSize().getWidth() / 2 - s_defaultFrameWidth / 2, (int) toolkit.getScreenSize().getHeight() / 2 - s_defaultFrameHeight / 2);
    private static Dimension m_initialDimension = new Dimension(s_defaultFrameWidth, s_defaultFrameHeight);
    private JPanel m_menuPanel;
    private JSplitPane m_splitPanel_main;
    private JSplitPane m_splitPanel_right;
    private JPanel m_north;
    private JPanel m_center;
    private JPanel m_fileTreePanel;
    private JPanel m_downloadStatePanel;
    private JPanel m_logsPanel;
    private JButton m_launchDL;
    private JLabel m_titleLabel;
    private JLabel m_titleLabel2;
    private JProgressBar m_jpb;
    private JButton m_exitB;
    private JButton m_fullScreenB;
    private JButton m_reduceB;

    /**
     * ctor
     */
    public MainFrame() {
        super(m_initialDimension, m_initialLocation, s_TITLE);
        initFrame();
        initComponents();
        initLayout();
        addComponents();
        swagComponents();
        addListener();
    }

    /**
     * Basic frame inits
     */
    private void initFrame() {
        this.initIcone();
        this.setUndecorated(true);
        this.setVisible(true);
        this.setSize((s_DIM.width / 2), (s_DIM.height / 2));
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Initialize the icon of the app
     */
    private void initIcone() {
        try {
            this.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("Ressources/logo.bioinfo.png"))));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }

    /**
     * Initialize the components
     */
    private void initComponents() {
        //Creation of one panel per area
        m_menuPanel = new JPanel();
        m_north = new JPanel();
        m_center = new JPanel();

        m_fileTreePanel = new JPanel();
        m_downloadStatePanel = new JPanel();
        m_logsPanel = new JPanel();
        m_splitPanel_right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_downloadStatePanel, m_logsPanel);
        m_splitPanel_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_fileTreePanel, m_splitPanel_right);
        // Components
        m_titleLabel = new JLabel("Projet de Bio-Informatique");
        m_titleLabel2 = new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");
        m_launchDL = new JButton("Demarrer le telechargement");
        m_jpb = new JProgressBar();

        m_exitB = new JButton(" X ");
        m_fullScreenB = new JButton("[�]"); //[-]
        m_reduceB = new JButton(" - ");
    }

    /**
     * Initialize layout types
     */
    private void initLayout() {
        this.setLayout(new BorderLayout());
        m_menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        m_center.setLayout(new BorderLayout());
        m_north.setLayout(new BorderLayout());
        m_fileTreePanel.setLayout(new BorderLayout());
        m_downloadStatePanel.setLayout(new BorderLayout());
        m_logsPanel.setLayout(new BorderLayout());
    }

    /**
     * Add components into layouts
     */
    private void addComponents() {
        this.add(m_north, BorderLayout.NORTH);
        this.add(m_center, BorderLayout.CENTER);

        // Menu
        m_menuPanel.add(m_reduceB);
        m_menuPanel.add(m_fullScreenB);
        m_menuPanel.add(m_exitB);

        m_center.add(m_splitPanel_main, BorderLayout.CENTER);

        m_north.add(m_menuPanel, BorderLayout.NORTH);
        m_north.add(m_titleLabel, BorderLayout.CENTER);
        m_north.add(m_titleLabel2, BorderLayout.SOUTH);

        m_fileTreePanel.add(m_launchDL, BorderLayout.SOUTH);

        m_downloadStatePanel.add(m_jpb, BorderLayout.SOUTH);

    }

    /**
     * Swag the interface
     */
    private void swagComponents() {
        //to modify window swag,check the LookAndFeel thing
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        m_splitPanel_right.setResizeWeight(.80d);
        m_splitPanel_main.setResizeWeight(.10d);

        m_menuPanel.setBackground(s_DARKGRAY);
        m_north.setBackground(s_DARKGRAY);     // Dark gray
        m_center.setBackground(s_LIGHTGRAY);     // Gray
        m_fileTreePanel.setBackground(s_LIGHTGRAY);
        m_downloadStatePanel.setBackground(s_LIGHTGRAY);
        m_logsPanel.setBackground(s_LIGHTGRAY);

        // SplitPane
        m_splitPanel_main.setDividerLocation(0.5);

        // Panels margin (top, left, bottom, right)
        //m_east.setBorder(s_basicEmptyBorder);
        //m_west.setBorder(s_basicEmptyBorder);
        m_fileTreePanel.setBorder(s_basicEmptyBorder);
        m_downloadStatePanel.setBorder(s_basicEmptyBorder);
        m_logsPanel.setBorder(s_basicEmptyBorder);

        // Labels
        m_titleLabel.setFont(new Font("Helvetica", Font.PLAIN, 28));
        m_titleLabel2.setFont(new Font("Helvetica", Font.PLAIN, 18));
        m_titleLabel.setForeground(Color.WHITE);
        m_titleLabel2.setForeground(Color.LIGHT_GRAY);
        m_titleLabel.setBorder(s_basicEmptyBorder);
        m_titleLabel2.setBorder(BorderFactory.createEmptyBorder(0, 25, 5, 5));

        // Progress bar
        m_jpb.setStringPainted(true);
        m_jpb.setString("ProgressBar");
        //m_jpb.setSize(m_south.getWidth(),( m_south.getHeight()));
        m_jpb.setBackground(new Color(66, 86, 142));     //BLUE

        // Buttons
        m_launchDL.setBackground(Color.LIGHT_GRAY);
        m_launchDL.setForeground(s_LIGHTGRAY);  // Light gray
        m_launchDL.setBorderPainted(false);

        // Menu Bar
        m_menuPanel.setPreferredSize(new Dimension(s_defaultFrameWidth, 35));


        Font font = new Font("Helvetica", Font.BOLD, 16);
        m_exitB.setMargin(s_insets);
        m_reduceB.setMargin(s_insets);
        m_fullScreenB.setMargin(s_insets);
        m_exitB.setBackground(s_DARKGRAY);
        m_fullScreenB.setBackground(s_DARKGRAY);
        m_reduceB.setBackground(s_DARKGRAY);
        m_exitB.setForeground(Color.WHITE);
        m_fullScreenB.setForeground(Color.WHITE);
        m_reduceB.setForeground(Color.WHITE);
        m_exitB.setFont(font);
        m_fullScreenB.setFont(font);
        m_reduceB.setFont(font);

        //could be useful
        //UIManager.put("Button.select", Color.WHITE);

    }

    /**
     * Add listener on components
     * Manage the action to do when a listener is triggered
     */
    private void addListener() {
        //for resize and fullscreen by double-click
        m_menuPanel.addMouseListener(this);
        m_menuPanel.addMouseMotionListener(this);

        m_launchDL.addActionListener(new CloseListener());
        m_exitB.addActionListener(new CloseListener());

        m_reduceB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reduceActionPerformed(e);
            }
        });
        m_fullScreenB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fullScreenActionPerformed(e);
            }
        });

    }


    protected void reduceActionPerformed(ActionEvent e) {
        this.setState(Frame.ICONIFIED);
    }

    protected void fullScreenActionPerformed(ActionEvent e) {
        if (this.getExtendedState() == MAXIMIZED_BOTH) {
            this.setExtendedState(JFrame.NORMAL);
        } else {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        //frame.setUndecorated(true);
        //frame.setVisible(true);*/

    }

    /**
     * @return the initialLocation
     */
    public Point getInitialLocation() {
        return m_initialLocation;
    }

    /**
     * /**
     *
     * @return the s_initialDimension
     */
    public Dimension getS_initialDimension() {
        return m_initialDimension;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }


}

