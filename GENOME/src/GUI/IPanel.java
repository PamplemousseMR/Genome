package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public abstract class IPanel extends JPanel {

    protected IPanel(String _title) {
        final TitlePanel titlePanel = new TitlePanel(_title, 20, s_BLUEGRAY);
        this.setLayout(new BorderLayout());
        this.add(titlePanel, BorderLayout.NORTH);
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
