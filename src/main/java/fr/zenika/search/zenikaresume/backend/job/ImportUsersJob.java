

package fr.zenika.search.zenikaresume.backend.job;

import fr.zenika.search.zenikaresume.backend.parsing.ElasticsearchUserService;
import fr.zenika.search.zenikaresume.backend.parsing.ImportRemoteDataFromApiService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

@Service
public class ImportUsersJob extends QuartzJobBean {


	@Autowired
	private ImportRemoteDataFromApiService importRemoteDataFromApiService;

	@Autowired
	private ElasticsearchUserService elasticsearchUserService;

	private String name;

	// Invoked if a Job data map entry with that name
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		elasticsearchUserService.indexUsers(importRemoteDataFromApiService.fetchDatas());
	}

}