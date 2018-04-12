package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public final class InformationPanel extends IPanel {

    private static final String s_TITLE = "Informations";

    private JTextArea m_textAreaLeft;
    private JTextArea m_textAreaRight;
    private JSplitPane m_panel;
    private ScrollComponent m_scroll;

    protected InformationPanel() {
        super(s_TITLE);
    }

    protected void createComponent() {
        m_textAreaLeft = new JTextArea();
        m_textAreaRight = new JTextArea();
        m_panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_textAreaLeft, m_textAreaRight);
        m_scroll = new ScrollComponent(m_panel);
    }

    protected void initLayout() {
    }

    protected void addComponents() {
        super.add(m_scroll);
    }

    protected void swagComponent() {
        m_textAreaLeft.setBackground(s_LIGHTGRAY);
        m_textAreaLeft.setFont(new Font(s_FONT, Font.PLAIN, 14));
        m_textAreaLeft.setForeground(s_WHITE);
        m_textAreaLeft.setEditable(false);
        m_textAreaLeft.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));

        m_textAreaRight.setBackground(s_LIGHTGRAY);
        m_textAreaRight.setFont(new Font(s_FONT, Font.PLAIN, 14));
        m_textAreaRight.setForeground(s_WHITE);
        m_textAreaRight.setEditable(false);
        m_textAreaRight.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));

        m_panel.setResizeWeight(.50d);
        m_panel.setDividerSize(0);
        m_panel.setBorder(BorderFactory.createEmptyBorder());
        m_panel.setBackground(s_LIGHTGRAY);
    }

    protected void updateInformationLeft(String _info) {
        m_textAreaLeft.setText(null);
        m_textAreaLeft.append(_info);
        repaint();
        updateUI();
    }

    protected void updateInformationRight(String _info) {
        m_textAreaRight.setText(null);
        m_textAreaRight.append(_info);
        repaint();
        updateUI();
    }

}
