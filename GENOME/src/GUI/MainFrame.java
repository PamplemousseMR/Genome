package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -6768656055410219611L;
	private static String s_TITLE="BIOINFORMATIQUE ILC. Realise par Arthur D. -- Adele M. -- Florian H. -- Romain M. -- Romain T. -- Sami F. -- Vincent H.";
	private JPanel m_north;
	private JPanel m_center;
	private JPanel m_south;
	private JPanel m_east;
	private JPanel m_west;
	private JButton m_launchDL;
	private JLabel m_titleLabel;
	private JLabel m_titleLabel2;
	private JProgressBar m_jpb; //Vector de progress bar si on veut en mettre plusieurs
	private JMenuBar m_menuBar;
	private JMenu m_exit;
	private JMenu m_fullScreen;
	private JMenu m_reduce;

	public MainFrame()
	{
		super(s_TITLE);
		initFrame();
		initComponents();
		initLayout();
		addComponents();
		swagComponents();
	}

	private void initFrame()
	{
		//Sans doute possible de detecter la taille de l'ecran et d'adapter la taille de la fenetre en fonction
		this.setUndecorated(true);
		this.setVisible(true);
		this.setSize(1000,800);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents()
	{
		//Creation d'un Panel par area
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

	// Adapter les Layout des differents Panel si besoin.
	private void initLayout()
	{
		this.setLayout(new BorderLayout());
		m_north.setLayout(new BorderLayout());
		m_south.setLayout(new BorderLayout());
	}

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

	// Apparence des composantes de l'interface
	private void swagComponents()
	{
		// Panels backgrounds
		m_north.setBackground(new Color(32, 34, 37));     // Dark gray
		m_south.setBackground(new Color(54, 57, 62));     // Gray
		m_center.setBackground(new Color(54, 57, 62));     // Gray
		m_west.setBackground(new Color(54, 57, 62));     // Gray
		m_east.setBackground(new Color(54, 57, 62));     // Gray

		// Panels margin (top, left, bottom, right)
		m_north.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		m_east.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		m_west.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
		m_launchDL.setForeground(new Color(32, 34, 37));  // Light gray
		m_launchDL.setBorderPainted(false);

		// Menu Bar
		m_menuBar.setBackground(new Color(32, 34, 37));
	}
}