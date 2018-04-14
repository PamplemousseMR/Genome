package GUI;

import Utils.Logs;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import static GUI.Constant.*;

public final class LogsPanel extends IPanel {

    private final static String s_TITLE = "Logs";

    private JTextPane m_textPane;
    private ScrollPanel m_container;

    protected LogsPanel() {
        super(s_TITLE);
    }

    protected void createComponent() {
        m_textPane = new JTextPane();
        m_container = new ScrollPanel(m_textPane);
        new SmartScrollComponent(m_container);
    }

    protected void initLayout() {
    }

    protected void addComponents() {
        super.add(m_container);
    }

    protected void swagComponent() {
        m_textPane.setBackground(s_LIGHTGRAY);
        m_textPane.setFont(new Font(s_FONT, Font.PLAIN, 14));
        m_textPane.setForeground(s_WHITE);
        m_textPane.setEditable(false);
        m_textPane.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
    }

    protected void updateLog(String _log, Logs.Type _type) {
        Color m_logColour;
        switch (_type) {
            case WARNING:
                m_logColour = s_ORANGE;
                break;
            case EXCEPTION:
                m_logColour = s_RED;
                break;
            default:
                m_logColour = s_WHITE;
                break;
        }
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, m_logColour);
        int len = m_textPane.getDocument().getLength();
        m_textPane.setCharacterAttributes(aset, true);
        try {
            m_textPane.getDocument().insertString(len, _log +"\n", aset);
        } catch (BadLocationException e) {
            Logs.exception(e);
        }
        if (m_textPane.getText().split("\n").length > 1000) {
            try {
                m_textPane.getDocument().remove(0, len/	20);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

    }
}
