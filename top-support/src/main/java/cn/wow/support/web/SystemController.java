package cn.wow.support.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wow.common.domain.Menu;
import cn.wow.common.service.MenuService;

@Controller
@RequestMapping(value = "system")
public class SystemController {
	
	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model, String choose) throws IOException {
		Menu menu = menuService.selectByAlias("system");
		
		if(StringUtils.isBlank(choose)){
			choose = "0";
		}
		model.addAttribute("choose", Integer.parseInt(choose));
		
		ObjectMapper mapper = new ObjectMapper();  
        try {
			String json = mapper.writeValueAsString(menu.getSubList());
			model.addAttribute("menu", json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}  
		return "sys/index";
	}
	
}
