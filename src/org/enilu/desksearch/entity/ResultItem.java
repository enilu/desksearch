package org.enilu.desksearch.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询结果项
 * 
 * @author zhangtao
 * 
 */
public class ResultItem implements Serializable {

	private String id;// 唯一标识
	private Date updateTime;// 索引生成时间
	private String fileName;// 文件名称
	private String filePath;// 文件路径

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {

		return new StringBuilder("ResultItem:\r\nid:").append(id)
				.append("\r\nfileName:").append(fileName)
				.append("\r\nfilePath:").append(filePath)
				.append("\r\nindex update time:").append(updateTime.toString())
				.toString();
	}

}
