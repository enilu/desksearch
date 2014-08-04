package org.enilu.desksearch.index;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.enilu.desksearch.ui.PropertiesUtil;
import org.enilu.desksearch.utils.Contants;

/**
 * 将指定目录下的txt文本文件生成索引
 * 
 * @author zhangtao
 * 
 */
public class TextIndexCreator {
	static Logger logger = Logger.getLogger(TextIndexCreator.class.getName());
	// 索引计数器
	private int count = 0;
	private static IndexWriter indexWriter;
	private static TextIndexCreator instance;

	private TextIndexCreator() {
		logger.setLevel(Contants.log_level);
	}

	public static TextIndexCreator getInstance() throws Exception {
		if (instance == null) {
			instance = new TextIndexCreator();
			logger.info("生成索引路径" + Contants.indexDir);
			indexWriter = new IndexWriter(FSDirectory.open(new File(
					Contants.indexDir)),
					new StandardAnalyzer(Version.LUCENE_30),
					IndexWriter.MaxFieldLength.LIMITED);
		}
		return instance;
	}

	/**
	 * 生成索引
	 * 
	 * @throws Exception
	 */
	public void create() throws Exception {
		// 删除所有索引
		indexWriter.deleteAll();
		// 重建所有索引
		String dataDirs[] = PropertiesUtil.getValue("datadir").toString()
				.split("#");
		for (String dataDir : dataDirs) {
			this.createIndex(new File(dataDir));

		}
		indexWriter.optimize();
		indexWriter.close();
	}

	/**
	 * 根据指定的文件/目录生成索引
	 * 
	 * @param file
	 * @throws Exception
	 */
	private void createIndex(File file) throws Exception {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File child : files) {
				createIndex(child);
			}
		} else {
			// count++;
			// if (count == 1000) {
			// logger.log(Level.INFO, "提交索引更改，重新打开索引继续添加");
			// indexWriter.optimize();
			// indexWriter.close();
			// indexWriter = new IndexWriter(FSDirectory.open(new File(
			// Contants.indexDir)), new StandardAnalyzer(
			// Version.LUCENE_30), IndexWriter.MaxFieldLength.LIMITED);
			// }
			String fileName = file.getName();

			String extName = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			Document doc = new Document();
			logger.log(Level.INFO, "索引：" + file.getAbsolutePath());
			long id = new Date().getTime();
			if ("txt".equals(extName) || "properties".equals(extName)
					|| "java".equals(extName) || "jsp".equals(extName)) {

				doc.add(new Field("id", String.valueOf(id), Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("filename", file.getName(), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new Field("filepath", file.getAbsolutePath(),
						Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", IOUtils.toString(
						new FileInputStream(file), "UTF-8"), Field.Store.NO,
						Field.Index.ANALYZED));
				doc.add(new NumericField("inputdate", Field.Store.YES, true)
						.setLongValue(id));

			} else {

				doc.add(new Field("id", String.valueOf(id), Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("filename", file.getName(), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new Field("filepath", file.getAbsolutePath(),
						Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", file.getName(), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new NumericField("inputdate", Field.Store.YES, true)
						.setLongValue(id));
			}
			indexWriter.addDocument(doc);
		}
	}

}
