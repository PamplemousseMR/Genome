package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.s_BLUEGRAY;
import static GUI.Constant.s_CHARCOAL;

public final class ActivityPanel extends JPanel {


    private JPanel m_container;
    private ButtonComponent m_start;
    private ButtonComponent m_pause;
    private ButtonComponent m_resume;
    private ButtonComponent m_stop;

    protected ActivityPanel() {
        createComponent();
        initLayout();
        addComponents();
        swagComponent();
    }

    protected void createComponent() {
        m_container = new JPanel();
        m_start = new ButtonComponent("Ressources/play.png");
        m_pause = new ButtonComponent("Ressources/pause.png");
        m_resume = new ButtonComponent("Ressources/resume.png");
        m_stop = new ButtonComponent("Ressources/stop.png");
    }

    protected void initLayout() {
        this.setLayout(new BorderLayout());
        m_container.setLayout(new GridLayout(1, 2));
    }

    protected void addComponents() {
        m_container.add(m_start);
        m_container.add(m_stop);
        m_container.setBackground(s_BLUEGRAY);
        super.add(m_container);
    }

    protected void swagComponent() {
        super.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
    }

    /**
     * Add start action
     *
     * @param _activityListener the start action
     */
    protected void addtStartListener(ActivityListener _activityListener) {
        m_start.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_start);
                m_container.add(m_pause, 0);
                m_container.revalidate();
                m_container.repaint();
                m_pause.resetColor();
            }
        });
    }

    /**
     * Add stop action
     *
     * @param _activityListener the stop action
     */
    protected void addStopListener(ActivityListener _activityListener) {
        m_stop.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(0);
                m_container.add(m_start, 0);
                m_container.revalidate();
                m_container.repaint();
                m_start.resetColor();
            }
        });
    }

    /**
     * Add pause action
     *
     * @param _activityListener the pause action
     */
    protected void addPauseListener(ActivityListener _activityListener) {
        m_pause.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_pause);
                m_container.add(m_resume, 0);
                m_container.revalidate();
                m_container.repaint();
                m_resume.resetColor();
            }
        });
    }

    /**
     * Add resume action
     *
     * @param _activityListener the resume action
     */
    protected void addResumeListener(ActivityListener _activityListener) {
        m_resume.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_resume);
                m_container.add(m_pause, 0);
                m_container.revalidate();
                m_container.repaint();
                m_pause.resetColor();
            }
        });
    }

    public interface ActivityListener {
        boolean activityEvent();
    }
}
