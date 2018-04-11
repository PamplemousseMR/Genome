package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public final class InformationPanel extends IPanel {

    private static final String s_TITLE = "Informations";

    private JTextArea m_textArea;
    private ScrollComponent m_container;

    protected InformationPanel() {
        super(s_TITLE);
    }

    protected void createComponent() {
        m_textArea = new JTextArea();
        m_container = new ScrollComponent(m_textArea);
    }

    protected void initLayout() {
    }

    protected void addComponents() {
        super.add(m_container);
    }

    protected void swagComponent() {
        m_textArea.setBackground(s_LIGHTGRAY);
        m_textArea.setFont(new Font(s_FONT, Font.PLAIN, 14));
        m_textArea.setForeground(s_WHITE);
        m_textArea.setEditable(false);
    }

}
