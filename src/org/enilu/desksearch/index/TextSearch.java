package org.enilu.desksearch.index;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.enilu.desksearch.entity.ResultItem;
import org.enilu.desksearch.utils.Contants;

/**
 * 将指定目录下的txt文本文件生成索引
 * 
 * @author zhangtao
 * 
 */
public class TextSearch {
	static Logger logger = Logger.getLogger(TextSearch.class.getName());

	public TextSearch() {
		logger.setLevel(Contants.log_level);
	}

	/**
	 * 根据制定的关键字查询内容匹配的项目
	 * 
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public List<ResultItem> query(String keyword, String fieldName)
			throws Exception {
		IndexSearcher searcher = new IndexSearcher(FSDirectory.open(new File(
				Contants.indexDir)), true);
		searcher.setDefaultFieldSortScoring(true, false);
		QueryParser parser = new QueryParser(Version.LUCENE_30, fieldName,
				new StandardAnalyzer(Version.LUCENE_30));
		Query query = parser.parse(keyword);
		org.apache.lucene.search.Sort sort = new org.apache.lucene.search.Sort(
				new SortField("updatetime", SortField.LONG, true));
		TopDocs result = searcher.search(query, null, Integer.MAX_VALUE, sort);

		ScoreDoc[] docs = result.scoreDocs;
		System.out.println(docs.length);
		List<ResultItem> list = new ArrayList<ResultItem>();
		for (int i = 0; i < (docs.length > 50 ? 50 : docs.length); i++) {
			Document doc = searcher.doc(docs[i].doc);
			ResultItem item = new ResultItem();
			item.setId(doc.getField("id").stringValue());
			item.setFileName(doc.getField("filename").stringValue());
			item.setFilePath(doc.getField("filepath").stringValue());
			item.setUpdateTime(new Date(Long.valueOf(doc.getField("inputdate")
					.stringValue())));
			list.add(item);
			logger.info(item.toString());

		}
		return list;

	}

}
