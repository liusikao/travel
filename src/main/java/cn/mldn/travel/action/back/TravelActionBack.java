package cn.mldn.travel.action.back;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.service.back.ITravelServiceBack;
import cn.mldn.travel.vo.Travel;
import cn.mldn.util.action.abs.AbstractBaseAction;

@Controller
@RequestMapping("/pages/back/admin/travel/*")
public class TravelActionBack extends AbstractBaseAction {
	private static final String FLAG = "出差申请";
	
	@Resource
	private ITravelServiceBack iTravel ;
	@Resource
	private IEmpServiceBack iEmp;
	
	@RequestMapping("add_pre")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:add" }, logical = Logical.OR)
	public ModelAndView addPre(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.add.page"));
		mav.addObject("allItems", iTravel.list());
		String eid = (String)  request.getSession().getAttribute("eid");
		mav.addObject("emp", iEmp.findByEid(eid));
		return mav;
	}

	@RequestMapping("add")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:add" }, logical = Logical.OR)
	public ModelAndView add(HttpServletRequest request,Travel vo) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "travel.add.action", "vo.add.failure",
		// FLAG);
		System.out.println(vo);
		vo.setAudit(1);
		Date date = new Date () ;
		String d = new SimpleDateFormat().format(date);
		Date a =null;
		try {
			a = new SimpleDateFormat("yyyy-MM-dd").parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vo.setSubdate(a);
		
		if(iTravel.add(vo) == true){
		
		super.setUrlAndMsg(request, "travel.add.action", "vo.add.success", FLAG);
		}else{
			super.setUrlAndMsg(request, "travel.add.action", "travel.submit.failure", FLAG);
		}
		return mav;
	}
	
	@RequestMapping("list_emp")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:self" }, logical = Logical.OR)
	public ModelAndView listEmp(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.emp.page"));
		return mav;
	}
	
	@RequestMapping("list_self")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:self" }, logical = Logical.OR)
	public ModelAndView listSelf(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.self.page"));
		return mav;
	}
	
	@RequestMapping("user_edit_pre")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:edit" }, logical = Logical.OR)
	public ModelAndView editUser() {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.user.page"));
		return mav;
	}

	@RequestMapping("cost_edit_pre")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:edit" }, logical = Logical.OR)
	public ModelAndView editCost() {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.cost.page"));
		return mav;
	}
	
	
	
	@RequestMapping("edit_pre")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:edit" }, logical = Logical.OR)
	public ModelAndView editPre() {
		ModelAndView mav = new ModelAndView(super.getUrl("travel.edit.page"));
		return mav;
	}
	 
	@RequestMapping("edit")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:edit" }, logical = Logical.OR)
	public ModelAndView edit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "travel.self.action", "vo.edit.failure",
		// FLAG);
		super.setUrlAndMsg(request, "travel.self.action", "vo.edit.success", FLAG);
		return mav;
	}
	
	@RequestMapping("delete")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:delete" }, logical = Logical.OR)
	public ModelAndView delete(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "travel.self.action", "vo.delete.failure",
		// FLAG);
		super.setUrlAndMsg(request, "travel.self.action", "vo.delete.success", FLAG);
		return mav;
	}
	
	@RequestMapping("submit")
	@RequiresUser
	@RequiresRoles(value = { "travel" }, logical = Logical.OR)
	@RequiresPermissions(value = { "travel:submit" }, logical = Logical.OR)
	public ModelAndView submit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "travel.self.action", "travel.submit.failure");
		super.setUrlAndMsg(request, "travel.self.action", "travel.submit.success");
		return mav;
	}
}
