package com.example.demo.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.models.DBQueueItem;

@Mapper
public interface JobMapper {

    // Insert queries
    @Insert("INSERT INTO queue (job_name, build_number, time_in_queue, url, day,week,month,year) " +
            "VALUES (#{queueItem.jobName}, #{queueItem.jobBuildNumber},#{queueItem.timeInQueue},#{queueItem.jobBuildURL},#{queueItem.day},#{queueItem.week},#{queueItem.month},#{queueItem.year})")
    void insertJobIntoBD(@Param("queueItem") DBQueueItem queueItem);

    // Select queries
    // Get is job exists in DB
    @Select("SELECT COUNT(*) FROM queue WHERE url = #{queueItem.jobBuildNumber}")
    int queueItemExists(@Param("jobBuildNumber") String jobBuildNumber);

    // Get job from jobBuildURL
    @Select("SELECT job_name as jobName, build_number as jobBuildNumber, url as jobBuildURL, " +
            "time_in_queue as timeInQueue, day as day , week as week , month as month , year as year " +
            " FROM queue WHERE url = #{jobBuildNumber}")
    DBQueueItem getJob(@Param("jobBuildNumber") String jobBuildNumber);

    // Update queries
    // Update job time
    @Update("UPDATE queue SET time_in_queue = #{time} WHERE url=#{url}")
    void updateJob(@Param("url") String buildURL, @Param("time") Long time);

}
