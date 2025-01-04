package com.simbest.cloud.cores.sys.service;



import com.simbest.cloud.cores.base.service.ILogicService;
import com.simbest.cloud.cores.sys.model.SysDict;

import java.util.List;

public interface ISysDictService extends ILogicService<SysDict, String> {

    SysDict findByDictType(String dictType);

    List<SysDict> findByParentId(String parentId);

    List<SysDict> findByEnabled(Boolean enabled);

}
