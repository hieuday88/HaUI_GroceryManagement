package util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class ButtonHover {
	public static void addButtonHover(JButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setOpaque(true);
				btn.setBackground(Color.LIGHT_GRAY);
				btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setOpaque(false);
				btn.setBackground(Color.white);
				btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
}
