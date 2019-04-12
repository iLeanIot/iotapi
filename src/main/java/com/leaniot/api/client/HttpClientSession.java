package com.leaniot.api.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.leaniot.api.HttpSession;
import com.leaniot.api.dto.RestPage;
import com.leaniot.domain.Device;
import com.leaniot.domain.DeviceType;
import com.leaniot.domain.IoTObject;
import com.leaniot.domain.Site;
import com.leaniot.domain.SiteType;
import com.leaniot.domain.User;
import com.leaniot.domain.attribute.AttTypeInfo;
import com.leaniot.domain.attribute.AttValueInfo;
import com.leaniot.domain.attribute.AttributeType;
import com.leaniot.domain.attribute.ClassTypeInfo;
import com.leaniot.domain.attribute.IDeviceAttTypeInfo;
import com.leaniot.dto.ActionTypeInfo;
import com.leaniot.dto.DeviceTypeInfo;
import com.leaniot.dto.DeviceTypeRenameInfo;
import com.leaniot.dto.PageInfo;
import com.leaniot.dto.QueryInfo;
import com.leaniot.dto.SiteInfo;
import com.leaniot.dto.SitePageInfo;
import com.leaniot.dto.SiteRenameInfo;
import com.leaniot.dto.SiteTypeRenameInfo;
import com.leaniot.dto.SiteUpdateInfo;
import com.leaniot.dto.UserInfo;
import com.leaniot.dto.UserUpdateInfo;
import com.leaniot.exception.NotFoundException;
import com.leaniot.exception.ValueException;

/**
 * 客户端与物联网平台的http会话
 *
 * @author 曹新宇
 */
@Component
public class HttpClientSession extends HttpSession {
	/**
	 * 获取设备信息。
	 * @param deviceId 设备标识符。
	 * @return 返回设备信息。
	 */
	public Device getDevice(String deviceId) {
		return getEntity("/device/" + deviceId, null, Device.class);
	}
	
	/**
	 * 查询符合条件的设备列表。
	 * @param queryParams 查询条件。
	 * @return 设备列表。
	 */
	public List<Device> queryDeviceList(Map<String, String> queryParams){
		return getEntity("/devices/list", queryParams, new ParameterizedTypeReference<List<Device>>() {});
	}
	
	private Class<User> userType = User.class;
	private String userUrl = "/user";
	
	public User addUser(UserInfo info) {
		return postEntity(userUrl, info, userType);
	}
	
	public User getCurrentUser() {
		return getEntity(userUrl + "/me", null, userType);
	}
	
	public User getUser(String username) {
		if(username != null && !username.isEmpty()) {
			return getEntity(userUrl + "/" + username, null, userType);
		} else
			throw new ValueException("username can't be empty");
	}
	
	public void deleteUser(String userId) {
		if(userId != null && !userId.isEmpty())
			deleteEntity(userUrl + "/" + userId, null, userType);
		else
			throw new ValueException("userid can't be empty");
	}
	
	public Page<User> getUserPage(PageInfo info) {
		if(info == null)
			info = new PageInfo();
		Map<String, String> queryParams= new HashMap<String, String>();
		queryParams.put("currentPage", Integer.toString(info.getCurrentPage()));
		queryParams.put("numPerPage", Integer.toString(info.getNumPerPage()));
		
		return getEntity(userUrl + "s", queryParams, new ParameterizedTypeReference<RestPage<User>>() {});
	}
	
	public User updateUserArea(UserUpdateInfo info) {
		return patchEntity(userUrl, info, userType);
	}
	
	private Class<DeviceType> deviceTypeType = DeviceType.class;
	private String deviceTypeUrl = "/" + IoTObject.devicetype.getName();
	
	public DeviceType addDevicetype(DeviceTypeInfo info) {
		return postEntity(deviceTypeUrl, info, deviceTypeType);
	}
	
	public DeviceType getDeviceType(String id) {
		if(id != null && !id.isEmpty()) {
			return getEntity(deviceTypeUrl + "/" + id, null, deviceTypeType);
		} else
			throw new ValueException("id can't be empty");
	}
	
