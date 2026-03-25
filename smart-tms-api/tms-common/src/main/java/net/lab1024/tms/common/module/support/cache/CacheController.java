package net.lab1024.tms.common.module.support.cache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/10/11 20:07
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_CACHE})
public class CacheController extends SupportBaseController {

    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "获取所有缓存 @author 罗伊")
    @GetMapping("/cache/names")
    public ResponseDTO<List<String>> cacheNames() {
        return ResponseDTO.ok(cacheService.cacheNames());
    }


    @ApiOperation(value = "移除某个缓存 @author 罗伊")
    @GetMapping("/cache/remove/{cacheName}")
    public ResponseDTO<String> removeCache(@PathVariable String cacheName) {
        cacheService.removeCache(cacheName);
        return ResponseDTO.ok();
    }


    @ApiOperation(value = "获取某个缓存的所有key @author 罗伊")
    @GetMapping("/cache/keys/{cacheName}")
    public ResponseDTO<List<String>> cacheKeys(@PathVariable String cacheName) {
        return ResponseDTO.ok(cacheService.cacheKey(cacheName));
    }

}
