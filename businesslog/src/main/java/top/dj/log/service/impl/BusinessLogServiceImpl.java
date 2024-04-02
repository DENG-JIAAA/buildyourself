package top.dj.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dj.log.mapper.BusinessLogMapper;
import top.dj.log.service.IBusinessLogService;
import top.dj.model.businesslog.domain.entity.BusinessLog;

/**
 * @Author: DengJia
 * @Date: 2024/4/2
 * @Description:
 */

@Service
public class BusinessLogServiceImpl extends ServiceImpl<BusinessLogMapper, BusinessLog> implements IBusinessLogService {

}
