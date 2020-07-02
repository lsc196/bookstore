"use strict";

$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getCategoryByName(pageIndex, pageSize) {
        $.ajax({
            url: "/manage/category",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "name":$("#searchCategoryName").val()
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getCategoryByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchCategoryNameBtn").click(function() {
        getCategoryByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addCategory").click(function() {
        $.ajax({
            url: "/manage/category/add",
            success: function(data){
                $("#CategoryFormContainer").html(data);
            },
            error : function(data) {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑用户的界面
    $("#rightContainer").on("click",".category-edit", function () {
        $.ajax({
            url: "/manage/category/edit/" + $(this).attr("cId"),
            success: function(data){
                $("#CategoryFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    // 提交变更后，清空表单
    $("#submitEdit").click(function() {
        $.ajax({
            url: "/manage/category",
            type: 'POST',
            data:$('#categoryForm').serialize(),  //在add.html页面有id="userForm"的form表单
            success: function(data){
                $('#categoryForm')[0].reset();
                // alert("data.success前");
                if (data.success) {
                    // 从新刷新主界面
                    // alert("data.success后");
                    getCategoryByName(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }

            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    // 删除用户
    $("#rightContainer").on("click",".category-delete", function () {
        var result = confirm("确定删除吗？");
        if(result){
            $.ajax({
                url: "/manage/category/" + $(this).attr("cId") ,
                type: 'DELETE',
                contentType:"application/json",
                dataType:"json",
                success: function(data){
                    if (data.success) {
                        // 从新刷新主界面
                        getCategoryByName(0, _pageSize);
                    } else {
                        toastr.error(data.message);
                    }
                },
                error : function() {
                    toastr.error("error!");
                }
            });
        }

    });
});