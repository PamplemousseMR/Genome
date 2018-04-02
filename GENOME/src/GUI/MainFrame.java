package GUI;

import Utils.Logs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public final class MainFrame extends ResizibleFrame {

	private static final String s_TITLE = "GENOME";
	private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
	protected static final Color s_DARKGRAY = new Color(32, 34, 37);
	protected static final Color s_LIGHTGRAY = new Color(54, 57, 62);
	protected static final Color s_BLUEGRAY = new Color(62, 68, 83);
	private static final Insets s_INSETS = new Insets(3, 3, 3, 3);
	private static final Toolkit s_TOOLKIT = Toolkit.getDefaultToolkit();
	private static final int s_DEFAULT_FRAME_WIDTH = 300;
	private static final int s_DEFAULT_FRAME_HEIGHT = 300;
	private static final Point s_INITIAL_LOCATION = new Point((int) s_TOOLKIT.getScreenSize().getWidth() / 2 - s_DEFAULT_FRAME_WIDTH / 2, (int) s_TOOLKIT.getScreenSize().getHeight() / 2 - s_DEFAULT_FRAME_HEIGHT / 2);
	private static final Dimension s_INITIAL_DIMENSION = new Dimension(s_DEFAULT_FRAME_WIDTH, s_DEFAULT_FRAME_HEIGHT);
	private static final String s_FONT = "Helvetica";
	private static MainFrame s_mainFrame;

	private JPanel m_menuPanel;
	private JSplitPane m_splitPanel_main;
	private JSplitPane m_splitPanel_right;
	private JScrollPane m_treeContainer;
	private JPanel m_north;
	private JPanel m_center;
	private JPanel m_fileTreePanel;
	private JPanel m_informationsPanel;
	private JPanel m_logsPanel;
	private JPanel m_footerPanel;

	private JPanel m_fileTreeTitlePanel;
	private JPanel m_informationTitlePanel;
	private JPanel m_logsTitlePanel;
	private JPanel m_footerTitlePanel;

	private JLabel m_mainTitle;
	private JLabel m_secondTitle;
	private JLabel m_logsTitle;
	private JLabel m_informationTitle;
	private JLabel m_treeTitle;
	private JLabel m_footerTitle;

	private JButton m_closeB;
	private JButton m_maximizeB;
	private JButton m_minimizeB;

	private DBTree m_dbTree;
	private JButton m_launchDL;

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
		m_dbTree.update(_path);
	}

	/**
	 * Add download action
	 *
	 * @param _actionListener the download action
	 */
	public void addDownloadAction(ActionListener _actionListener) {
		m_launchDL.addActionListener(_actionListener);
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
		m_menuPanel = new JPanel();
		m_north = new JPanel();
		m_center = new JPanel();

		m_fileTreePanel = new JPanel();
		m_informationsPanel = new JPanel();
		m_logsPanel = new JPanel();
		m_footerPanel = new JPanel();
		m_fileTreeTitlePanel = new JPanel();
        m_informationTitlePanel = new JPanel();
        m_logsTitlePanel = new JPanel();
        m_footerTitlePanel = new JPanel();

        m_dbTree = new DBTree();
        m_treeContainer = new JScrollPane(m_dbTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        m_splitPanel_right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_informationsPanel, m_logsPanel);
        m_splitPanel_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_fileTreePanel, m_splitPanel_right);

        m_mainTitle = new JLabel("   Projet de Bio-Informatique");
        m_secondTitle = new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");
        m_informationTitle = new JLabel("Informations");
        m_treeTitle = new JLabel("Arborescence des fichiers");
        m_logsTitle = new JLabel("Logs");
        m_footerTitle = new JLabel("Application cree par -- Adele M. -- Arthur D. -- Florian H. -- Romain M. -- Romain T. -- Sami F. -- Vincent H.");
        m_launchDL = new JButton("Demarrer le telechargement");

        m_closeB = new JButton();
        m_maximizeB = new JButton();
        m_minimizeB = new JButton();

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
		m_informationsPanel.setLayout(new BorderLayout());
		m_logsPanel.setLayout(new BorderLayout());
		m_footerPanel.setLayout(new BorderLayout());
	}

	/**
	 * Add components into layouts
	 */
	private void addComponents() {
		add(m_north, BorderLayout.NORTH);
		add(m_center, BorderLayout.CENTER);
		add(m_footerPanel, BorderLayout.SOUTH);

		m_menuPanel.add(m_minimizeB);
		m_menuPanel.add(m_maximizeB);
		m_menuPanel.add(m_closeB);

		m_center.add(m_splitPanel_main, BorderLayout.CENTER);

		m_north.add(m_menuPanel, BorderLayout.NORTH);
		m_north.add(m_mainTitle, BorderLayout.CENTER);
		m_north.add(m_secondTitle, BorderLayout.SOUTH);

		m_fileTreeTitlePanel.add(m_treeTitle, BorderLayout.CENTER);
		m_fileTreePanel.add(m_fileTreeTitlePanel, BorderLayout.NORTH);

		m_fileTreePanel.add(m_treeContainer, BorderLayout.CENTER);
		m_fileTreePanel.add(m_launchDL, BorderLayout.SOUTH);

		m_informationTitlePanel.add(m_informationTitle, BorderLayout.CENTER);
		m_informationsPanel.add(m_informationTitlePanel, BorderLayout.NORTH);

		m_logsTitlePanel.add(m_logsTitle, BorderLayout.CENTER);
		m_logsPanel.add(m_logsTitlePanel, BorderLayout.NORTH);

		m_informationTitlePanel.add(m_informationTitle, BorderLayout.CENTER);
		m_informationsPanel.add(m_informationTitlePanel, BorderLayout.NORTH);
	}

	/**
	 * Swag the interface
	 */
	private void swagComponents() {
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		m_splitPanel_right.setResizeWeight(.80d);
		m_splitPanel_main.setResizeWeight(.15d);
		m_splitPanel_right.setDividerSize(3);
		m_splitPanel_main.setDividerSize(3);

		m_splitPanel_main.setBorder(null);
		m_splitPanel_right.setBorder(null);
		m_informationsPanel.setBorder(null);


		m_menuPanel.setBackground(s_DARKGRAY);
		m_north.setBackground(s_DARKGRAY);	 // Dark gray
		m_center.setBackground(s_LIGHTGRAY);	 // Gray
		m_fileTreePanel.setBackground(s_LIGHTGRAY);
		
		m_treeContainer.setOpaque(false);
		m_treeContainer.getViewport().setBackground(s_LIGHTGRAY);
		m_treeContainer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		m_treeContainer.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
		m_treeContainer.getVerticalScrollBar().setAutoscrolls(true);
		m_treeContainer.getVerticalScrollBar().setBackground(s_BLUEGRAY);
		
		m_informationsPanel.setBackground(s_LIGHTGRAY);
		m_logsPanel.setBackground(s_LIGHTGRAY);

		m_footerTitlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		m_footerTitlePanel.setBackground(new Color(51, 54, 63));

		m_fileTreeTitlePanel.setBackground(s_BLUEGRAY);
		m_informationTitlePanel.setBackground(s_BLUEGRAY);
		m_logsTitlePanel.setBackground(s_BLUEGRAY);

		m_mainTitle.setFont(new Font(s_FONT, Font.PLAIN, 28));
		m_secondTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
		m_mainTitle.setForeground(Color.WHITE);
		m_secondTitle.setForeground(Color.LIGHT_GRAY);
		m_secondTitle.setBorder(BorderFactory.createEmptyBorder(0, 25, 5, 5));
		m_logsTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
		m_informationTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
		m_treeTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
		m_logsTitle.setForeground(Color.WHITE);
		m_informationTitle.setForeground(Color.WHITE);
		m_treeTitle.setForeground(Color.WHITE);

		m_footerTitle.setFont(new Font(s_FONT, Font.PLAIN, 11));
		m_footerTitle.setForeground(Color.WHITE);

		m_launchDL.setBackground(Color.LIGHT_GRAY);
		m_launchDL.setForeground(s_LIGHTGRAY);  // Light gray
		m_launchDL.setBorderPainted(false);

		m_menuPanel.setPreferredSize(new Dimension(s_DEFAULT_FRAME_WIDTH, 35));

		swagButton(m_closeB, "Ressources/close.png");
		swagButton(m_minimizeB, "Ressources/minimize.png");
		swagButton(m_maximizeB, "Ressources/maximize.png");
	}

	/**
	 * Swag button
	 *
	 * @param _button button to swag
	 * @param _path   path to the icon
	 */
	private void swagButton(JButton _button, String _path) {
		_button.setMargin(s_INSETS);
		_button.setBackground(s_DARKGRAY);
		_button.setForeground(Color.WHITE);
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

