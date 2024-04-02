package top.dj.business.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import top.dj.business.BusinessApplication;
import top.dj.model.business.domain.entity.CompanyInfo;

@SpringBootTest(classes = BusinessApplication.class) // 更改：启动类.class
@Slf4j
class CompanyInfoServiceTest {

    @Autowired
    private ICompanyInfoService companyInfoService;

    @Test
    void springBootTest_202404211269() {
        UpdateWrapper<CompanyInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", 99657571579429867L);
        wrapper.set("short_name", "宜康有限公司1");
        boolean update = companyInfoService.update(wrapper);
        assert update;
    }

}
