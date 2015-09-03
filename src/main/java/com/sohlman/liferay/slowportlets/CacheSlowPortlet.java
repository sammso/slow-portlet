package com.sohlman.liferay.slowportlets;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Portlet implementation class CacheSlowPortlet
 */
public class CacheSlowPortlet extends MVCPortlet {
 
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {
		
		renderResponse.getCacheControl().setExpirationTime(60);
		
		try {
			List<PortletPreferences> list = PortletPreferencesLocalServiceUtil.getPortletPreferences();
			String header = "plid\tportletId\tprefgroupId\tarticleId\tddmTemplateKey";
			System.out.println(header);
			for (PortletPreferences portletPreferences : list) {
				if (!portletPreferences.getPortletId().startsWith("56_")) {
					continue;
				}
				portletPreferences.getPreferences();
				String xml = portletPreferences.getPreferences();

				
				javax.portlet.PortletPreferences javaxPrefs =
					PortletPreferencesFactoryUtil.fromDefaultXML(xml);
				
				String prefsGroupId = javaxPrefs.getValue(
						"groupId", null);
				String articleId = javaxPrefs.getValue(
					"articleId", null);
				String ddmTemplateKey = javaxPrefs.getValue(
					"ddmTemplateKey", null);
				

				
				String row = String.format("%s\t%s\t%s\t%s\t%s",
						portletPreferences.getPlid(),
						portletPreferences.getPortletId(),
						prefsGroupId,
						articleId,
						ddmTemplateKey);
				System.out.println(row);
			}
		
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		super.render(renderRequest, renderResponse);
	}
}
