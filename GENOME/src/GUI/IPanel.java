package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public abstract class IPanel extends JPanel {

    private final TitlePanel m_titlePanel;

    protected IPanel(String _title) {
        m_titlePanel = new TitlePanel(_title, 20, s_WHITE, s_BLUEGRAY);
        this.setLayout(new BorderLayout());
        this.add(m_titlePanel, BorderLayout.NORTH);
        this.setBackground(s_LIGHTGRAY);
        createComponent();
        initLayout();
        addComponents();
        swagComponent();
    }

    protected abstract void createComponent();

    protected abstract void initLayout();

    protected abstract void addComponents();

    protected abstract void swagComponent();

}
