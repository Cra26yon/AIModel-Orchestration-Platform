package com.op.gateway.filters;

import com.op.gateway.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    // AuthPaths匹配 器
    private final AntPathMatcher autPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request请求
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是否在放行名单中，不需要进行登录拦截
        if (isExclude(request.getPath().toString())) {
            //在方形名单内，直接放行
            return chain.filter(exchange);
        }
        //3.获取token
        String token = request.getHeaders().getFirst("authorization");
        //4.校验token是否为空
        if (token == null) {
            /*
            * 若token为空，直接终止该请求
            * 拦截，设置响应状态码为401
            */
            //首先拿到响应对象
            ServerHttpResponse response = exchange.getResponse();
            //设置响应状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //完结整条过滤器链，不再进行后续拦截器，而response.setComplete()的返回值是Mono，直接返回即可
            return response.setComplete();
        }
        //5.token不为空则直接放行
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {
        //拿到authProperties中配置的所有放行路径，并且进行遍历
        for (String excludePath : authProperties.getExcludePaths()) {
            //进行路径匹配
            if (autPathMatcher.match(excludePath, path)) {
                //放行
                return true;
            }
        }
        //返回错误信息
         return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
