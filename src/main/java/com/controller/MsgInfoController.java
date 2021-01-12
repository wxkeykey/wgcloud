package com.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.Page;
import com.entity.msg.MsgInfo;
import com.service.msg.MsgInfoService;
import com.util.CodeUtil;
import com.util.staticvar.StaticKeys;

/**
 *
 * @ClassName:MsgInfoController.java     
 * @version V1.0 
 * @author: http://www.wgstart.com     
 * @date: 2019年1月14日
 * @Description: MsgInfoController.java
 * @Copyright: 2019 wgcloud. All rights reserved.
 *
 */
@Controller
@RequestMapping("/sys/msg")
public class MsgInfoController {
	
	 
	private static final Logger logger = LoggerFactory.getLogger(MsgInfoController.class);
	
	private String MENU_MARK = "msgActive";//菜单标识
	
    @Resource
    private MsgInfoService msgInfoService;
	    
	/**
     * 根据条件查询信息发送记录列表
     * @param model
     * @param request
     * @return
     */
	@RequestMapping(value="list")
	public String msgInfoList(Model model,HttpServletRequest request) {
		String curPage = request.getParameter(StaticKeys.CUR_PAGE);
		if(StringUtils.isEmpty(curPage)){
			curPage = "1";
		}
		String account = request.getParameter("account");
		String acceptInfo = request.getParameter("acceptInfo");
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer url = new StringBuffer();
		if(!StringUtils.isEmpty(account)) {
			account = CodeUtil.unescape(account);
			params.put("account", account);
			url.append("&account=").append(CodeUtil.escape(account));
			model.addAttribute("account", account);
		}
		if(!StringUtils.isEmpty(acceptInfo)) {
			acceptInfo = CodeUtil.unescape(acceptInfo);
			params.put("acceptInfo", acceptInfo);
			url.append("&acceptInfo=").append(CodeUtil.escape(acceptInfo));
			model.addAttribute("acceptInfo", acceptInfo);
		}
		Page page = null;
		try {
			page = msgInfoService.selectByParams(params, Integer.valueOf(curPage), StaticKeys.PAGE_SIZE);
			model.addAttribute("pageUrl", "/sys/msg/list?1=1"+ url.toString());
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("查询出错",e);
		}
		model.addAttribute(MENU_MARK, StaticKeys.MENU_ACTIVE);
		return "msg/list";
	}
	
    /**
     * 查看信息发送记录
     * @param LogInfo
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="view")
    public String viewLogInfo(Model model,HttpServletRequest request) {
    	 model.addAttribute(MENU_MARK, StaticKeys.MENU_ACTIVE);
    	 String id = request.getParameter("id");
    	 MsgInfo logInfo;
		try {
			logInfo = msgInfoService.selectById(id);
			model.addAttribute("msgInfo", logInfo);
		} catch (Exception e) {
			logger.error("查看信息发送记录：",e);
		}
    	return "msg/view";
    }
    
    
    /**
     * 删除
     * @param id
     * @param model
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value="del")
    public String delete(Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			try {
				msgInfoService.deleteById(request.getParameter("id").split(","));
			} catch (Exception e) {
				logger.error("删除日志异常：",e);
			}
		}
        return "redirect:/sys/msg/list";
    }   
    
}
