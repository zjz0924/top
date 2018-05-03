package cn.wow.support.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.Page;
import cn.wow.common.domain.Vehicle;
import cn.wow.common.service.VehicleService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;

/**
 * 整车信息
 * 
 * @author zhenjunzhuo 2017-09-28
 */
@Controller
@RequestMapping(value = "vehicle")
public class VehicleController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(VehicleController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private VehicleService vehicleService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		return "vehicle/vehicle_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getList")
	public Map<String, Object> getList(HttpServletRequest request, Model model, String code, String type,
			String startProTime, String endProTime) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "code asc");
		map.put("state", Contants.FINISH_TYPE);

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
		}
		if (StringUtils.isNotBlank(type)) {
			map.put("type", type);
		}
		if (StringUtils.isNotBlank(startProTime)) {
			map.put("startProTime", startProTime);
		}
		if (StringUtils.isNotBlank(endProTime)) {
			map.put("endProTime", endProTime);
		}

		List<Vehicle> dataList = vehicleService.selectAllList(map);

		// 分页
		Page<Vehicle> pageList = (Page<Vehicle>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	/**
	 * 详情
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id) {
		if (StringUtils.isNotBlank(id)) {
			Vehicle vehicle = vehicleService.selectOne(Long.parseLong(id));

			model.addAttribute("facadeBean", vehicle);
		}
		return "vehicle/vehicle_detail";
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String code, String type, String proTime,
			String proAddr, String remark) {
		AjaxVO vo = new AjaxVO();
		Vehicle vehicle = null;

		try {
			if (StringUtils.isNotBlank(id)) {
				vehicle = vehicleService.selectOne(Long.parseLong(id));

				if (vehicle != null) {
					vehicle.setType(type);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					vehicle.setProTime(sdf.parse(proTime));
					vehicle.setProAddr(proAddr);
					vehicle.setRemark(remark);
					vehicleService.update(getCurrentUserName(), vehicle);
				}
				vo.setMsg("编辑成功");
			} else {
				Vehicle dbVehicle = vehicleService.selectByCode(code);

				if (dbVehicle != null) {
					vo.setData("code");
					vo.setMsg("代码已存在");
					vo.setSuccess(false);
					return vo;
				} else {
					vehicle = new Vehicle();
					vehicle.setType(type);
					vehicle.setCode(code);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					vehicle.setProTime(sdf.parse(proTime));
					vehicle.setProAddr(proAddr);
					vehicle.setRemark(remark);
					vehicle.setCreateTime(new Date());
					vehicleService.save(getCurrentUserName(), vehicle);

					vo.setMsg("添加成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			logger.error("整车信息保存失败", ex);
			vo.setMsg("保存失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, String id) {
		AjaxVO vo = new AjaxVO();
		vo.setMsg("删除成功");

		try {
			Vehicle vehicle = vehicleService.selectOne(Long.parseLong(id));

			if (vehicle != null) {
				vehicleService.deleteByPrimaryKey(getCurrentUserName(), vehicle);
			} else {
				vo.setMsg("删除失败，记录不存在");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("整车信息删除失败", ex);

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

}
