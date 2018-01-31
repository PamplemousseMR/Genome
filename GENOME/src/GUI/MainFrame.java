package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.Border;

import Utils.Logs;

public class MainFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -6768656055410219611L;
	private static final String s_TITLE="BIOINFORMATIQUE ILC. Realise par Adele M. -- Arthur D. --  Florian H. -- Romain M. -- Romain T. -- Sami F. -- Vincent H.";
	private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
	private static Border s_basicEmptyBorder=BorderFactory.createEmptyBorder(20, 20, 20, 20);
	private static final Color s_DARKGRAY =new Color(32, 34, 37);
	private static final Color s_LIGHTGRAY= new Color(54, 57, 62);
	private JPanel m_north;
	private JPanel m_center;
	private JPanel m_south;
	private JPanel m_east;
	private JPanel m_west;
	private JButton m_launchDL;
	private JLabel m_titleLabel;
	private JLabel m_titleLabel2;
	private JProgressBar m_jpb; 
	private JMenuBar m_menuBar;
	private JMenu m_exit;
	private JMenu m_fullScreen;
	private JMenu m_reduce;

	/**
	 * ctor
	 */
	public MainFrame()
	{
		super(s_TITLE);
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
	private void initFrame()
	{
		this.setUndecorated(true); // Vincent : Vraiment pas fan, ça vire aussi les fonctionnalités de resize etç 
		this.setVisible(true);
		this.setSize((s_DIM.width/2),(s_DIM.height/2));
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Initialize the components
	 */
	private void initComponents()
	{
		//Creation of one panel per area
		m_north= new JPanel();
		m_center= new JPanel();
		m_south= new JPanel();
		m_east= new JPanel();
		m_west= new JPanel();

		// Components
		m_titleLabel = new JLabel("Projet de Bio-Informatique");
		m_titleLabel2 = new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");
		m_launchDL= new JButton("Demarrer le telechargement");
		m_jpb = new JProgressBar();
		m_menuBar = new JMenuBar();
		m_exit = new JMenu("X");
		m_fullScreen = new JMenu("[]");
		m_reduce = new JMenu("-");		
	}

	/**
	 * Initialize layout types
	 */
	private void initLayout()
	{
		this.setLayout(new BorderLayout());
		m_north.setLayout(new BorderLayout());
		m_south.setLayout(new BorderLayout());
	}

	/**
	 * Add components into layouts
	 */
	private void addComponents()
	{
		this.add(m_north,BorderLayout.NORTH);
		this.add(m_south,BorderLayout.SOUTH);
		this.add(m_center,BorderLayout.CENTER);
		this.add(m_east,BorderLayout.EAST);
		this.add(m_west,BorderLayout.WEST);

		// Menu Bar
		m_menuBar.add(m_reduce);
		m_menuBar.add(m_fullScreen);
		m_menuBar.add(m_exit);
		this.setJMenuBar(m_menuBar);

		// North panel
		m_north.add(m_titleLabel,BorderLayout.NORTH);
		m_north.add(m_titleLabel2,BorderLayout.CENTER);

		// West panel
		m_west.add(m_launchDL,BorderLayout.WEST);

		// South panel
		m_south.add(m_jpb,BorderLayout.CENTER);
	}

	/**
	 * Swag the interface
	 */
	private void swagComponents()
	{
		//to modify window swag,check the LookAndFeel thing

		// Panels backgrounds
		m_north.setBackground(s_DARKGRAY);     // Dark gray
		m_south.setBackground(s_LIGHTGRAY);     // Gray
		m_center.setBackground(s_LIGHTGRAY);     // Gray
		m_west.setBackground(s_LIGHTGRAY);     // Gray
		m_east.setBackground(s_LIGHTGRAY);     // Gray

		// Panels margin (top, left, bottom, right)
		m_north.setBorder(s_basicEmptyBorder);
		m_east.setBorder(s_basicEmptyBorder);
		m_west.setBorder(s_basicEmptyBorder);

		// Labels
		m_titleLabel.setFont(new Font("Helvetica", Font.PLAIN, 28));
		m_titleLabel2.setFont(new Font("Helvetica", Font.PLAIN, 18));
		m_titleLabel.setForeground(Color.WHITE);
		m_titleLabel2.setForeground(Color.LIGHT_GRAY);

		// Progress bar
		m_jpb.setStringPainted(true);
		m_jpb.setString("ProgressBar");
		m_jpb.setSize(m_south.getWidth(),( m_south.getHeight()));
		m_jpb.setBackground(new Color(46, 184, 46));     // Green

		// Buttons
		m_launchDL.setBackground(Color.LIGHT_GRAY);
		m_launchDL.setForeground(s_LIGHTGRAY);  // Light gray
		m_launchDL.setBorderPainted(false);

		// Menu Bar
		m_menuBar.setBackground(s_DARKGRAY);		

		//ugly atm, but just to know it does exist and we can modify it easily
		UIManager.put("Button.select", Color.BLUE);
	}

	/**
	 * Add listener on components
	 */
	private void addListener()
	{
		m_launchDL.addActionListener(this);

	}


	/**
	 * Manage the action to do when a listener is triggered
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if(source==this.m_launchDL)
		{
			Logs.info("Bouton cliqué");
		}

	}

}

