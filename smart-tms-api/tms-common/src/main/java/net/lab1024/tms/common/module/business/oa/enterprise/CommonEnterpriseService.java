package net.lab1024.tms.common.module.business.oa.enterprise;

import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yandy
 * @description:
 * @date 2025/1/22 12:13 下午
 */
@Service
public class CommonEnterpriseService {

    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;

    private static ConcurrentHashMap<String, EnterpriseEntity> domainEnterpriseMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void domainCacheInit(){
        List<EnterpriseEntity> companyEntityList = commonEnterpriseDao.selectEnterpriseList(false, false);
        for(EnterpriseEntity enterpriseEntity : companyEntityList){
            String domainNames  = enterpriseEntity.getDomainName();
            if(StringUtils.isEmpty(domainNames)){
                return;
            }
            String [] domainNameArray = domainNames.split(",");
            for(String domainName : domainNameArray){
                domainEnterpriseMap.put(domainName, enterpriseEntity);
            }
        }
    }

    /**
     * 移除域名缓存
     * @param domainNames
     */
    public void removeDomainCache(String domainNames){
        if(StringUtils.isEmpty(domainNames)){
            return;
        }
        String [] domainNameArray = domainNames.split(",");
        for(String domainName : domainNameArray){
            domainEnterpriseMap.remove(domainName);
        }
    }

    /**
     * 根据域名缓存公司id
     * @param request
     * @return
     */
    public EnterpriseEntity selectByDomainName(HttpServletRequest request){
        String domainName = request.getServerName();
        if(request.getServerPort() != 80 && request.getServerPort() != 443){
            domainName = domainName + ":" + request.getServerPort();
        }
        EnterpriseEntity enterpriseEntity = domainEnterpriseMap.get(domainName);
        if (enterpriseEntity != null) {
            return enterpriseEntity;
        }
        if (enterpriseEntity == null) {
            enterpriseEntity = commonEnterpriseDao.selectByDomainName(domainName, false, false);
        }
        if (enterpriseEntity != null) {
            String domainNames  = enterpriseEntity.getDomainName();
            String [] domainNameArray = domainNames.split(",");
            for(String domainNameItem : domainNameArray){
                domainEnterpriseMap.put(domainNameItem, enterpriseEntity);
            }
        }
        return enterpriseEntity;
    }
}