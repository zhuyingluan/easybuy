//分类添加
function toAddProductCategory() {
    $.ajax({
        url: contextPath + "/admin/productCategory/toAddProductCategory",
        method: "post",
        success: function (data) {
            $("#addProductCategory").html(data);
            $("input[name=select]").removeAttr("checked");
        }
    });
}
function addProductCategory() {
    var productCategoryLevel1 = $("#productCategoryLevel1").val();
    var productCategoryLevel2 = $("#productCategoryLevel2").val();
    var name = $("#name").val();
    var type = $("#type").val();
    $.ajax({
        url: contextPath + "/admin/productCategory/addProductCategory",
        method: "post",
        data: {
            name: name,
            type: type,
            productCategoryLevel1: (productCategoryLevel1 == null || productCategoryLevel1 == "") ? 0 : productCategoryLevel1,
            productCategoryLevel2: (productCategoryLevel2 == null || productCategoryLevel2 == "") ? 0 : productCategoryLevel2
        },
        success: function (data) {
            //状态判断
            if (data.status == 1) {
                window.location.reload();
            }
        }
    });
}
//查询下级分类
function queryProductCategoryList(obj, selectId) {
    var parentId = $(obj).val();
    $.ajax({
        url: contextPath + "/admin/productCategory/queryProductCategoryList",
        method: "post",
        data: {
            parentId: parentId
        },
        success: function (data) {
            //状态判断
            if (data.status == 1) {
                var options = "<option value=''>" + "请选择..." + "</option>";
                for (var i = 0; i < data.data.length; i++) {
                    var option = "<option value=" + data.data[i].id + ">" + data.data[i].name + "</option>";
                    options = options + option;
                }
                $("#" + selectId).html(options);
            }
        }
    });
}
//修改
function toUpdateProductCategoryList(obj) {
    var id = $(obj).val();
    $.ajax({
        url: contextPath + "/admin/productCategory/toUpdateProductCategory",
        method: "post",
        data: {
            id: id
        },
        success: function (data) {
            $("#addProductCategory").html(data);
        }
    });
}
//修改
function saveOrUpdate() {
    var id = $("#id").val();
    if (id == null || id == "") {
        addProductCategory();
    } else {
        modifyProductCategory();
    }
}
function modifyProductCategory() {
    var id = $("#id").val();
    var productCategoryLevel1 = $("#productCategoryLevel1").val();
    var productCategoryLevel2 = $("#productCategoryLevel2").val();
    var name = $("#name").val();
    var type = $("#type").val();
    $.ajax({
        url: contextPath + "/admin/productCategory/modifyProductCategory",
        method: "post",
        data: {
            id: id,
            name: name,
            type: type,
            productCategoryLevel1: (productCategoryLevel1 == null || productCategoryLevel1 == "") ? 0 : productCategoryLevel1,
            productCategoryLevel2: (productCategoryLevel2 == null || productCategoryLevel2 == "") ? 0 : productCategoryLevel2
        },
        success: function (data) {
            //状态判断
            if (data.status == 1) {
                window.location.reload();
            }
        }
    });
};
//选择商品分类级别
function selectProductCategoryLevel(obj) {
    var level = $(obj).val();
    if (level == 1) {
        $('#productCategoryLevel1').parent().parent().hide();
        $('#productCategoryLevel2').parent().parent().hide();
    } else if (level == 2) {
        $('#productCategoryLevel1').parent().parent().show();
    } else {
        $('#productCategoryLevel1').parent().parent().show();
        $('#productCategoryLevel2').parent().parent().show();
    }
}
//删除商品分类
function deleteProductCategory(id,type) {
 var bool=window.confirm("确认删除此分类信息么?");
	if(bool){
		$.ajax({
	        url: contextPath + "/admin/productCategory/deleteProductCategory",
	        method: "post",
	        data: {
	            id: id,
	            type: type
	        },
	        success: function (data) {
	            if (data.status == 1) {
	                window.location.reload();
	            }else{
	            	showMessage(data.message);
	            }
	        }
	    });
	}
}
//商品发布的是很检查相关字段
function checkProduct() {
    var productCategoryLevel1 = $("#productCategoryLevel1").val();
    var productCategoryLevel2 = $("#productCategoryLevel2").val();
    var productCategoryLevel3 = $("#productCategoryLevel3").val();
    var name = $("#name").val();
    var price = $("#price").val();
    var stock = $("#stock").val();
    if (productCategoryLevel1 == null || productCategoryLevel1 == "") {
        showMessage("请选择商品分类");
        return false;
    }
    if (name == null || name == "") {
        showMessage("清填写商品名称");
        return false;
    }
    if (name.length < 2 || name > 16) {
        showMessage("商品名称为2到16个字符");
        return false;
    }
    if (price == null || price == "") {
        showMessage("清填写商品价格");
        return false;
    }
    if (stock == null || stock == "") {
        showMessage("清填写商品库存");
        return false;
    }
}
// 删除商品
function deleteById(id) {
	var bool=window.confirm("确认删除此商品信息么?");
	if(bool){
		$.ajax({
	        url: contextPath + "/admin/product/deleteById",
	        method: "post",
	        data: {
	            id: id
	        },
	        success: function (data) {
	            if (data.status == 1) {
	                window.location.reload();
	            }
	        }
	    });
	}
}
/**
 * 检查用户
 */
