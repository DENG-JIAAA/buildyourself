package top.dj.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.dj.model.business.domain.entity.Company;

public interface ICompanyService extends IService<Company> {
    void updateTest();
}
