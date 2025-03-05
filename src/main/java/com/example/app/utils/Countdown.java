package com.example.app.utils;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.app.mapper.ProjectMapper;
import com.example.app.mapper.QuoteMapper;
import com.example.app.model.entity.ProjectEntity;
import com.example.app.model.entity.QuoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

// 定时更改状态和当前轮次
@EnableScheduling
@Component
public class Countdown {

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    QuoteMapper quoteMapper;

    @Scheduled(fixedRate = 1000) // 每秒执行一次
    public void checkProjectStatus() {
        // 未开始项目列表
        List<ProjectEntity> projectList = projectMapper.selectList(null);

        if (!projectList.isEmpty()) {
            for (ProjectEntity projectEntity : projectList) {

                if (Objects.equals(projectEntity.getStatus(), "未开始")) {
                    String startTime = projectEntity.getStartTime();

                    if (Objects.equals(startTime, DateUtil.now())) {
                        // 到时间更新项目的状态
                        UpdateWrapper<ProjectEntity> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", projectEntity.getId());
                        updateWrapper.set("status", "进行中");
                        projectMapper.update(updateWrapper);

                        // 更新项目下轮次为1的数据
                        quoteMapper.update(new UpdateWrapper<QuoteEntity>().eq("project_id", projectEntity.getId()).eq("round", 1).set("status", "进行中"));
                    }

                } else if (Objects.equals(projectEntity.getStatus(), "进行中")) {
                    List<QuoteEntity> quoteList = quoteMapper.selectList(new QueryWrapper<QuoteEntity>().eq("project_id", projectEntity.getId()).orderByAsc("round"));
                    for (int i = 0; i < quoteList.size(); i++) {
                        QuoteEntity quote = quoteList.get(i);
                        // 如果时间已经结束了,则改变状态并设置下一轮

                        if (quote.getLeftTime() <= 1000) {
                            quoteMapper.update(new UpdateWrapper<QuoteEntity>().eq("id", quote.getId()).set("status", "已结束"));
                            if (i + 1 <= quoteList.size() - 1) {
                                quoteMapper.update(new UpdateWrapper<QuoteEntity>().eq("id", quoteList.get(i + 1).getId()).set("status", "进行中"));
                            }
                        }
                        // 如果当前轮次开始了,则轮次的剩余时间开始倒计时(每次减一秒)
                        if (Objects.equals(quote.getStatus(), "进行中")) {
                            quoteMapper.update(new UpdateWrapper<QuoteEntity>().eq("id", quote.getId()).set("left_time", quote.getLeftTime() - 1000));
                        }
                    }

                    // 判断轮次状态是否都是已结束
                    boolean allEnd = quoteList.stream().allMatch(item -> "已结束".equals(item.getStatus()));
                    System.out.println(allEnd);
                    // 如果是true则把项目状态设置为已结束
                    if (allEnd) {
                        projectMapper.update(new UpdateWrapper<ProjectEntity>().eq("id", projectEntity.getId()).set("status", "已结束"));
                    }

                }

            }
        }
    }

}
