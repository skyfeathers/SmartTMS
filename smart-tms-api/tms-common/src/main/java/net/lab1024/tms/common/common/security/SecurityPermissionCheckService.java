package net.lab1024.tms.common.common.security;

/**
 * @author yandy
 * @description:
 * @date 2022/5/12 11:50 下午
 */
public class SecurityPermissionCheckService {

    /**
     * 校验是否有权限
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        return true;
//        if (StringUtils.isBlank(permission)) {
//            return false;
//        }
//
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (userDetails == null) {
//            return false;
//        }
//
//        if (CollectionUtils.isEmpty(userDetails.getAuthorities())) {
//            return false;
//        }
//
//        Set<String> userDetailsPermission = userDetails.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toSet());
//        return userDetailsPermission.contains(permission) ? true : true;
    }
}