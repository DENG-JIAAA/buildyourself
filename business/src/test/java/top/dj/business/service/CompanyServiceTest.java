package top.dj.business.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.dj.business.BusinessApplication;
import top.dj.business.bootstrap.properties.BusinessLogProperties;
import top.dj.model.business.domain.entity.Company;

@SpringBootTest(classes = BusinessApplication.class)
@Slf4j
class CompanyServiceTest {

    @Autowired
    private ICompanyService companyInfoService;

    @Autowired
    private BusinessLogProperties businessLogProperties;

    @Test
    void springBootTest_202404211269() {
        UpdateWrapper<Company> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", 99657571579429867L);
        wrapper.set("short_name", "宜康有限公司1");
        companyInfoService.update(wrapper);
    }

}
