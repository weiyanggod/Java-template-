package com.example.app.service;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.app.config.GenerateCode;
import com.example.app.config.GlobalProperties;
import com.example.app.config.RandomPwd;
import com.example.app.mapper.*;
import com.example.app.model.dto.LoginDTO;
import com.example.app.model.dto.ProjectDTO;
import com.example.app.model.entity.*;
import com.example.app.model.vo.UserVO;
import com.example.app.utils.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AdminImp {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuoteMapper quoteMapper;

    @Autowired
    PriceMapper priceMapper;
    /**
     * 登录
     *
     * @param login
     * @return
     */
    public UserEntity Login(LoginDTO login) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username", login.getUsername());
        objectQueryWrapper.eq("password", SecureUtil.md5(login.getPassword()));
        UserEntity object = adminMapper.selectOne(objectQueryWrapper);
        return object;
    }
    /**
     * 新增项目
     *
     * @param projectDTO
     */
    public void addProject(ProjectDTO projectDTO) {

        String newEquipmentNo = null;

        List<ProjectEntity> projectList = projectMapper.selectList(null);

        if (projectList.size() > 0) {
            ProjectEntity project = projectList.get(projectList.size() - 1);
            newEquipmentNo = GenerateCode.getNewEquipmentNo("XM", project.getCode().split("XM")[1]);
        } else {
            newEquipmentNo = GenerateCode.getNewEquipmentNo("XM", null);
        }

        // 设置项目
        String projectId = Number.getRandomId();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setCode(newEquipmentNo);
        projectEntity.setName(projectDTO.getName());
        projectEntity.setStatus("未开始");
        projectEntity.setStartTime(projectDTO.getStartTime());
        projectEntity.setBidPrice(projectDTO.getBidPrice());
        projectEntity.setValue(projectDTO.getValue());
        projectEntity.setValueDetails(projectDTO.getValueDetails());
        projectEntity.setRound(projectDTO.getRound());
        projectEntity.setDuration(projectDTO.getDuration());
        projectEntity.setCreateName(GlobalProperties.getUserEntity().getUsername());
        projectEntity.setCreatId(GlobalProperties.getUserEntity().getId());
        projectEntity.setCreatedTime(DateUtil.date().toLocalDateTime());

        projectMapper.insert(projectEntity);

        List<SupplierEntity> supplierList = projectDTO.getSupplierList();

        UserEntity userEntity = new UserEntity();

        // 设置供应商信息
        for (SupplierEntity supplierEntity : supplierList) {

            String password = RandomPwd.getRandomPwd(10);

            String supplierId = Number.getRandomId();

            supplierEntity.setId(supplierId);
            supplierEntity.setProjectName(projectDTO.getName());
            supplierEntity.setProjectId(projectId);
            supplierEntity.setUsername(supplierEntity.getContact());
            supplierEntity.setAccountType("供应商");
            supplierEntity.setPassword(password);
            supplierMapper.insert(supplierEntity);

            // 添加供应商账号
            userEntity.setId(supplierId);
            userEntity.setName(supplierEntity.getName());
            userEntity.setUsername(supplierEntity.getContact());
            userEntity.setPassword(SecureUtil.md5(password));
            userEntity.setAccountType("供应商");
            userMapper.insert(userEntity);

        }

        // 创建轮次
        Integer round = projectDTO.getRound();

        for (Integer i = 0; i < round; i++) {
            QuoteEntity quoteEntity = new QuoteEntity();
            String quoteId = Number.getRandomId();

            quoteEntity.setId(quoteId);
            quoteEntity.setProjectId(projectId);
            quoteEntity.setStatus("未开始");
            quoteEntity.setRound(i + 1);
            quoteEntity.setLeftTime(projectDTO.getDuration());
            quoteMapper.insert(quoteEntity);

        }

    }

    /**
     * 修改项目
     */
    public void editProject(ProjectDTO projectDTO) {

        UpdateWrapper<Object> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id", projectDTO.getId());
        updateWrapper.set("name", projectDTO.getName());
        updateWrapper.set("startTime", projectDTO.getStartTime());
        updateWrapper.set("bidPrice", projectDTO.getBidPrice());
        updateWrapper.set("duration", projectDTO.getDuration());
        updateWrapper.set("round", projectDTO.getRound());
        updateWrapper.set("value", projectDTO.getValue());
        updateWrapper.set("valueDetails", projectDTO.getValueDetails());

        List<SupplierEntity> supplierList = projectDTO.getSupplierList();

        // 获取前端提交的所有ID（需保留的ID）
        Set<String> frontendIds = supplierList.stream()
                .map(SupplierEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 项目供应商id列表
        List<String> supplierIds = supplierMapper.getIds(projectDTO.getId());

        Set<String> idsToDelete = supplierIds.stream()
                .filter(id -> !frontendIds.contains(id))
                .collect(Collectors.toSet());

        if (!idsToDelete.isEmpty()) {
            // 如果供应商被删除了则删除对应的供应商和供应商账号
            supplierMapper.deleteByIds(idsToDelete);
            userMapper.deleteByIds(idsToDelete);
        }

        // 重新创建
        if (projectDTO.getSupplierList() != null) {

            for (SupplierEntity supplier : supplierList) {
                // 如果没有id就新增,否则就修改
                if (supplier.getId() == null) {

                    UserEntity userEntity = new UserEntity();

                    String password = RandomPwd.getRandomPwd(10);

                    String supplierId = Number.getRandomId();

                    supplier.setId(supplierId);
                    supplier.setProjectName(projectDTO.getName());
                    supplier.setProjectId(projectDTO.getId());
                    supplier.setUsername(supplier.getContact());
                    supplier.setAccountType("供应商");
                    supplier.setPassword(password);
                    supplierMapper.insert(supplier);

                    // 添加供应商账号
                    userEntity.setId(supplierId);
                    userEntity.setName(supplier.getName());
                    userEntity.setUsername(supplier.getContact());
                    userEntity.setPassword(SecureUtil.md5(password));
                    userEntity.setAccountType("供应商");
                    userMapper.insert(userEntity);

                } else {
                    // 更新
                    UpdateWrapper<SupplierEntity> updateSupplierWrapper = new UpdateWrapper<>();
                    updateSupplierWrapper.eq("id", supplier.getId());

                    supplierMapper.update(supplier, updateSupplierWrapper);
                }
            }

        }

    }
    /**
     * 获取用户信息
     */
    public UserVO getUserInfo() {
        String id = GlobalProperties.getUserEntity().getId();
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        UserEntity user = userMapper.selectOne(queryWrapper);
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setName(user.getName());
        userVO.setUsername(user.getUsername());
        userVO.setAccountType(user.getAccountType());
        return userVO;
    }

    // 获取项目列表
    public List<ProjectEntity> getProjectList() {
        return projectMapper.selectList(null);
    }

    // 获取项目详情(采购员)
    public ProjectEntity getProjectDetailsByPurchaseId(String projectId, String purchaseId) {
        QueryWrapper<ProjectEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("creat_id", purchaseId);
        objectQueryWrapper.eq("id", projectId);
        ProjectEntity projectEntity = projectMapper.selectOne(objectQueryWrapper);

        List<SupplierEntity> supplierEntityList = supplierMapper.selectList(new QueryWrapper<SupplierEntity>().eq("project_id", projectEntity.getId()));

        List<QuoteEntity> quoteList = quoteMapper.selectList(new QueryWrapper<QuoteEntity>().eq("project_id", projectEntity.getId()).orderByAsc("round"));

        for (QuoteEntity quote : quoteList) {
            List<PriceEntity> priceList = priceMapper.selectList(new QueryWrapper<PriceEntity>().eq("round", quote.getRound()));
            quote.setPriceList(priceList);
        }

        projectEntity.setSupplierList(supplierEntityList);
        projectEntity.setQuoteList(quoteList);

        return projectEntity;
    }
    // 获取项目详情(供应商)
    public ProjectEntity getProjectDetailsBySupplierId(String projectId, String supplierId) {

        SupplierEntity supplierEntity = supplierMapper.selectOne(new QueryWrapper<SupplierEntity>().eq("id", supplierId).eq("id", projectId));

        ProjectEntity project = projectMapper.selectOne(new QueryWrapper<ProjectEntity>().eq("id", supplierEntity.getProjectId()));

        List<QuoteEntity> quoteList = getQuoteList(supplierEntity.getProjectId());

        supplierEntity.setQuoteList(quoteList);

        // 循环轮次并添加每轮的报价
        for (QuoteEntity quote : quoteList) {
            PriceEntity price = getPrice(supplierEntity.getId(), quote.getRound());
            List<PriceEntity> list = new ArrayList<>();

            // 判断是否有值
            if (price != null) {
                list.add(price);
            } else {
                quote.setPriceList(null);
            }

            quote.setPriceList(list);
        }

        project.setQuoteList(quoteList);

        return project;

    }

    // 获取轮次
    public List<QuoteEntity> getQuoteList(String projectId) {
        return quoteMapper.selectList(new QueryWrapper<QuoteEntity>().eq("project_id", projectId).orderByAsc("round"));
    }

    // 设置报价
    public void setPrice(PriceEntity priceEntity) {

        SupplierEntity supplier = supplierMapper.selectOne(new QueryWrapper<SupplierEntity>().eq("id", priceEntity.getSupplierId()));

        PriceEntity oldPriceEntity = priceMapper.selectOne(new QueryWrapper<PriceEntity>().eq("supplier_id", priceEntity.getSupplierId()));

        if (oldPriceEntity == null) {
            priceEntity.setId(Number.getRandomId());
            priceEntity.setEditTime(DateUtil.date().toLocalDateTime());
            priceEntity.setSupplierName(supplier.getName());
            priceMapper.insert(priceEntity);
        } else {
            UpdateWrapper<PriceEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", oldPriceEntity.getId());
            updateWrapper.set("number", priceEntity.getNumber());
            updateWrapper.set("edit_time", DateUtil.date().toLocalDateTime());
            priceMapper.update(updateWrapper);
        }

    }

    // 获取报价
    public PriceEntity getPrice(String supplierId, Integer round) {
        PriceEntity priceEntity = priceMapper.selectOne(new QueryWrapper<PriceEntity>().eq("supplier_id", supplierId).eq("round", round));

        if (priceEntity == null) {
            return null;
        }

        return priceEntity;
    }

}
