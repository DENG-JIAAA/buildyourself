package top.dj.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.dj.model.businesslog.domain.entity.BusinessLog;

public interface BusinessLogMapper extends BaseMapper<BusinessLog> {
}