	public Page<DeviceType> getDeviceTypePage(PageInfo info) {
		if(info == null)
			info = new PageInfo();
		Map<String, String> queryParams= new HashMap<String, String>();
		queryParams.put("currentPage", Integer.toString(info.getCurrentPage()));
		queryParams.put("numPerPage", Integer.toString(info.getNumPerPage()));
		
		return getEntity(deviceTypeUrl + "s/page", queryParams, new ParameterizedTypeReference<RestPage<DeviceType>>() {});
	}
	
	public List<DeviceType> getDeviceTypeList() {
		return getEntity(deviceTypeUrl + "s/list", null, new ParameterizedTypeReference<List<DeviceType>>() {});
	}
	
	public DeviceType renameDeviceType(DeviceTypeRenameInfo info) {
		return patchEntity(deviceTypeUrl + "/name", info, deviceTypeType);
	}
	
	public void deleteDeviceType(String id) {
		if(id != null && !id.isEmpty())
			deleteEntity(deviceTypeUrl + "/" + id, null, deviceTypeType);
		else
			throw new ValueException("id can't be empty");
	}
	
	public DeviceType addDevicetypeAttribute(String id, IDeviceAttTypeInfo info) {
		return postEntity(deviceTypeUrl + "/" + id + "/attribute", info, deviceTypeType);
	}
	
	public void deleteDeviceTypeAttribute(String id, String attribute) {
		if(id != null && !id.isEmpty() && attribute != null && !attribute.isEmpty())
			deleteEntity(deviceTypeUrl + "/" + id + "/attribute/" + attribute, null, deviceTypeType);
		else
			throw new ValueException("id and attribute can't be empty");
	}
	
	public DeviceType addDevicetypeActiontype(String id, ActionTypeInfo info) {
		return postEntity(deviceTypeUrl + "/" + id + "/actiontype", info, deviceTypeType);
	}
	
	public void deleteDeviceTypeActiontype(String id, String actiontype) {
		if(id != null && !id.isEmpty() && actiontype != null && !actiontype.isEmpty())
			deleteEntity(deviceTypeUrl + "/" + id + "/actiontype/" + actiontype, null, deviceTypeType);
		else
			throw new ValueException("id and actiontype can't be empty");
	}
	
	public DeviceType addDevicetypeAlarmtype(String id, AttTypeInfo info) {
		return postEntity(deviceTypeUrl + "/" + id + "/alarmtype", info, deviceTypeType);
	}
	
	public void deleteDeviceTypeAlarmtype(String id, String alarmtype) {
		if(id != null && !id.isEmpty() && alarmtype != null && !alarmtype.isEmpty())
			deleteEntity(deviceTypeUrl + "/" + id + "/alarmtype/" + alarmtype, null, deviceTypeType);
		else
			throw new ValueException("id and alarmtype can't be empty");
	}
	
	private Class<SiteType> siteTypeType = SiteType.class;
	private String siteTypeUrl = "/" + IoTObject.sitetype.getName();
	
	public SiteType addSitetype(ClassTypeInfo info) {
		return postEntity(siteTypeUrl, info, siteTypeType);
	}
	
	public SiteType getSiteType(String id) {
		if(id != null && !id.isEmpty()) {
			return getEntity(siteTypeUrl + "/" + id, null, siteTypeType);
		} else
			throw new ValueException("id can't be empty");
	}
	
	public Page<SiteType> getSiteTypePage(PageInfo info) {
		if(info == null)
			info = new PageInfo();
		Map<String, String> queryParams= new HashMap<String, String>();
		queryParams.put("currentPage", Integer.toString(info.getCurrentPage()));
		queryParams.put("numPerPage", Integer.toString(info.getNumPerPage()));
		
		return getEntity(siteTypeUrl + "s/page", queryParams, new ParameterizedTypeReference<RestPage<SiteType>>() {});
	}
	
	public List<SiteType> getSiteTypeList() {
		return getEntity(siteTypeUrl + "s/list", null, new ParameterizedTypeReference<List<SiteType>>() {});
	}
	
	public SiteType renameSiteType(SiteTypeRenameInfo info) {
		return patchEntity(siteTypeUrl + "/name", info, siteTypeType);
	}
	
	public void deleteSiteType(String id) {
		if(id != null && !id.isEmpty())
			deleteEntity(siteTypeUrl + "/" + id, null, siteTypeType);
		else
			throw new ValueException("id can't be empty");
	}
	
