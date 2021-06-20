/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.LeaseMode;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.LeaseModeRepository;
import me.zhengjie.modules.system.service.LeaseModeService;
import me.zhengjie.modules.system.service.dto.LeaseModeDto;
import me.zhengjie.modules.system.service.dto.LeaseModeQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.LeaseModeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author lease
* @date 2021-06-19
**/
@Service
@RequiredArgsConstructor
public class LeaseModeServiceImpl implements LeaseModeService {

    private final LeaseModeRepository leaseModeRepository;
    private final LeaseModeMapper leaseModeMapper;

    @Override
    public Map<String,Object> queryAll(LeaseModeQueryCriteria criteria, Pageable pageable){
        Page<LeaseMode> page = leaseModeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(leaseModeMapper::toDto));
    }

    @Override
    public List<LeaseModeDto> queryAll(LeaseModeQueryCriteria criteria){
        return leaseModeMapper.toDto(leaseModeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LeaseModeDto findById(Integer modeId) {
        LeaseMode leaseMode = leaseModeRepository.findById(modeId).orElseGet(LeaseMode::new);
        ValidationUtil.isNull(leaseMode.getModeId(),"LeaseMode","modeId",modeId);
        return leaseModeMapper.toDto(leaseMode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeaseModeDto create(LeaseMode resources) {
        return leaseModeMapper.toDto(leaseModeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LeaseMode resources) {
        LeaseMode leaseMode = leaseModeRepository.findById(resources.getModeId()).orElseGet(LeaseMode::new);
        ValidationUtil.isNull( leaseMode.getModeId(),"LeaseMode","id",resources.getModeId());
        leaseMode.copy(resources);
        leaseModeRepository.save(leaseMode);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer modeId : ids) {
            leaseModeRepository.deleteById(modeId);
        }
    }

    @Override
    public void download(List<LeaseModeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeaseModeDto leaseMode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" modeName",  leaseMode.getModeName());
            map.put(" modeDescribe",  leaseMode.getModeDescribe());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}