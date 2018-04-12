package GUI;

import Utils.Logs;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

import static GUI.Constant.*;

public final class LogsPanel extends IPanel {

    private final static String s_TITLE = "Logs";

    private JTextArea m_textArea;
    private ScrollComponent m_container;

    protected LogsPanel() {
        super(s_TITLE);
    }

    protected void createComponent() {
        m_textArea = new JTextArea("", 1, 250); //1 column, 250 rows
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

    protected void updateLog(String _log, Logs.Type _type) {
        m_textArea.append("\n " + _log);
        while(m_textArea.getLineCount() > 100) //print only the last 100 lines
        {
            try {
                m_textArea.replaceRange("", m_textArea.getLineStartOffset(0), m_textArea.getLineStartOffset(1));
            } catch (BadLocationException e) {
                Logs.exception(e);
            }
        }
    }

}
