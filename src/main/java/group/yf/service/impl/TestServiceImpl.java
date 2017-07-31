package group.yf.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.yf.service.TestService;

/**
 * @author Yang Fei
 *
 */
@Service
public class TestServiceImpl implements TestService{

	Logger log=Logger.getLogger(this.getClass());
	@Autowired
	private group.yf.dao.TestMapper testMapper;
	
	@Override
	public int add() {
		log.info("service");
		return testMapper.add();
	}
	
}