function checkUser() {
    var loginName = $("input[name='loginName']").val();
    var userName = $("input[name='userName']").val();
    var identityCode = $("input[name='identityCode']").val();
    var email = $("input[name='email']").val();
    var mobile = $("input[name='mobile']").val();
    var type = $("select[name='type']").val();
    var password = $("input[name='password']").val();
    var repPassword = $("input[name='repPassword']").val();
    var id = $("input[name='id']").val();

    if (loginName == null || loginName == "") {
        showMessage("请填写登录用户名");
        return false;
    }
    
    if(loginName.length<2 || loginName>10){
        showMessage("登录名不能小于两个字符或者大于十个字符.");
        return false;
    }

    if (userName == null || userName == "") {
        showMessage("请填写真实姓名");
        return false;
    }
    if(id==null || id=="" || id==0){
    	if (password == null || password == "") {
            showMessage("请填写密码");
            return false;
        }
        
        if (password !=repPassword) {
            showMessage("两次输入密码不一致");
            return false;
        }
    }

    if(email!=null && email!="" && !checkMail(email)){
    	showMessage("邮箱格式不正确");
        return false;
    }
    //验证邮箱格式
    if(mobile!=null && mobile!="" && !checkMobile(mobile)){
    	showMessage("手机格式不正确");
        return false;
    }
     //验证邮箱格式
    if(identityCode!=null && identityCode!="" && !checkIdentityCode(identityCode)){
    	showMessage("身份证号格式不正确");
        return false;
    }
    return true;
}

// 添加用户
function addUser() {
    if(!checkUser()){
    	return false;
    }
    var loginName = $("input[name='loginName']").val();
    var userName = $("input[name='userName']").val();
    var identityCode = $("input[name='identityCode']").val();
    var email = $("input[name='email']").val();
    var mobile = $("input[name='mobile']").val();
    var type = $("select[name='type']").val();
    var id = $("input[name='id']").val();
    var password = $("input[name='password']").val();
    $.ajax({
        url: contextPath + "/admin/user/updateUser",
        method: "post",
        data: {
           id: id,
           loginName: loginName,
           userName: userName,
           identityCode: identityCode,
           email: email,
           mobile: mobile,
           type: type,
           password:password
        },
        success: function (data) {
            if (data.status == 1) {
                window.location.href=contextPath+"/admin/user/queryUserList";
            }else{
            	showMessage(data.message);
            }
        }
    });
}
/**
 * 删除用户
 * @param id
 */
function deleteUserId(id) {
	var bool=window.confirm("确认删除此用户信息么?");
	if(bool){
		$.ajax({
	        url: contextPath + "/admin/user/deleteUserById",
	        method: "post",
	        data: {
	            id: id,
	        },
	        success: function (data) {
	            if (data.status == 1) {
	                window.location.reload();
	            }
	        }
	    });
	}
}


function checkMail(mail) {
  var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  if (filter.test(mail)) 
	  return true;
  else {
	 return false;}
}

function checkMobile(phone) {
  var filter  = /^\d{5,11}$/;
  if (filter.test(phone)) 
	  return true;
  else {
	 return false;
  }
}

function checkIdentityCode(identityCode) {
  var filter  = /^\w{18}$/;
  if (filter.test(identityCode)) 
	  return true;
  else {
	 return false;
  }
}
