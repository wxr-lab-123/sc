package com.hjm.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRoutel {

    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter routeDefinitionWriter;

    private final String dataId = "gateway-routers.json";
    private final String group = "DEFAULT_GROUP";
    private final Set<String> routeIds = new HashSet<>();

    @PostConstruct
    public void initRouterConfigListener() throws NacosException {
        // 1.项目启动时,先拉取一次配置,并且添加配置监听器
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String s) {
                        // 2.监听器配置变更,需要去更新路由表
                        updateConfigInfo(s);
                    }
                });
        // 3.
        updateConfigInfo(configInfo);
    }
    public void updateConfigInfo(String configInfo){
        log.debug("监听路由表配置信息，{}", configInfo);
        // 1.将configInfo转化为list
        List<RouteDefinition> list = JSONUtil.toList(configInfo, RouteDefinition.class);
        // 2.删除旧的路由表
        routeIds.stream().forEach(s-> routeDefinitionWriter.delete(Mono.just(s)).subscribe());
        routeIds.clear();
        // 2.更新路由表
        for (RouteDefinition routeDefinition : list) {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            routeIds.add(routeDefinition.getId());
        }
    }
}
