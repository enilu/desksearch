/**
 * 
 */
package org.enilu.desksearch.ui;

/**
 * @author burns
 *
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.enilu.desksearch.entity.ResultItem;
import org.enilu.desksearch.index.CreatorTask;
import org.enilu.desksearch.index.TextSearch;
import org.enilu.desksearch.utils.Contants;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 * 
 * @author Chel
 */
public class Main {
	static Logger logger = Logger.getLogger(Main.class.getName());
	private JPanel northPanel;
	private JScrollPane centerPanel;
	private JList datadirList;
	private String fieldName = "filename";

	public static void main(String[] args) throws Exception {
		new Main().init();
	}

	public void init() throws Exception {
		BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
		BeautyEyeLNFHelper.launchBeautyEyeLNF();
		UIManager.put("RootPane.setupButtonVisible", false);
		final JFrame f = new JFrame("桌面搜索工具");

		final JTable resultTable = new JTable(0, 3);// new JTable();
		logger.setLevel(Contants.log_level);

		final JTextField searchText = new JTextField("");
		searchText.setPreferredSize(new Dimension(380, 30));
		JButton searchBtn = new JButton("搜索");
		JButton createBtn = new JButton("重新构建索引");
		JButton settingBtn = new JButton("设置");
		JRadioButton filenameRadio = new JRadioButton("查文件");
		filenameRadio.setSelected(true);
		JRadioButton contentRadio = new JRadioButton("查内容");
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(filenameRadio);
		radioGroup.add(contentRadio);

		filenameRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fieldName = "filename";
			}
		});
		contentRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fieldName = "content";

			}
		});
		settingBtn.setToolTipText("配置扫描目录");

		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String keyword = searchText.getText();
				if (keyword == null || "".equals(keyword.trim())) {
					searchText.setText("请输入搜索关键字");
					return;
				}
				try {
					List<ResultItem> data = new TextSearch().query(keyword,
							fieldName);

					Vector vector2 = new Vector();
					vector2.addElement("文件名称");
					vector2.addElement("路径");
					vector2.addElement("更新日期");
					DefaultTableModel model = new DefaultTableModel();

					Vector vectorData = new Vector();
					for (ResultItem item : data) {

						Vector vector1 = new Vector();
						vector1.addElement(item.getFileName());
						vector1.addElement(item.getFilePath());
						vector1.addElement(new SimpleDateFormat(
								"yyyy-MM-dd hh:MM:ss").format(item
								.getUpdateTime()));
						vectorData.addElement(vector1);

					}
					model.setDataVector(vectorData, vector2);
					resultTable.setModel(model);
					TableColumn column1 = resultTable.getColumnModel()
							.getColumn(0);
					TableColumn column2 = resultTable.getColumnModel()
							.getColumn(1);
					TableColumn column3 = resultTable.getColumnModel()
							.getColumn(2);
					column1.setPreferredWidth(150);
					column1.setMaxWidth(150);
					column1.setMinWidth(250);
					column2.setPreferredWidth(400);
					column2.setMaxWidth(350);
					column2.setMinWidth(250);
					column3.setPreferredWidth(200);
					column3.setMaxWidth(200);
					column3.setMinWidth(250);
					// resultTable.setSize(680, 500);
					// centerPanel.add(new JScrollPane(resultTable,
					// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					// JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),
					// BorderLayout.CENTER);
					// int lastIndex = model.getRowCount();
					// Rectangle rect = resultTable.getCellRect(lastIndex,
					// model.getColumnCount(), true);
					//
					// centerPanel.getViewport().scrollRectToVisible(rect);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		// 重新构建索引
		createBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 返回0：yes，1：no
				int response = JOptionPane.showConfirmDialog(null,
						"您确认重新构建索引，构建过程耗时较长", "提醒", JOptionPane.YES_NO_OPTION);

				if (response == 0) {
					CreatorTask task = new CreatorTask();
					new Thread(task).start();

					while (!task.isFinished()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						}
					}
					searchText.enable();
					searchText.setText("生成索引完毕，请输入关键字符进行查询");
				}
			}
		});
		northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		northPanel.add(filenameRadio);
		northPanel.add(contentRadio);
		northPanel.add(searchText);
		northPanel.add(searchBtn);
		northPanel.add(createBtn);
		northPanel.add(settingBtn);

		f.add(northPanel, BorderLayout.NORTH);

		settingBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = new JOptionPane();
				Object[] message = new Object[4];
				JButton fileChooserBtn = new JButton("选择");
				String datadirs = "";
				try {
					datadirs = PropertiesUtil.getValue("datadir");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				fileChooserBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = new JFileChooser();
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						String path = null;

						int flag = fc.showOpenDialog(null);
						if (flag == JFileChooser.APPROVE_OPTION) {
							path = fc.getSelectedFile().getAbsolutePath();
							try {
								String olddatadir = PropertiesUtil
										.getValue("datadir");
								PropertiesUtil.setValue("datadir", path + "#"
										+ olddatadir);

								DefaultListModel dlm = new DefaultListModel();
								dlm.addElement(path);
								datadirList.setModel(dlm);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					}
				});
				message[0] = fileChooserBtn;
				message[1] = new JLabel("扫描的目录：");
				String data[] = datadirs.split("#");
				datadirList = new JList(data);

				message[2] = datadirList;

				String[] options = { "关闭" };
				pane.showOptionDialog(null, message, "配置扫描目录",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);
			}
		});

		TableColumn column1 = resultTable.getColumnModel().getColumn(0);
		TableColumn column2 = resultTable.getColumnModel().getColumn(1);
		TableColumn column3 = resultTable.getColumnModel().getColumn(2);
		column1.setPreferredWidth(150);
		column1.setMaxWidth(150);
		column1.setMinWidth(250);
		column2.setPreferredWidth(400);
		column2.setMaxWidth(350);
		column2.setMinWidth(250);
		column3.setPreferredWidth(200);
		column3.setMaxWidth(200);
		column3.setMinWidth(250);
		resultTable.setSize(680, 500);
		centerPanel = new JScrollPane();
		centerPanel.setSize(700, 510);
		centerPanel.add(resultTable);
		// 分别设置水平和垂直滚动条总是出现
		centerPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerPanel
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		f.add(centerPanel, BorderLayout.CENTER);

		f.setSize(800, 600);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;

		int left = (width - f.getSize().width) / 2;
		int top = (height - f.getSize().height) / 2;
		f.setLocation(left, top);
		f.setVisible(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}