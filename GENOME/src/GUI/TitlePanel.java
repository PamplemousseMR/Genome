package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.s_FONT;
import static GUI.Constant.s_WHITE;

final class TitlePanel extends JPanel {

    private final JLabel m_title;
    private final int m_size;
    private final Color m_background;

    /**
     * Class constructor
     *
     * @param _title      the title
     * @param _size       the font size
     * @param _background the background color
     */
    TitlePanel(String _title, int _size, Color _background) {
        super();

        m_title = new JLabel(_title);
        m_size = _size;
        m_background = _background;

        initLayout();
        addComponents();
        swagComponent();
    }

    /**
     * Init the layouts of the panel
     */
    private void initLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        m_title.setLayout(new GridBagLayout());
    }

    /**
     * Add the components into the panel
     */
    private void addComponents() {
        this.add(Box.createHorizontalGlue());
        this.add(m_title, BorderLayout.CENTER);
        this.add(Box.createHorizontalGlue());
    }

    /**
     * Make components of the panel pretty
     */
    private void swagComponent() {
        this.setBackground(m_background);
        this.setBorder(BorderFactory.createEmptyBorder(m_size / 2, m_size / 2, m_size / 2, m_size / 2));
        m_title.setForeground(s_WHITE);
        m_title.setFont(new Font(s_FONT, Font.PLAIN, m_size));
    }

}
