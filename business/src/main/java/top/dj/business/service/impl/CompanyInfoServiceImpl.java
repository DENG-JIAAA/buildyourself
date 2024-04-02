package top.dj.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dj.business.mapper.CompanyInfoMapper;
import top.dj.business.service.ICompanyInfoService;
import top.dj.model.business.domain.entity.CompanyInfo;

/**
 * @Author: DengJia
 * @Date: 2024/4/2
 * @Description:
 */

@Service
@Transactional
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements ICompanyInfoService {

}
