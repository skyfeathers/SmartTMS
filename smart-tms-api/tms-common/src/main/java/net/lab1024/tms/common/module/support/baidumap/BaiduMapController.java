package net.lab1024.tms.common.module.support.baidumap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.baidumap.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 3:12 下午
 */
@Api(tags = {SwaggerTagConst.Support.BAIDU_MAP})
@RestController
public class BaiduMapController extends SupportBaseController {

    @Autowired
    private BaiduMapService baiduMapService;

    @ApiOperation("查询两点距离（千米） - @author yandy")
    @PostMapping("/baidu/map/distance/query")
    public ResponseDTO<Integer> distanceQuery(@RequestBody @Valid BaiduAddressDistanceQueryForm distanceQueryForm) {
        return baiduMapService.distanceQuery(distanceQueryForm);
    }

    @ApiOperation("根据经纬度查询详细地址 - @author lidoudou")
    @PostMapping("/baidu/map/reverseGeocoding/query")
    public ResponseDTO<BaiduLonLatAddressVO> reverseGeocoding(@RequestBody @Valid BaiduReverseGeocodingQueryForm distanceQueryForm) {
        return baiduMapService.reverseGeocoding(distanceQueryForm);
    }

    @ApiOperation("根据源地址转换详细地址 @author zhaoxinyang")
    @PostMapping("/baidu/map/address/trans")
    public ResponseDTO<BaiduLonLatAddressVO> addressTrans(@RequestBody @Valid BaiduAddressTransForm transForm) {
        return baiduMapService.addressTrans(transForm);
    }

    @ApiOperation("根据关键字检索地址 @author zhaoxinyang")
    @PostMapping("/baidu/map/address/suggestion")
    public ResponseDTO<List<BaiduSuggestionAddressVO>> suggestion(@RequestBody @Valid BaiduAddressSuggestionForm suggestionForm) {
        return baiduMapService.suggestionByKeywords(suggestionForm);
    }

}