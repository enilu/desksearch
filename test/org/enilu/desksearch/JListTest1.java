package org.enilu.desksearch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

final public class JListTest1 extends JFrame {
	private JList jlist;
	private JTextField text;
	private JScrollPane scrollPane;
	private DefaultListModel listModel = new DefaultListModel();

	public JListTest1() {
		jlist = new JList(listModel);
		jlist.setCellRenderer(new ListRenderer());
		add(text = new JTextField(), BorderLayout.SOUTH);
		add(scrollPane = new JScrollPane(jlist), BorderLayout.CENTER);
		text.addKeyListener(new TextKeyAdapter());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
	}

	private class TextKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				listModel.addElement(text.getText());
				scrollToView();
			}
		}
	}

	private class ListRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			return this;
		}

		public Insets getInsets() {
			return new Insets(0, 0, 0, 0);
		}
	}

	private void scrollToView() {
		int lastIndex = listModel.getSize();
		Rectangle rect = jlist.getCellBounds(lastIndex - 1, lastIndex - 1);
		scrollPane.getViewport().scrollRectToVisible(rect);
	}

	public static void main(String args[]) {
		new JListTest1();
	}
}