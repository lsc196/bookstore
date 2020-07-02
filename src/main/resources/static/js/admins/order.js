"use strict";

$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getOrdersByNoLike(pageIndex, pageSize) {
        $.ajax({
            url: "/manage/order",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "orderNo":$("#searchOrderNo").val()
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
        getOrdersByNoLike(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchOrderNoBtn").click(function() {
        getOrdersByNoLike(0, _pageSize);
    });



    // 删除订单
    $("#rightContainer").on("click",".order-delete", function () {
        var result = confirm("确定删除吗？");
        if(result){
            $.ajax({
                url: "/manage/order/" + $(this).attr("orderNo") ,
                type: 'DELETE',
                contentType:"application/json",
                dataType:"json",
                success: function(data){
                    if (data.success) {
                        // 从新刷新主界面
                        getOrdersByNoLike(0, _pageSize);
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