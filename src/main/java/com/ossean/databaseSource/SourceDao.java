package com.ossean.databaseSource;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ossean.model.RelativeMemo_ExtractResult;

public interface SourceDao {
	
	@Select("select Pointer from ${table} where SourceTableName=#{SourceTableName} and TargetTableName=#{TargetTableName}")
	public int getPointer(@Param("table") String table, @Param("SourceTableName") String SourceTableName, @Param("TargetTableName") String TargetTableName);
	
	@Insert("insert into ${table} values (null,#{SourceTableName},#{TargetTableName},#{Pointer})")
	public void insertPointer(@Param("table") String table, @Param("SourceTableName") String SourceTableName, @Param("TargetTableName") String TargetTableName, @Param("Pointer") int Pointer);

	@Update("update ${table} set Pointer=#{Pointer} where SourceTableName=#{SourceTableName} and TargetTableName=#{TargetTableName}")
	public void updatePointer(@Param("table") String table, @Param("SourceTableName") String SourceTableName, @Param("TargetTableName") String TargetTableName, @Param("Pointer") int Pointer);
	
	@Select("select * from ${table} where author=#{author} and url=#{url}")
	public List<RelativeMemo_ExtractResult> getByAuthorAndUrl(@Param("table") String table, @Param("author") String author, @Param("url") String url);
	

}