	private Class<Site> siteType = Site.class;
	private String siteUrl = "/" + IoTObject.site.getName();
	
	public Site addSite(SiteInfo<Object> info) {
		SiteType st = getSiteByName(info.getSiteType());
		
		SiteInfo<AttValueInfo> i = new SiteInfo<AttValueInfo>();
		i.setName(info.getName());
		i.setParentId(info.getParentId());
		i.setSiteType(info.getSiteType());
		Map<String, Object> attInfos = info.getAttInfos();
		Map<String, AttributeType> attDefinition = st.getAttDefinition();
		
		Map<String, AttValueInfo> attValues = getAttInfos(attInfos, attDefinition);
		i.setAttInfos(attValues);
		
		return postEntity(siteUrl, i, siteType);
	}

	public SiteType getSiteByName(String name) {
		QueryInfo q = new QueryInfo();
		String filter = String.format("{\"name\": \"%s\"}", name);
		q.setFilter(filter);
		SiteType st = this.getOneEntity(IoTObject.sitetype, q);
		if(st == null)
			throw new NotFoundException("site type");
		return st;
	}

	private Map<String, AttValueInfo> getAttInfos(Map<String, Object> attInfos,
			Map<String, AttributeType> attDefinition) {
		Map<String, AttValueInfo> iValue = new HashMap<String, AttValueInfo>();
		
		for (Map.Entry<String, AttributeType> entry : attDefinition.entrySet()) {
			String key = entry.getKey();
			AttributeType attType = entry.getValue();
			Object value = attInfos.get(key);
			
			try{
				iValue.put(key, attType.getAttValue(value));
			} catch(ValueException e) {
				throw new ValueException(key + ":" + e.getMessage());
			}
		}
		return iValue;
	}
	
	public Site getSite(String id) {
		if(id != null && !id.isEmpty()) {
			return getEntity(siteUrl + "/" + id, null, siteType);
		} else
			throw new ValueException("id can't be empty");
	}
	
	public void deleteSite(String id) {
		if(id != null && !id.isEmpty())
			deleteEntity(siteUrl + "/" + id, null, siteType);
		else
			throw new ValueException("id can't be empty");
	}
	
	public Site renameSite(SiteRenameInfo info) {
		return patchEntity(siteUrl + "/name", info, siteType);
	}
	
	public Site updateSite(SiteUpdateInfo<Object> info) {
		SiteType st = getSite(info.getId()).getSiteType();
		
		SiteUpdateInfo<AttValueInfo> siteValue = new SiteUpdateInfo<AttValueInfo>();
		siteValue.setId(info.getId());
		Map<String, Object> attInfos = info.getAttInfos();
		Map<String, AttributeType> attDefinition = st.getAttDefinition();
		
		Map<String, AttValueInfo> attValues = getAttInfos(attInfos, attDefinition);
		siteValue.setAttInfos(attValues);
		
		return patchEntity(siteUrl, siteValue, siteType);
	}
	
	public Page<Site> getSitePage(SitePageInfo info) {
		if(info == null)
			info = new SitePageInfo();
		Map<String, String> queryParams= new HashMap<String, String>();
		queryParams.put("currentPage", Integer.toString(info.getCurrentPage()));
		queryParams.put("numPerPage", Integer.toString(info.getNumPerPage()));
		if(info.getParentId() != null)
			queryParams.put("parentId", info.getParentId());
		if(info.getSiteName() != null)
			queryParams.put("siteName", info.getSiteName());
		if(info.getSiteTypeId() != null)
			queryParams.put("siteTypeId", info.getSiteTypeId());
		
		return getEntity(siteUrl + "s", queryParams, new ParameterizedTypeReference<RestPage<Site>>() {});
	}
	
	public long getSiteCount(String parentId, String siteName, String siteTypeId) {
		Map<String, String> queryParams= new HashMap<String, String>();
		if(parentId != null)
			queryParams.put("parentId", parentId);
		if(siteName != null)
			queryParams.put("siteName", siteName);
		if(siteTypeId != null)
			queryParams.put("siteTypeId", siteTypeId);
		
		return getEntity(siteUrl + "s/count", queryParams, Long.class);
	}
}
