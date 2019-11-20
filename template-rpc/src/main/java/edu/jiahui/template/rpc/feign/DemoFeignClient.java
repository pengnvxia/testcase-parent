package edu.jiahui.{{ name }}.rpc.feign;

import edu.jiahui.{{ name }}.constant.ServiceConstant;
import edu.jiahui.{{ name }}.domain.Demo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * Copyright (C) 2019 Shanghai JiaHui Edu., Ltd. All rights reserved.
 * <p>
 * No parts of this file may be reproduced or transmitted in any form or by any means,
 * electronic, mechanical, photocopying, recording, or otherwise, without prior written
 * permission of Shanghai JiaHui Edu., Ltd.
 *
 * @author krame
 * @date 2019/11/14
 */
@FeignClient(ServiceConstant.SERVICE_NAME)
@RequestMapping(ServiceConstant.ROOT_URL)
public interface DemoFeignClient {

    @GetMapping("/demo/testGet")
    String testGet(@RequestParam("testId") String testId);

    @PostMapping("/demo/testPost")
    Demo testPost(@RequestBody Demo demo);
}
