package GUI;

import Utils.Logs;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class MainFrame extends ResizibleFrame implements ActionListener {

	private static final String s_TITLE = "BioInformatique ILC";//. Realise par Adele M. -- Arthur D. --  Florian H. -- Romain M. -- Romain T. -- Sami F. -- Vincent H."
	private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Color s_DARKGRAY = new Color(32, 34, 37);
	private static final Color s_LIGHTGRAY = new Color(54, 57, 62);
	private static final Color s_BLUEGRAY = new Color(62, 68, 83);
	private static final Insets s_INSETS = new Insets(1, 1, 1, 1);
	private static final Toolkit s_TOOLKIT = Toolkit.getDefaultToolkit();
	private static final int s_DEFAULT_FRAME_WIDTH = 300;
	private static final int s_DEFAULT_FRAME_HEIGHT = 300;
	private static final Point s_INITIAL_LOCATION = new Point((int) s_TOOLKIT.getScreenSize().getWidth() / 2 - s_DEFAULT_FRAME_WIDTH / 2, (int) s_TOOLKIT.getScreenSize().getHeight() / 2 - s_DEFAULT_FRAME_HEIGHT / 2);
	private static final Dimension s_INITIAL_DIMENSION = new Dimension(s_DEFAULT_FRAME_WIDTH, s_DEFAULT_FRAME_HEIGHT);
	private static final String s_FONT = "Helvetica";

	private JPanel m_menuPanel;
	private JSplitPane m_splitPanel_main;
	private JSplitPane m_splitPanel_right;
	private JPanel m_north;
	private JPanel m_center;
	private JPanel m_fileTreePanel;
	private JPanel m_downloadStatePanel;
	private JPanel m_logsPanel;
	private JPanel m_fileTreeTitlePanel;
	private JPanel m_downloadStateTitlePanel;
	private JPanel m_logsTitlePanel;
	private JButton m_launchDL;
	//private JButton m_lookForFiles;
	private JLabel m_titleLabel;
	private JLabel m_titleLabe2;
	private JLabel m_logsTitle;
	private JLabel m_downloadStateTitle;
	private JLabel m_treeTitle;
	private JButton m_exitB;
	private JButton m_fullScreenB;
	private JButton m_reduceB;

	/**
	 * ctor
	 */
	public MainFrame() {
		super(s_INITIAL_DIMENSION, s_INITIAL_LOCATION, s_TITLE);
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
		//Creation of one panel per area
		m_menuPanel = new JPanel();
		m_north = new JPanel();
		m_center = new JPanel();

		m_fileTreePanel = new JPanel();
		m_downloadStatePanel = new JPanel();
		m_logsPanel = new JPanel();
		m_fileTreeTitlePanel= new JPanel();
		m_downloadStateTitlePanel= new JPanel();
		m_logsTitlePanel= new JPanel();
		
		m_splitPanel_right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_downloadStatePanel, m_logsPanel);
		m_splitPanel_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_fileTreePanel, m_splitPanel_right);
		// Components
		m_titleLabel = new JLabel("   Projet de Bio-Informatique");
        m_titleLabe2 = new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");
        
    	m_downloadStateTitle= new JLabel("Informations");
    	m_treeTitle= new JLabel("Arborescence des fichiers");
    	m_logsTitle= new JLabel("Logs");
        //m_lookForFiles= new JButton("Voir les fichiers");
		m_launchDL = new JButton("Demarrer le telechargement");

		m_exitB = new JButton();
		m_fullScreenB = new JButton();
		m_reduceB = new JButton();
	}

	/**
	 * Initialize layout types
	 */
	private void initLayout() {
		setLayout(new BorderLayout());
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
		add(m_north, BorderLayout.NORTH);
		add(m_center, BorderLayout.CENTER);

		// Menu
		m_menuPanel.add(m_reduceB);
		m_menuPanel.add(m_fullScreenB);
		m_menuPanel.add(m_exitB);

        m_center.add(m_splitPanel_main, BorderLayout.CENTER);

		m_north.add(m_menuPanel, BorderLayout.NORTH);
		m_north.add(m_titleLabel, BorderLayout.CENTER);
        m_north.add(m_titleLabe2, BorderLayout.SOUTH);
        
        m_fileTreeTitlePanel.add(m_treeTitle, BorderLayout.CENTER);
        m_fileTreePanel.add(m_fileTreeTitlePanel, BorderLayout.NORTH);
		//m_fileTreePanel.add(m_lookForFiles, BorderLayout.NORTH);
		m_fileTreePanel.add(m_launchDL, BorderLayout.SOUTH);
		
		m_downloadStateTitlePanel.add(m_downloadStateTitle, BorderLayout.CENTER);
		m_downloadStatePanel.add(m_downloadStateTitlePanel, BorderLayout.NORTH);
		
		m_logsTitlePanel.add(m_logsTitle, BorderLayout.CENTER);
		m_logsPanel.add(m_logsTitlePanel, BorderLayout.NORTH);
		
	}

	/**
	 * Swag the interface
	 */
	private void swagComponents() {
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		m_splitPanel_right.setResizeWeight(.80d);
		m_splitPanel_main.setResizeWeight(.10d);
        m_splitPanel_right.setDividerSize(3);
		m_splitPanel_main.setDividerSize(3);

        m_menuPanel.setBackground(s_DARKGRAY);
		m_north.setBackground(s_DARKGRAY);     // Dark gray
		m_center.setBackground(s_LIGHTGRAY);     // Gray
		m_fileTreePanel.setBackground(s_LIGHTGRAY);
		m_downloadStatePanel.setBackground(s_LIGHTGRAY);
		m_logsPanel.setBackground(s_LIGHTGRAY);
		
		m_fileTreeTitlePanel.setBackground(s_BLUEGRAY);
		m_downloadStateTitlePanel.setBackground(s_BLUEGRAY);
		m_logsTitlePanel.setBackground(s_BLUEGRAY);
		
		m_titleLabel.setFont(new Font(s_FONT, Font.PLAIN, 28));
		m_titleLabe2.setFont(new Font(s_FONT, Font.PLAIN, 18));
		m_titleLabel.setForeground(Color.WHITE);
		m_titleLabe2.setForeground(Color.LIGHT_GRAY);
		m_titleLabe2.setBorder(BorderFactory.createEmptyBorder(0, 25, 5, 5));
		m_logsTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
    	m_downloadStateTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
    	m_treeTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
    	m_logsTitle.setForeground(Color.WHITE);
    	m_downloadStateTitle.setForeground(Color.WHITE);
    	m_treeTitle.setForeground(Color.WHITE);
    	
		//m_lookForFiles.setBackground(Color.LIGHT_GRAY);
		//m_lookForFiles.setForeground(s_LIGHTGRAY);  // Light gray
		//m_lookForFiles.setBorderPainted(false);
		m_launchDL.setBackground(Color.LIGHT_GRAY);
		m_launchDL.setForeground(s_LIGHTGRAY);  // Light gray
		m_launchDL.setBorderPainted(false);

		m_menuPanel.setPreferredSize(new Dimension(s_DEFAULT_FRAME_WIDTH, 35));

		m_exitB.setMargin(s_INSETS);
        m_reduceB.setMargin(s_INSETS);
		m_fullScreenB.setMargin(s_INSETS);

		m_exitB.setBackground(s_DARKGRAY);
        m_reduceB.setBackground(s_DARKGRAY);
        m_fullScreenB.setBackground(s_DARKGRAY);

		m_exitB.setForeground(Color.WHITE);
        m_reduceB.setForeground(Color.WHITE);
        m_fullScreenB.setForeground(Color.WHITE);

		m_exitB.setFont(new Font(s_FONT, Font.BOLD, 16));
        m_reduceB.setFont(new Font(s_FONT, Font.BOLD, 16));
        m_fullScreenB.setFont(new Font(s_FONT, Font.BOLD, 16));

        m_exitB.setBorder(BorderFactory.createEmptyBorder());
        m_reduceB.setBorder(BorderFactory.createEmptyBorder());
        m_fullScreenB.setBorder(BorderFactory.createEmptyBorder());

        try {
            Image img = ImageIO.read(getClass().getResource("Ressources/close.png")).getScaledInstance(20,20, Image.SCALE_DEFAULT);
            m_exitB.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            Logs.exception(e);
        }

        try {
            Image img = ImageIO.read(getClass().getResource("Ressources/minimize.png")).getScaledInstance(20,20, Image.SCALE_DEFAULT);
            m_reduceB.setIcon(new ImageIcon(img));
        }catch (Exception e) {
            Logs.exception(e);
        }

        try {
            Image img = ImageIO.read(getClass().getResource("Ressources/maximize.png")).getScaledInstance(20,20, Image.SCALE_DEFAULT);
            m_fullScreenB.setIcon(new ImageIcon(img));
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

		//m_lookForFiles.addActionListener(new OpenFileSystemListener());
		m_launchDL.addActionListener(new CloseListener());
		m_exitB.addActionListener(new CloseListener());

		m_reduceB.addActionListener(e -> setState(Frame.ICONIFIED));
		m_fullScreenB.addActionListener(e -> {
			if (getExtendedState() == MAXIMIZED_BOTH) {
				setExtendedState(JFrame.NORMAL);
			} else {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

}

