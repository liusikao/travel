package cn.mldn.travel.action.back;

import java.io.IOException;

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

import cn.mldn.travel.service.back.IDeptServiceBack;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.vo.Emp;
import cn.mldn.util.action.abs.AbstractBaseAction;
import cn.mldn.util.enctype.PasswordUtil;
import cn.mldn.util.web.FileUtils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/pages/back/admin/emp/*")
public class EmpActionBack extends AbstractBaseAction {
	private static final String FLAG = "雇员";
	
	@Resource
	private IEmpServiceBack iEmpService;
    @Resource
    private IDeptServiceBack iDeptService ;
    
	@RequestMapping("add_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:add")
	public ModelAndView addPre() {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.add.page"));
		mav.addObject("allDept", iDeptService.list());
		mav.addObject("allLevel", iDeptService.listLevel());
		
		return mav;
	}

	@RequestMapping("add")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:add")
	public ModelAndView add(Emp vo, MultipartFile pic, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		if(pic== null ||pic.getSize() == 0){
		    
		    
		    super.setUrlAndMsg(request, "emp.add.action", "vo.add.failure", FLAG);
			
		}else{
			FileUtils fileUtils = new FileUtils (pic) ;
			String fileName = fileUtils.createFileName();
			vo.setPhoto(fileName);
			fileUtils.saveFile(request, "upload", fileName);
			
			iEmpService.add(vo);
			
			super.setUrlAndMsg(request, "emp.add.action", "vo.add.success", FLAG);
		}
		
		
		// super.setUrlAndMsg(request, "emp.add.action", "vo.add.failure",
		// FLAG);
		
		return mav;
	}

	@RequestMapping("edit_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:edit")
	public ModelAndView editPre(String eid) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.edit.page"));
		Emp vo = iEmpService.findByEid(eid) ;
		String password =  PasswordUtil.getPassword(vo.getPassword());
		System.out.println(password);
	    vo.setPassword(password);
		mav.addObject("emp",vo);
		
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
		JSONObject obj =new JSONObject() ;		
		obj.put("emp", vo);
		String emp =    obj.toString()  ;
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
	public ModelAndView list(String ids, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.list.page"));
		return mav;
	}

	@RequestMapping("delete")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:delete")
	public ModelAndView delete(String ids, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "emp.list.action", "vo.delete.failure",
		// FLAG);
		super.setUrlAndMsg(request, "emp.list.action", "vo.delete.success", FLAG);
		return mav;
	}
	
	
	
	
}
