package org.xhtmlrenderer.demo.browser;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.swing.BasicPanel;

public class SelectionMouseListener implements MouseListener, MouseMotionListener {

    protected BasicPanel panel = null;

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getComponent() instanceof BasicPanel) {
            panel = (BasicPanel) e.getComponent();
            panel.getSharedContext().clearSelection();
            Box box = panel.find(e);
            if (box == null) {
                return;
            }
            // if box is text node then start selection
            /*
            if (box instanceof InlineBox) {
                int x = BoxFinder.findBoxX(panel, e.getX(), e.getY());
                panel.getSharedContext().setSelectionStart(box, x);
                panel.getSharedContext().setSelectionEnd(box, x + 1);
                panel.repaint();
            }
            */
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (panel != null) {
            panel.repaint();
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getComponent() instanceof BasicPanel) {
            panel = (BasicPanel) e.getComponent();
            Box box = panel.find(e);
            if (box == null) {
                return;
            }
            // if box is text node then start selection
            /*
            if ((box instanceof InlineTextBox)) {
                int x = BoxFinder.findBoxX(panel, e.getX(), e.getY());
                panel.getSharedContext().setSelectionEnd(box, x);
                panel.repaint();
            }
            */
        }
    }

    public void mouseMoved(MouseEvent e) {
    }


}

