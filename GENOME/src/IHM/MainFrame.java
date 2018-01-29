package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -6768656055410219611L;
	private static String s_TITLE="BIOINFORMATIQUE ILC. Realise par Arthur D. -- Adele M. -- Florian H. -- Romain M. -- Romain T. -- Sami . -- Vincent H.";
	private JPanel m_north;
	private JPanel m_center;
	private JPanel m_south;
	private JPanel m_east;
	private JPanel m_west;
	private JButton m_launchDL;
	private JLabel m_titleLabel;
	private JLabel m_titleLabel2;
	private JProgressBar m_jpb; //Vector de progress bar si on veut en mettre plusieurs

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
		//Sans doute possible de detecter la taille de l'ecran et d'adapter e la taille de la fenetre en fonction
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
		m_titleLabel=new JLabel("BIG DATA EN BIOINFORMATIQUE");
		m_titleLabel2=new JLabel("Statistiques sur les trinucleotides dans les genes de la base GenBank");
		m_launchDL= new JButton("Download");
		m_jpb = new JProgressBar();
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
		m_north.add(m_titleLabel,BorderLayout.NORTH);
		m_north.add(m_titleLabel2,BorderLayout.CENTER);
		m_north.add(m_launchDL,BorderLayout.EAST);
		m_south.add(m_jpb,BorderLayout.CENTER);
	}

	// Apparence des composantes de l'interface
	private void swagComponents() 
	{
		m_titleLabel.setFont(new Font("Serif", Font.PLAIN, 28));
		m_titleLabel2.setFont(new Font("Serif", Font.PLAIN, 18));
		m_titleLabel.setForeground(Color.DARK_GRAY);
		m_titleLabel2.setForeground(Color.LIGHT_GRAY);
		m_jpb.setStringPainted(true);
		m_jpb.setString("ProgressBar");
		m_jpb.setSize(m_south.getWidth(),( m_south.getHeight()));
		m_jpb.setBackground(Color.CYAN);
		m_east.setBackground(Color.LIGHT_GRAY);
		m_south.setBackground(Color.GRAY);
	}
}

