package org.enilu.desksearch.index;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.desksearch.utils.Contants;

public class CreatorTask implements Runnable {
	static Logger logger = Logger.getLogger(CreatorTask.class.getName());
	private boolean isFinished = false;

	public CreatorTask() {
		logger.setLevel(Contants.log_level);
	}

	@Override
	public void run() {
		try {
			TextIndexCreator.getInstance().create();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "生成索引异常", e);
		}
		isFinished = true;

	}

	public boolean isFinished() {
		return isFinished;
	}

}
