package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ResizibleFrame extends JFrame implements MouseMotionListener, MouseListener {

    private static final String s_TITLE = "GENOME";
    private static final Toolkit s_TOOLKIT = Toolkit.getDefaultToolkit();
    private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int s_DEFAULT_FRAME_WIDTH = 300;
    private static final int s_DEFAULT_FRAME_HEIGHT = 300;
    private static final Point s_INITIAL_LOCATION = new Point((int) s_TOOLKIT.getScreenSize().getWidth() / 2 - s_DEFAULT_FRAME_WIDTH / 2, (int) s_TOOLKIT.getScreenSize().getHeight() / 2 - s_DEFAULT_FRAME_HEIGHT / 2);
    private static final Dimension s_INITIAL_DIMENSION = new Dimension(s_DEFAULT_FRAME_WIDTH, s_DEFAULT_FRAME_HEIGHT);

    /**
     * Area inside which the action listener detects the mouse
     **/
    private static final int s_CURSOR_AREA = 6;
    /**
     * Vertical threshold
     **/
    private static final int s_DIFF_MIN_WIDTH = 30;
    /**
     * Horizontal threshold
     **/
    private static final int s_DIFF_MIN_HEIGHT = 30;

    /**
     * Screen environment
     **/
    private final Point m_initialLocation;
    private int m_minWidth;
    private int m_minHeight;
    private Point m_start_drag;
    private Point m_start_loc;

    protected ResizibleFrame() {
        super(s_TITLE);
        m_initialLocation = s_INITIAL_LOCATION;
        m_minWidth = (int) s_INITIAL_DIMENSION.getWidth();
        m_minHeight = (int) s_INITIAL_DIMENSION.getHeight();
        Init();
    }

    private static Point getScreenLocation(MouseEvent e, JFrame frame) {
        final Point cursor = e.getPoint();
        final Point view_location = frame.getLocationOnScreen();
        return new Point((int) (view_location.getX() + cursor.getX()), (int) (view_location.getY() + cursor.getY()));
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        moveOrFullResizeFrame(e);
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        final Point cursorLocation = e.getPoint();
        final int xPos = cursorLocation.x;
        final int yPos = cursorLocation.y;

        if (xPos >= s_CURSOR_AREA && xPos <= getWidth() - s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos >= s_CURSOR_AREA && yPos <= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos >= s_CURSOR_AREA && yPos <= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        else if (xPos >= s_CURSOR_AREA && xPos <= getWidth() - s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        else
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        final Object sourceObject = e.getSource();
        if (sourceObject instanceof JPanel) {
            if (e.getClickCount() == 2) {
                if (getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))
                    doubleClicktoFullScreen();
            }
        }
    }

    /**
     * Drag and drop the Frame
     */
    @Override
    public void mousePressed(MouseEvent e) {
        m_start_drag = getScreenLocation(e, this);
        m_start_loc = getLocation();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    private void Init() {
        addMouseMotionListener(this);
        addMouseListener(this);
        setSize(m_minWidth, m_minHeight);
        m_minWidth -= s_DIFF_MIN_WIDTH;
        m_minHeight -= s_DIFF_MIN_HEIGHT;
        setLocation(m_initialLocation);
        setUndecorated(true);
        setSize((s_DIM.width / 2), (s_DIM.height / 2));
    }

    private void moveOrFullResizeFrame(MouseEvent e) {
        final Object sourceObject = e.getSource();
        final Point current = getScreenLocation(e, this);
        final Point offset = new Point((int) current.getX() - (int) m_start_drag.getX(), (int) current.getY() - (int) m_start_drag.getY());

        if (sourceObject instanceof JPanel
                && getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))
            setLocation((int) (m_start_loc.getX() + offset.getX()), (int) (m_start_loc.getY() + offset.getY()));
        else if (!getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))) {
            final int oldLocationX = (int) getLocation().getX();
            final int oldLocationY = (int) getLocation().getY();
            int newLocationX = (int) (m_start_loc.getX() + offset.getX());
            int newLocationY = (int) (m_start_loc.getY() + offset.getY());
            final boolean N_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            final boolean NE_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            final boolean NW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            final boolean E_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            final boolean W_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            final boolean S_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            final boolean SW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            boolean setLocation = false;
            int newWidth = e.getX();
            int newHeight = e.getY();

            if (NE_Resize) {
                newHeight = getHeight() - (newLocationY - oldLocationY);
                newLocationX = (int) getLocation().getX();
                setLocation = true;
            } else if (E_Resize) {
                newHeight = getHeight();
            } else if (S_Resize) {
                newWidth = getWidth();
            } else if (N_Resize) {
                newLocationX = (int) getLocation().getX();
                newWidth = getWidth();
                newHeight = getHeight() - (newLocationY - oldLocationY);
                setLocation = true;
            } else if (NW_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newHeight = getHeight() - (newLocationY - oldLocationY);
                setLocation = true;
            } else if (SW_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newLocationY = (int) getLocation().getY();
                setLocation = true;
            }

            if (W_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newLocationY = (int) getLocation().getY();
                newHeight = getHeight();
                setLocation = true;
            }

            if (newWidth >= (int) s_TOOLKIT.getScreenSize().getWidth() || newWidth <= m_minWidth) {
                newLocationX = oldLocationX;
                newWidth = getWidth();
            }

            if (newHeight >= (int) s_TOOLKIT.getScreenSize().getHeight() - 30 || newHeight <= m_minHeight) {
                newLocationY = oldLocationY;
                newHeight = getHeight();
            }

            if (newWidth != getWidth() || newHeight != getHeight()) {
                setSize(newWidth, newHeight);
                if (setLocation) {
                    setLocation(newLocationX, newLocationY);
                }
            }
        }
    }

    /**
     * Double-Click to fullscreen the Frame orto go back to the previous size of the frame
     */
    private void doubleClicktoFullScreen() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}
	 