package cn.mldn.travel.action.back;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.travel.exception.DeptManagerExistException;
import cn.mldn.travel.service.back.IDeptServiceBack;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.service.back.ILevelServiceBack;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.travel.vo.Level;
import cn.mldn.util.action.abs.AbstractBaseAction;
import cn.mldn.util.enctype.PasswordUtil;
import cn.mldn.util.split.ActionSplitPageUtil;
import cn.mldn.util.web.FileUtils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/pages/back/admin/emp/*")
public class EmpActionBack extends AbstractBaseAction {
	private static final String FLAG = "雇员";

	@Resource
	private IEmpServiceBack iEmpService;
	@Resource
	private IDeptServiceBack iDeptService;
	@Resource
	private ILevelServiceBack iLevelService;

	@RequestMapping("add_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:add")
	public ModelAndView addPre() {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.add.page"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allDept", iDeptService.list());
		map.put("allLevel", iDeptService.listLevel());
		mav.addAllObjects(map);
		return mav;
	}

	@RequestMapping("add")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:add")
	public ModelAndView add(Emp vo, MultipartFile pic, HttpServletRequest request) {

		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		if (pic == null || pic.getSize() == 0) {

			super.setUrlAndMsg(request, "emp.add.action", "vo.add.failure", FLAG);

		} else {

			FileUtils fileUtils = new FileUtils(pic);
			String fileName = fileUtils.createFileName();
			vo.setIneid(request.getSession().getId());
			vo.setPhoto(fileName);
			String password = PasswordUtil.getPassword(vo.getPassword());
			vo.setPassword(password);
			try {

				fileUtils.saveFile(request, "upload/member/", fileName);

				iEmpService.add(vo);

			} catch (DeptManagerExistException e) {
				super.setUrlAndMsg(request, "emp.add.action", "emp.add.dept.mgr.failure");
			}
			super.setUrlAndMsg(request, "emp.add.action", "vo.add.success", FLAG);
		}

		return mav;
	}

	@RequestMapping("edit_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:edit")
	public ModelAndView editPre(String eid) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.edit.page"));
		Emp vo = iEmpService.findByEid(eid);

		mav.addObject("emp", vo);
		mav.addObject("allDept", iDeptService.list());
		mav.addObject("allLevel", iDeptService.listLevel());

		return mav;
	}

	@RequestMapping("edit")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:edit")
	public ModelAndView edit(Emp vo, MultipartFile pic, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "emp.list.action", "vo.edit.failure",
		// FLAG);
		super.setUrlAndMsg(request, "emp.list.action", "vo.edit.success", FLAG);
		return mav;
	}

	@RequestMapping("get")
	@RequiresUser
	public ModelAndView get(String eid, HttpServletResponse response) {

		Emp vo = iEmpService.findByEid(eid);
		Dept dept = iDeptService.findByEid(eid);
		Level level = iLevelService.findByLid(vo.getLid());

		JSONObject obj = new JSONObject();
		obj.put("emp", vo);
		obj.put("dept", dept);
		obj.put("level", level);
		String emp = obj.toString();
		System.out.println(emp);

		try {
			response.getWriter().println(emp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping("list")
	@RequiresUser
	@RequiresRoles(value = { "emp", "empshow" }, logical = Logical.OR)
	@RequiresPermissions(value = { "emp:list", "empshow:list" }, logical = Logical.OR)
	public ModelAndView list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.list.page"));
		ActionSplitPageUtil aspu = new ActionSplitPageUtil(request,"雇员编号:eid|雇员姓名:ename|联系电话:phone",super.getMsg("emp.list.action"));
		Map<String,Object> map = iEmpService.list(aspu.getCurrentPage(), aspu.getLineSize(), aspu.getColumn(), aspu.getKeyWord());
		List<Dept> allDepts = (List<Dept>) map.get("allDepts");
		List<Level> allLevels = (List<Level>) map.get("allLevels");

		mav.addAllObjects(map); 
		Map<Long, String> deptMap = new HashMap<Long, String>();
		Iterator<Dept> iter = allDepts.iterator();
		while (iter.hasNext()) {
			Dept dept = iter.next();
			deptMap.put(dept.getDid(), dept.getDname());
		}
		Map<String, String> levelMap = new HashMap<String, String>();
		Iterator<Level> iter2 = allLevels.iterator();
		while (iter2.hasNext()) {
			Level lev = iter2.next();
			levelMap.put(lev.getLid(), lev.getTitle());
		}
		mav.addObject("allDepts", deptMap); // 属性名称一样会出现覆盖
		mav.addObject("allLevels", levelMap); // 属性名称一样会出现覆盖
		return mav;
		
		
		
		
		
	}

	@RequestMapping("delete")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:delete")
	public ModelAndView delete(String  ids, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		Set<String> set =  super.handleStringIds(ids);
		if(iEmpService.delete(set)){
			super.setUrlAndMsg(request, "emp.list.action", "vo.delete.success", FLAG);
		}else{
			super.setUrlAndMsg(request, "emp.list.action", "vo.delete.failure", FLAG);
		}
		
		return mav;
	}

}
