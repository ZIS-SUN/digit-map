package com.specimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.dto.SpecimenDTO;
import com.specimen.dto.SpecimenQueryRequest;
import com.specimen.entity.MapMarker;
import com.specimen.entity.Specimen;
import com.specimen.entity.User;
import com.specimen.mapper.MapMarkerMapper;
import com.specimen.mapper.SpecimenMapper;
import com.specimen.mapper.UserMapper;
import com.specimen.service.SpecimenService;
import com.specimen.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标本服务实现类
 */
@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenMapper specimenMapper;
    private final MapMarkerMapper mapMarkerMapper;
    private final UserMapper userMapper;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public Specimen addSpecimen(Specimen specimen, MultipartFile imageFile) {
        // 生成标本编号
        specimen.setSpecimenNo("SP" + System.currentTimeMillis());
        specimen.setStatus("已入库");

        // 上传图片
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = fileUtil.saveFile(imageFile);
                specimen.setImageUrl(fileUtil.getFileUrl(filename));
            } catch (IOException e) {
                throw new RuntimeException("图片上传失败", e);
            }
        }

        // 插入标本
        specimenMapper.insert(specimen);

        // 创建地图标注
        if (specimen.getLongitude() != null && specimen.getLatitude() != null) {
            MapMarker marker = new MapMarker();
            marker.setSpecimenId(specimen.getId());
            marker.setLongitude(specimen.getLongitude());
            marker.setLatitude(specimen.getLatitude());
            marker.setLabel(specimen.getName());
            marker.setMarkerColor("#4CAF50");
            mapMarkerMapper.insert(marker);
        }

        return specimen;
    }

    @Override
    @Transactional
    public Specimen updateSpecimen(Long id, Specimen specimen, MultipartFile imageFile) {
        Specimen existingSpecimen = specimenMapper.selectById(id);
        if (existingSpecimen == null) {
            throw new RuntimeException("标本不存在");
        }

        // 上传新图片
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = fileUtil.saveFile(imageFile);
                specimen.setImageUrl(fileUtil.getFileUrl(filename));
            } catch (IOException e) {
                throw new RuntimeException("图片上传失败", e);
            }
        }

        specimen.setId(id);
        specimenMapper.updateById(specimen);

        // 更新地图标注
        if (specimen.getLongitude() != null && specimen.getLatitude() != null) {
            LambdaQueryWrapper<MapMarker> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MapMarker::getSpecimenId, id);
            MapMarker marker = mapMarkerMapper.selectOne(wrapper);

            if (marker == null) {
                marker = new MapMarker();
                marker.setSpecimenId(id);
            }

            marker.setLongitude(specimen.getLongitude());
            marker.setLatitude(specimen.getLatitude());
            marker.setLabel(specimen.getName());

            if (marker.getId() == null) {
                mapMarkerMapper.insert(marker);
            } else {
                mapMarkerMapper.updateById(marker);
            }
        }

        return specimen;
    }

    @Override
    @Transactional
    public void deleteSpecimen(Long id) {
        Specimen specimen = specimenMapper.selectById(id);
        if (specimen == null) {
            throw new RuntimeException("标本不存在");
        }

        // 删除标本
        specimenMapper.deleteById(id);

        // 删除地图标注（由外键级联删除）
    }

    @Override
    public SpecimenDTO getSpecimenById(Long id) {
        Specimen specimen = specimenMapper.selectById(id);
        if (specimen == null) {
            throw new RuntimeException("标本不存在");
        }

        return convertToDTO(specimen);
    }

    @Override
    public Page<SpecimenDTO> querySpecimens(SpecimenQueryRequest request) {
        LambdaQueryWrapper<Specimen> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(request.getName())) {
            wrapper.like(Specimen::getName, request.getName());
        }
        if (StringUtils.hasText(request.getFamily())) {
            wrapper.eq(Specimen::getFamily, request.getFamily());
        }
        if (StringUtils.hasText(request.getGenus())) {
            wrapper.eq(Specimen::getGenus, request.getGenus());
        }
        if (StringUtils.hasText(request.getLocation())) {
            wrapper.like(Specimen::getLocation, request.getLocation());
        }
        if (request.getStartDate() != null) {
            wrapper.ge(Specimen::getCollectionDate, request.getStartDate());
        }
        if (request.getEndDate() != null) {
            wrapper.le(Specimen::getCollectionDate, request.getEndDate());
        }
        if (StringUtils.hasText(request.getStatus())) {
            wrapper.eq(Specimen::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(Specimen::getCreateTime);

        // 分页查询
        Page<Specimen> page = new Page<>(request.getPage(), request.getSize());
        Page<Specimen> result = specimenMapper.selectPage(page, wrapper);

        // 转换为DTO
        Page<SpecimenDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<SpecimenDTO> dtoList = result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);

        return dtoPage;
    }

    @Override
    public byte[] exportSpecimens(SpecimenQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Specimen> wrapper = new LambdaQueryWrapper<>();

        if (request != null) {
            // 添加查询条件
            if (StringUtils.hasText(request.getName())) {
                wrapper.like(Specimen::getName, request.getName());
            }
            if (StringUtils.hasText(request.getFamily())) {
                wrapper.eq(Specimen::getFamily, request.getFamily());
            }
            if (StringUtils.hasText(request.getGenus())) {
                wrapper.eq(Specimen::getGenus, request.getGenus());
            }
            if (StringUtils.hasText(request.getLocation())) {
                wrapper.like(Specimen::getLocation, request.getLocation());
            }
            if (request.getStartDate() != null) {
                wrapper.ge(Specimen::getCollectionDate, request.getStartDate());
            }
            if (request.getEndDate() != null) {
                wrapper.le(Specimen::getCollectionDate, request.getEndDate());
            }
            if (StringUtils.hasText(request.getStatus())) {
                wrapper.eq(Specimen::getStatus, request.getStatus());
            }
        }

        wrapper.orderByDesc(Specimen::getCreateTime);

        // 查询数据
        List<Specimen> specimens = specimenMapper.selectList(wrapper);

        // 生成CSV
        StringBuilder csv = new StringBuilder();
        // 添加BOM以支持中文
        csv.append("\uFEFF");
        csv.append("标本编号,名称,拉丁名,科名,属名,采集地点,经度,纬度,采集日期,采集人,状态,描述\n");

        for (Specimen specimen : specimens) {
            // 获取采集人姓名
            String collectorName = "";
            if (specimen.getCollectorId() != null) {
                User collector = userMapper.selectById(specimen.getCollectorId());
                if (collector != null) {
                    collectorName = collector.getRealName() != null ? collector.getRealName() : collector.getUsername();
                }
            }

            csv.append(escapeCSV(specimen.getSpecimenNo())).append(",")
               .append(escapeCSV(specimen.getName())).append(",")
               .append(escapeCSV(specimen.getLatinName())).append(",")
               .append(escapeCSV(specimen.getFamily())).append(",")
               .append(escapeCSV(specimen.getGenus())).append(",")
               .append(escapeCSV(specimen.getLocation())).append(",")
               .append(specimen.getLongitude() != null ? specimen.getLongitude() : "").append(",")
               .append(specimen.getLatitude() != null ? specimen.getLatitude() : "").append(",")
               .append(specimen.getCollectionDate() != null ? specimen.getCollectionDate() : "").append(",")
               .append(escapeCSV(collectorName)).append(",")
               .append(escapeCSV(specimen.getStatus())).append(",")
               .append(escapeCSV(specimen.getDescription())).append("\n");
        }

        try {
            return csv.toString().getBytes("UTF-8");
        } catch (Exception e) {
            return csv.toString().getBytes();
        }
    }

    /**
     * CSV字段转义，处理包含逗号和引号的字段
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // 如果包含逗号、引号或换行符，需要用引号包围，并将引号转义为双引号
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private SpecimenDTO convertToDTO(Specimen specimen) {
        SpecimenDTO dto = new SpecimenDTO();
        BeanUtils.copyProperties(specimen, dto);

        // 获取采集人姓名
        if (specimen.getCollectorId() != null) {
            User collector = userMapper.selectById(specimen.getCollectorId());
            if (collector != null) {
                dto.setCollectorName(collector.getRealName() != null ? collector.getRealName() : collector.getUsername());
            }
        }

        return dto;
    }
}
