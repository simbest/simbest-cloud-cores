package com.simbest.cloud.cores.sys.repository;


import com.simbest.cloud.cores.base.repository.LogicRepository;
import com.simbest.cloud.cores.sys.model.SysFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFileRepository extends LogicRepository<SysFile, String> {



	/**
	 * 通过单据id获取到表单
	 *
	 * @param pmInstId 单据id
	 * @return MeetingFormModel
	 */
	@Query(
		value = "select t.* from SYS_FILE t where t.pm_ins_id = :pmInstId and t.enabled = 1 order by t.created_time ",
		nativeQuery = true
	)
	List<SysFile> getFilesByPmInsId(@Param("pmInstId") String pmInstId);

	/**
	 * 通过流程id获取主单据id
	 *
	 * @param processInsId 单据id
	 * @return String
	 */
	@Query(
		value = "select t.receipt_code from ACT_BUSINESS_STATUS t where t.process_inst_id =:processInsId and t.enabled = 1 ",
		nativeQuery = true
	)
	String getPmInsIdByProcessInsId(@Param("processInsId") String processInsId);
}
