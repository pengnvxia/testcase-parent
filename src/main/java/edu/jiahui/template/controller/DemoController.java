package edu.jiahui.{{ name }}.controller;

import edu.jiahui.template.domain.Demo;
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
 * @date 2019/7/2
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping(method = RequestMethod.GET, value = "/testGet")
    public String testGet(@RequestParam String testId) {

        return "OK";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/testPost")
    @ResponseBody
    public Demo testPost(@RequestBody Demo demo) {
        return demo;
    }
}
