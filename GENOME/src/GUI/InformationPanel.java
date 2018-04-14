package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public final class InformationPanel extends IPanel {

    private static final String s_TITLE = "Informations";

    private JComponent m_container;
    private ScrollPanel m_scroll;

    InformationPanel() {
        super(s_TITLE);
    }

    public static JComponent swagComponent(JComponent _component) {
        _component.setBackground(s_LIGHTGRAY);
        _component.setFont(new Font(s_FONT, Font.PLAIN, 14));
        _component.setForeground(s_WHITE);
        return _component;
    }

    protected void createComponent() {
        m_container = new JPanel();
        m_scroll = new ScrollPanel(m_container);
    }

    protected void initLayout() {
    }

    protected void addComponents() {
        super.add(m_scroll);
    }

    protected void swagComponent() {
        swagComponent(m_container);
    }

    void updateInformation(JComponent _component) {
        super.remove(m_scroll);
        m_container = _component;
        m_container.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
        m_scroll = new ScrollPanel(m_container);
        swagComponent();
        super.add(m_scroll);
        super.repaint();
        super.updateUI();
    }
}
