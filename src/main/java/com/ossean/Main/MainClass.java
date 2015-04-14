package com.ossean.Main;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ossean.databaseSource.SourceDao;
import com.ossean.databaseTarget.TargetDao;
import com.ossean.model.RelativeMemo_ExtractResult;
import com.ossean.model.RelativeMemo_OsseanProduction;


@Component
public class MainClass {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TargetDao targetDao;
	@Resource
	private SourceDao sourceDao;
	
	private static String source = "relative_memos";
	private static String target = "relative_memos";
	private static String pointerTable = "pointers";
	private static String sourceTableName = "relative_memos_extract_result";
	private static String targetTableName = "relative_memos_ossean_production";
	private static int startId = 1;
	private static int batchSize = 500;
	
	
	public int readPointer(String table, String source, String target){
		int pointer = 1;
		try {
			pointer = sourceDao.getPointer(table, source, target);
		} catch(Exception e) {
			logger.info("No such pointer! Create one");
			sourceDao.insertPointer(table, source, target, pointer);
		}
		return pointer;
	}
	
	public void start(){
		while(true){
			startId = readPointer(pointerTable, sourceTableName, targetTableName);
			List<RelativeMemo_OsseanProduction> relativeMemos_op = targetDao.getByBatch(target, startId, batchSize);
			if(relativeMemos_op.size() == 0){
				logger.info("nothing to handle! Sleep 3600s");
				try {
					Thread.sleep(48*3600*1000);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(RelativeMemo_OsseanProduction rm_op:relativeMemos_op){
				logger.info("handle item :" + rm_op.getId());
				RelativeMemo_ExtractResult result = sourceDao.getById(source, rm_op.getId());
				if(result != null){
					RelativeMemo_ExtractResult rm_er = result;
					logger.info("author_url: " + rm_er.getAuthor_url() + "      " + "url_md5: " + rm_er.getUrl_md5());
					rm_op.setAuthor_url(rm_er.getAuthor_url());
					rm_op.setUrl_md5(rm_er.getUrl_md5());
					
					//更新数据库
					targetDao.updateItem(target, rm_op);
					
				}else{
					logger.info("no such Item in source table :" + rm_op.getId());
				}
				//更新指针
				sourceDao.updatePointer(pointerTable, sourceTableName, targetTableName, rm_op.getId() + 1);
			}
		}
	}
	
	public static void main(String[] args){

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");
		MainClass mainClass = applicationContext.getBean(MainClass.class);
		mainClass.start();
	}

}
