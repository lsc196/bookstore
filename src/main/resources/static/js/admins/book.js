"use strict";

$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBooksByName(pageIndex, pageSize) {
        $.ajax({
            url: "/manage/book",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "name":$("#searchBookName").val()
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
        getBooksByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchBookNameBtn").click(function() {
        getBooksByName(0, _pageSize);
    });

    // 获取添加图书的界面
    $("#addBook").click(function() {
        $.ajax({
            url: "/manage/book/add",
            success: function(data){
                $("#bookFormContainer").html(data);
            },
            error : function(data) {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑用户的界面
    $("#rightContainer").on("click",".book-edit", function () {
        $.ajax({
            url: "/manage/book/edit/" + $(this).attr("bookId"),
            success: function(data){
                $("#bookFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    // 提交变更后，清空表单
    $("#submitEdit").click(function() {
        var formData = new FormData($("#bookForm")[0]);//表单id
        $.ajax({
            url: "/manage/book",
            type: 'POST',
            data:formData,  //在add.html页面有id="userForm"的form表单
            cache: false,
            contentType: false,
            processData: false,
            success: function(data){
                $('#bookForm')[0].reset();
                // alert("data.success前");
                if (data.success) {
                    // 从新刷新主界面
                     //alert("data.success后");
                    getBooksByName(0, _pageSize);
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
    $("#rightContainer").on("click",".book-delete", function () {
        var result = confirm("确定删除吗？");
        if(result){
            $.ajax({
                url: "/manage/book/" + $(this).attr("bookId") ,
                type: 'DELETE',
                contentType:"application/json",
                dataType:"json",
                success: function(data){
                    if (data.success) {
                        // 从新刷新主界面
                        getBooksByName(0, _pageSize);
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