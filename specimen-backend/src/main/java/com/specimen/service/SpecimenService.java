package com.specimen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.dto.SpecimenDTO;
import com.specimen.dto.SpecimenQueryRequest;
import com.specimen.entity.Specimen;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 标本服务接口
 */
public interface SpecimenService {

    /**
     * 添加标本
     */
    Specimen addSpecimen(Specimen specimen, MultipartFile imageFile);

    /**
     * 更新标本
     */
    Specimen updateSpecimen(Long id, Specimen specimen, MultipartFile imageFile);

    /**
     * 删除标本
     */
    void deleteSpecimen(Long id);

    /**
     * 查询标本详情
     */
    SpecimenDTO getSpecimenById(Long id);

    /**
     * 分页查询标本
     */
    Page<SpecimenDTO> querySpecimens(SpecimenQueryRequest request);

    /**
     * 导出标本数据
     */
    byte[] exportSpecimens(SpecimenQueryRequest request);
}
