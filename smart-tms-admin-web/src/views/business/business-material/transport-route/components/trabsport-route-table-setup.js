/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 17:38:06
 * @LastEditors:
 * @LastEditTime: 2022-07-13 17:38:06
 */
import {reactive} from "vue";
import _ from "lodash";
export function transportRouteTableSetup() {
    const containerTransportColumns = reactive([
        {
            title: 'ID',
            dataIndex: 'transportRouteId',
            width: 50,
        },
        {
            title: '路线名称',
            dataIndex: 'transportRouteName',
            width: 200,
            ellipsis: true,
        },
        {
            title: '提柜地点',
            dataIndex: 'containerLocation',
            width: 300,
            ellipsis:true,
        },
        {
            title: '装货地点',
            dataIndex: 'placingLoading',
            width: 300,
            ellipsis:true,
        },
        {
            title: '卸货地点',
            dataIndex: 'unloadingLocation',
            width: 300,
            ellipsis:true,
        },
        {
            title: '还柜地点',
            dataIndex: 'returnContainerLocation',
            width: 300,
            ellipsis:true,
        },
        {
            title: '里程（千米）',
            dataIndex: 'mileage',
            width: 110,
        },
        {
            title: '路线状态',
            dataIndex: 'disabledFlag',
            width: 90,
        },
        {
            title: '创建人',
            dataIndex: 'createUserName',
            width: 100,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            width: 170,
        },
        {
            title: '操作',
            dataIndex: 'action',
            fixed: 'right',
            width: 100,
        },
    ]);

    const generalGoodsTransportColumns = reactive([
        {
            title: 'ID',
            width: 50,
            dataIndex: 'transportRouteId',
        },
        {
            title: '路线名称',
            dataIndex: 'transportRouteName',
            width: 200,
        },
        {
            title: '装货地点',
            dataIndex: 'placingLoading',
        },
        {
            title: '卸货地点',
            dataIndex: 'unloadingLocation',
        },
        {
            title: '里程（千米）',
            dataIndex: 'mileage',
            width: 110,
        },
        {
            title: '路线状态',
            dataIndex: 'disabledFlag',
            width: 80,
        },
        {
            title: '创建人',
            dataIndex: 'createUserName',
            width: 80,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            width: 160,
        },
        {
            title: '操作',
            dataIndex: 'action',
            fixed: 'right',
            width: 90,
        },
    ]);

    // 地址
    function getAddress(pathList, type) {
        let address = "";
        if(_.isEmpty(pathList)){
            return address;
        }
        let path = pathList.find(e => e.type == type);
        if(!path){
            return address;
        }
        if (path.provinceName) {
            address = address + path.provinceName;
        }
        if (path.cityName) {
            address = address + path.cityName;
        }
        if (path.districtName) {
            address = address + path.districtName;
        }
        if (path.address) {
            address = address + path.address;
        }
        return address;
    }

    return {
        containerTransportColumns,
        generalGoodsTransportColumns,
        getAddress,
    }
}
