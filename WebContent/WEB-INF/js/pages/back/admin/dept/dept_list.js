$(function() {
	$("button[id^=edit-]").each(function() {
		$(this).on("click", function() {
			;
			did = this.id.split("-")[1];
			console.log("部门编号：" + did);
			dname = $("#dname-" + did).val();
			console.log("部门mingcheng：" + dname);
			
			$.post("pages/back/admin/dept/edit.action", {
				"did" : did,
				"dname" : dname
			}, function(data) {
				
					if (data == "true") {
						operateAlert(true, "部门名称更新成功！", "部门更新失败");

					} else {
						operateAlert(false, "", "更新失败！");
					}
			
			}, "text");

		});
	});

	$("span[id^=eid-]").each(function() {
		$(this).on("click", function() {

			eid = this.id.split("-")[1];
			x = this.id.split("-")[2];
			console.log("雇员编号：" + eid + "-" + x);
			var dname = $("")
			f = eid + "-" + x;
			$.post("pages/back/admin/emp/get.action", {
				"eid" : f
			}, function(data) {
				$("#hello").text(data.emp.ename);
				$("#phone").text(data.emp.phone);
				$("#time").text(data.emp.hiredate.date);
				$("#baby").text(data.emp.note);
                $("#dname").text(data.dept.dname);
                $("#jjj").text(data.level.title)
			}, "json");
			$("#userInfo").modal("toggle");
		});
	});

});