package top.dj.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dj.business.bootstrap.properties.BusinessLogProperties;
import top.dj.business.mapper.CompanyMapper;
import top.dj.business.service.ICompanyService;
import top.dj.model.business.domain.entity.Company;

/**
 * @Author: DengJia
 * @Date: 2024/4/2
 * @Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {
    private final BusinessLogProperties businessLogProperties;

    @Override
    public void updateTest() {

    }
}
