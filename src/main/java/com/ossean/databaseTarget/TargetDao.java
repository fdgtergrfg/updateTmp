package com.ossean.databaseTarget;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ossean.model.RelativeMemo_OsseanProduction;

public interface TargetDao {

	@Select("select * from ${table} where id>=#{start} limit #{size}")
	public List<RelativeMemo_OsseanProduction> getByBatch(@Param("table") String table, @Param("start") int start, @Param("size") int size);
	
	
	@Update("update ${table} set author_url=#{item.author_url},url_md5=#{item.url_md5} where id=#{item.id}")
	public void updateItem(@Param("table") String table, @Param("item") RelativeMemo_OsseanProduction item);
}
