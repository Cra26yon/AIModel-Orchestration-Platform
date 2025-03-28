package com.op.api.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "orchestrator-service")
public interface OrchestratorClient {


}
