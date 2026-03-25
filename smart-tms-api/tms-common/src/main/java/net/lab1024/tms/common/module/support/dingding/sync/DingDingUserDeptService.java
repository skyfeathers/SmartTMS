package net.lab1024.tms.common.module.support.dingding.sync;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.constant.NumberConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingUrlConst;

import java.util.List;

/**
 * 钉钉操作
 *
 * @author lidoudou
 * @date 2023/4/20 下午4:31
 */
@Slf4j
@Service
public class DingDingUserDeptService {

    @Autowired
    private DingDingTokenService dingDingTokenService;

    public List<OapiV2UserListResponse.ListUserResponse> queryUserList(Long enterpriseId, Long deptId) {
        String accessToken = dingDingTokenService.getAccessToken(enterpriseId);
        Long initCursor = 0L;
        List<OapiV2UserListResponse.ListUserResponse> dingUserList = Lists.newArrayList();
        this.queryUserList(accessToken, deptId, initCursor, dingUserList);
        return dingUserList;
    }

    private void queryUserList(String accessToken, Long deptId, Long cursor, List<OapiV2UserListResponse.ListUserResponse> userList) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingUrlConst.USER_LIST_BY_DEPT_ID);
            OapiV2UserListRequest req = new OapiV2UserListRequest();
            req.setDeptId(deptId);
            req.setCursor(cursor);
            req.setSize(10L);
            OapiV2UserListResponse rsp = client.execute(req, accessToken);
            OapiV2UserListResponse.PageResult pageResult = rsp.getResult();
            List<OapiV2UserListResponse.ListUserResponse> userListResp = pageResult.getList();
            userList.addAll(userListResp);
            if (pageResult.getHasMore()) {
                queryUserList(accessToken, deptId, pageResult.getNextCursor(), userList);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public OapiV2UserGetResponse.UserGetResponse getByUserId(Long enterpriseId, String userId) {
        String accessToken = dingDingTokenService.getAccessToken(enterpriseId);
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingUrlConst.USER_GET);
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, accessToken);
            OapiV2UserGetResponse.UserGetResponse user = rsp.getResult();
            return user;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> queryDeptList(Long enterpriseId) {
        String accessToken = dingDingTokenService.getAccessToken(enterpriseId);
        Long initDeptId = 1L;
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> dingDeptList = Lists.newArrayList();
        this.queryDeptList(accessToken, initDeptId, dingDeptList);
        return dingDeptList;
    }


    private void queryDeptList(String accessToken, Long deptId, List<OapiV2DepartmentListsubResponse.DeptBaseResponse> enterpriseDeptList) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingUrlConst.DEPARTMENT_LIST_SUB);
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(deptId);
            OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
            if (NumberConst.DEFAULT_PARENT_ID == rsp.getErrcode()) {
                List<OapiV2DepartmentListsubResponse.DeptBaseResponse> list = rsp.getResult();
                enterpriseDeptList.addAll(list);
                list.forEach(dept -> {
                    queryDeptList(accessToken, dept.getDeptId(), enterpriseDeptList);
                });
            }
        } catch (ApiException e) {
            log.error("查询钉钉部门出错：" + e.getMessage());
        }
    }

    public OapiV2DepartmentGetResponse.DeptGetResponse getByDeptId(Long enterpriseId, Long deptId) {
        String accessToken = dingDingTokenService.getAccessToken(enterpriseId);
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingUrlConst.GET_DEPARTMENT);
            OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
            req.setDeptId(deptId);
            OapiV2DepartmentGetResponse rsp = client.execute(req, accessToken);
            return rsp.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

}
