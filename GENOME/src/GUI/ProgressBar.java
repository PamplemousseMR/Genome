package GUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

import static GUI.Constant.*;

public final class ProgressBar extends JProgressBar {

    protected ProgressBar() {
        super();
        super.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionForeground() {
                return s_DARKGRAY;
            }

            @Override
            protected Color getSelectionBackground() {
                return s_BLUE;
            }
        });
        super.setStringPainted(true);
        super.setForeground(s_GREEN);
        super.setBackground(s_BLUEGRAY);
        super.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        super.setValue(super.getMaximum());
    }

}
