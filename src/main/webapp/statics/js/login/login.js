/**
 * Created by bdqn on 2016/5/3.
 */
//登录的方法
function login(){
    var loginName=$("#loginName").val();
    var password=$("#password").val();
    $.ajax({
        url:contextPath+"/login",
        method:"post",
        data:{loginName:loginName,password:password},
        success:function(data){
            if(data.status==1){
                window.location.href=contextPath+"/index";
            }else{
                showMessage(data.message)
            }
        }
    })
}