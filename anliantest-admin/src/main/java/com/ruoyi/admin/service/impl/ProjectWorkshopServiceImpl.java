package com.ruoyi.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ProjectFormWorkshopDto;
import com.ruoyi.admin.domain.dto.ProjectWorkshopDto;
import com.ruoyi.admin.domain.dto.ProjectWorkshopFromDto;
import com.ruoyi.admin.entity.Point;
import com.ruoyi.admin.entity.ProjectWorkshop;
import com.ruoyi.admin.mapper.ProjectWorkshopMapper;
import com.ruoyi.admin.service.PointService;
import com.ruoyi.admin.service.ProjectWorkshopService;
import com.ruoyi.admin.util.WorkshopMenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhanghao
 * @date 2023-06-08
 * @desc : 项目车间岗位
 */
@Service
public class ProjectWorkshopServiceImpl extends ServiceImpl<ProjectWorkshopMapper, ProjectWorkshop> implements ProjectWorkshopService {
    private final PointService pointService;

    @Autowired
    public ProjectWorkshopServiceImpl(PointService pointService) {
        this.pointService = pointService;
    }

    /**
     * 项目车间岗位点位列表
     *
     * @param projectWorkshopDto
     * @return
     */
    @Override
    public List<ProjectWorkshopDto> pointList(ProjectWorkshopDto projectWorkshopDto) {
//        //该项目车间岗位
//        List<ProjectWorkshop> list1 = this.list(new LambdaQueryWrapper<ProjectWorkshop>()
//                .eq(ProjectWorkshop::getProjectId, projectWorkshopDto.getProjectId())
//        );
//
//        //该项目车间岗位无数据同步之前报价车间岗位
//        if(CollUtil.isEmpty(list1)){
//
//        }


        //车间岗位列表
        List<ProjectWorkshop> list = this.list(new LambdaQueryWrapper<ProjectWorkshop>()
                .eq(ProjectWorkshop::getPlanId, projectWorkshopDto.getPlanId())
        );


        if (CollUtil.isNotEmpty(list)) {
            List<ProjectWorkshopDto> projectWorkshopDtos = BeanUtil.copyToList(list, ProjectWorkshopDto.class);
            List<Long> workshopIds = list.stream().map(ProjectWorkshop::getId).collect(Collectors.toList());

            //点位数据
            List<Point> list1 = pointService.list(new LambdaQueryWrapper<Point>()
                    .in(Point::getWorkshopId, workshopIds)
            );
            if (CollUtil.isNotEmpty(list1)) {
                Map<Long, List<Point>> collect = list1.stream().collect(Collectors.groupingBy(Point::getWorkshopId));
                for (ProjectWorkshopDto projectWorkshopDto1 : projectWorkshopDtos
                ) {
                    projectWorkshopDto1.setPointList(collect.get(projectWorkshopDto1.getId()) != null ? collect.get(projectWorkshopDto1.getId()) : new ArrayList<>());
                }
            }

            WorkshopMenuTree workshopMenuTree = new WorkshopMenuTree(projectWorkshopDtos);
            List<ProjectWorkshopDto> projectWorkshopDtos1 = workshopMenuTree.builTree();


            return projectWorkshopDtos1;
        }
        return new ArrayList<ProjectWorkshopDto>();
    }

    /**
     * 项目车间岗位点位列表
     *
     * @param planId
     * @return
     */
    @Override
    public List<ProjectFormWorkshopDto> pointFormList(Long planId) {
        List<ProjectFormWorkshopDto> projectFormWorkshopDtos = baseMapper.pointFormList(planId);

        return projectFormWorkshopDtos;
    }


    /**
     * 删除车间岗位表格列表
     *
     * @param projectWorkshopDto
     */
    @Override
    @Transactional
    public void removeProjectWorkshop(ProjectWorkshopDto projectWorkshopDto) {
        Long id = projectWorkshopDto.getId();
        ArrayList<Long> longs1 = new ArrayList<>();
        longs1.add(id);
        //当前选择车间id数据及子级id
        List<Long> longs = childLevel(longs1);
        longs.add(id);
        //删除车间岗位数据
        this.removeByIds(longs);

        //关联删除点位信息
        pointService.remove(new LambdaQueryWrapper<Point>().in(Point::getWorkshopId, longs));

        //   重新生成点位编号
        pointService.initializationPoint(projectWorkshopDto.getPlanId());
    }

//    /**
//     * 选择车间岗位
//     *
//     * @param planId
//     * @return
//     */
//    @Override
//    public List<ProjectWorkshopDto> selectProjectWorkshop(Long planId) {
//        //只选到岗位的
//        List<ProjectWorkshop> list = this.list(new LambdaQueryWrapper<ProjectWorkshop>()
//                .eq(ProjectWorkshop::getPlanId, planId)
//                .eq(ProjectWorkshop::getType, 2)
//        );
//
//        if (CollUtil.isNotEmpty(list)) {
//
//            List<Long> collect = list.stream().map(ProjectWorkshop::getPid).collect(Collectors.toList());
//            //递归取父级id
//            List<Long> longs = fatherIds(collect);
//            //查询所有数据 转为树状结构
//            List<ProjectWorkshop> list1 = this.listByIds(longs);
//
//            List<ProjectWorkshopDto> projectWorkshopDtos = BeanUtil.copyToList(list1, ProjectWorkshopDto.class);
//            WorkshopMenuTree workshopMenuTree = new WorkshopMenuTree(projectWorkshopDtos);
//            List<ProjectWorkshopDto> projectWorkshopDtos1 = workshopMenuTree.builTree();
//            return projectWorkshopDtos1;
//        }
//        return new ArrayList<ProjectWorkshopDto>();
//    }


    /**
     * 递归查询子级id
     *
     * @param ids
     * @return 子级及本身id
     */
    private List<Long> childLevel(List<Long> ids) {
        List<Long> longs1 = new ArrayList<>();
//        longs1.addAll(ids);

        List<Long> longs = new ArrayList<>();
        longs.addAll(ids);
        while (true) {

            List<ProjectWorkshop> list = this.list(new LambdaQueryWrapper<ProjectWorkshop>()
                    .in(ProjectWorkshop::getPid, longs));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> collect = list.stream().map(ProjectWorkshop::getId).collect(Collectors.toList());

                longs = collect;
                longs1.addAll(collect);
            } else {
                break;
            }

        }

        return longs1;

    }


    /**
     * 递归查询父级id
     *
     * @param ids
     * @return 子级及本身id
     */
    @Override
    public List<Long> fatherIds(List<Long> ids) {
        //ids即为父级的ids
        List<Long> longs1 = new ArrayList<>();
        longs1.addAll(ids);

        //pid用于递归查询  本级的pid
        List<Long> longs = new ArrayList<>();
        longs.addAll(ids);
        //id ->pid  pid再作为id去拿值  每次id作为存储  pid用于递归查询
        while (true) {
            //pid用于递归查询
            List<ProjectWorkshop> list = this.list(new LambdaQueryWrapper<ProjectWorkshop>()
                    .in(ProjectWorkshop::getId, longs));
            if (CollUtil.isNotEmpty(list)) {
                //pid用于递归查询
                List<Long> collectPid = list.stream().map(ProjectWorkshop::getPid).collect(Collectors.toList());
                longs = collectPid;

                //每次id作为存储
                List<Long> collect = list.stream().map(ProjectWorkshop::getId).collect(Collectors.toList());
                longs1.addAll(collect);
            } else {
                break;
            }

        }

        return longs1;

    }


}