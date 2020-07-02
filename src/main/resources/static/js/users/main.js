"use strict";
$(function () {

    var _pageSize; // 存储用于搜索

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        var flag = $("#flag").html();  //用于标识是分类查找还是通过书名查找
        console.log(flag);
        if (flag == "category") {
            location.href = "/bookCategory?category=" + $("#bookNav").html() + '&sort=' + $("#sort").val() + '&pageIndex=' + pageIndex;
        } else {
            location.href = "/books?name=" + $("#search-input").val() + '&sort=' + $("#sort").val() + '&pageIndex=' + pageIndex;
        }
        _pageSize = pageSize;
    });

    // 搜索
    $("#search-btn").click(function () {
        location.href = "/books?name=" + $("#search-input").val() + '&pageIndex=0';
    });

    $("#sort").change(function () {
        var flag = $("#flag").html();  //用于标识是分类查找还是通过书名查找
        if (flag == "category") {
            location.href = "/bookCategory?category=" + $("#bookNav").html() + '&sort=' + $("#sort").val();
        } else {
            location.href = "/books?name=" + $("#bookNav").html() + '&sort=' + $("#sort").val();
        }

    });


    //加入购物车
    $("#addCart").click(function () {
        $.post("/cart/add",
            {
                bid: $("#bid").val(),
                quantity: $("#quantity").val()
            },
            function (data, status) {
                alert("添加购物车成功！" + "\n状态: " + status);
            });
    });

    //未登录点击加入购物车按钮
    $("#unAddCart").click(function () {
        alert("请登录，登录后才可以加入购物车")
    });

    $("#unbuy").click(function () {
        alert("请登录，登录后才可以立即购买")
    });
    // 删除购物车
    $("#cart_table").on("click", ".cart_delete", function () {
        console.log("删除购物车产品：" + $(this).attr("data_cartid"));
        var result = confirm("确定删除吗？");
        if (result) {
            $.ajax({
                url: "/cart/delete",
                type: 'post',
                data: {
                    cartId: $(this).attr("data_cartid")
                },
                success: function (data) {
                    if (data.success) {
                        // 从新刷新主界面
                        location.reload();

                    } else {
                        toastr.error(data.message);
                    }
                },
                error: function () {
                    toastr.error("error!");
                }
            });
        }
    });

    function calsum() {
        var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
        var sum = 0.00;
        var count = Check.length;
        if (count > 0) {
            console.log("选中商品共" + count + " 件");
            Check.each(function () {//遍历
                var row = $(this).parent("div").parent("td").parent("tr");//获取选中行
                var id = row.find("[name='cart_id']").html();//获取name='cart_id'的值，就是购物车id
                console.log(id);
                var s = row.find("[name='sum-one']").html();
                sum += parseFloat(s);
            });
            console.log(sum);
            $("#product_count").html(count);
            $("#sum-price").html(sum);
            $("#jiesuan").attr("disabled", false);
            $("#jiesuan").css("background-color", "red");
        } else {
            $("#product_count").html(0);
            $("#sum-price").html(0.00);
            $("#jiesuan").attr("disabled", true);
            $("#jiesuan").css("background-color", "#555555");
        }

    }

    //全选事件
    $("#check-all").click(function () {
        var xz = $(this).prop("checked");//判断全选按钮的选中状态
        var ck = $(".check-one").prop("checked", xz);  //让class名为qx的选项的选中状态和全选按钮的选中状态一致
        calsum();
    });

    //单个checkbox改变事件
    $(".check-one").click(function () {

        calsum();
    });

    //减数量按钮事件
    $(".sub").click(function () {
        console.log("进入减数量按钮事件");
        var row = $(this).parent("div").parent("td").parent("tr");//获取选中行
        var id = row.find("[name='cart_id']").html();//获取name='cart_id'的值，就是购物车id
        console.log(id);
        var s = row.find("[name='cartQuantity']").val();
        var ss = parseInt(s) - 1;
        console.log(ss);
        if (ss != 0) {
            $.ajax({
                url: "/cart/quantity/dec",
                type: 'post',
                data: {
                    cartId: id
                },
                success: function (data) {
                    if (data.success) {

                        row.find("[name='cartQuantity']").val(ss);
                        var price = row.find("[name='price']").html();
                        var sum = Math.round((parseFloat(price) * ss) * 100) / 100;
                        row.find("[name='sum-one']").html(sum);
                        calsum();
                    } else {
                        toastr.error(data.message);
                    }
                },
                error: function () {
                    toastr.error("error!");
                }
            });
        }


    });

    //加数量按钮事件
    $(".add").click(function () {
        console.log("进入加数量按钮事件");
        var row = $(this).parent("div").parent("td").parent("tr");//获取选中行
        var id = row.find("[name='cart_id']").html();//获取name='cart_id'的值，就是购物车id
        console.log(id);
        $.ajax({
            url: "/cart/quantity/add",
            type: 'post',
            data: {
                cartId: id
            },
            success: function (data) {
                if (data.success) {
                    var s = row.find("[name='cartQuantity']").val();
                    var ss = parseInt(s) + 1;
                    console.log(ss);
                    row.find("[name='cartQuantity']").val(ss);
                    var price = row.find("[name='price']").html();
                    var sum = Math.round((parseFloat(price) * ss) * 100) / 100;
                    row.find("[name='sum-one']").html(sum);
                    console.log(sum);
                    calsum();
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });

    });

    $("#delete_select").click(function () {
        var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
        var sum = 0.00;
        var count = Check.length;
        if (count > 0) {
            console.log("选中删除商品共" + count + " 件");
            var result = confirm("确定删除吗？");
            if (result) {
                Check.each(function () {//遍历
                    var row = $(this).parent("div").parent("td").parent("tr");//获取选中行
                    var id = row.find("[name='cart_id']").html();//获取name='cart_id'的值，就是购物车id
                    console.log(id);
                    $.ajax({
                        url: "/cart/delete",
                        type: 'post',
                        data: {
                            cartId: id
                        },
                        error: function () {
                            toastr.error("error!");
                        }
                    });
                });
                location.reload();
            }
        }else {
            alert("请选择需要删除的商品");
        }
    });


$("#jiesuan").click(function () {
    console.log("点击了结算，进入确认订单页面");
    var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
    var cartIds = [];
    Check.each(function () {//遍历
        var row = $(this).parent("div").parent("td").parent("tr");//获取选中行
        var id = row.find("[name='cart_id']").html();//获取name='cart_id'的值，就是购物车id
        cartIds.push(id);
    });
    $.ajax({
        url: "/order",
        type: 'get',
        traditional: true,  //传数组需要
        data: {
            cartIds: cartIds
        },
        success: function (data) {
            $("#cartContainer").html(data);
        },
        error: function () {
            toastr.error("error!");
        }
    });

});

$("#buy").click(function () {
    console.log("点击了立即购买，进入确认订单页面");
    window.open("/buyNow?bid=" + $("#bid").val() + "&quantity=" + $("#quantity").val());
});

// 删除订单
$("#orderContainer").on("click", ".order-delete", function () {
    var result = confirm("确定删除该订单吗？");
    if (result) {
        $.ajax({
            url: "deleteOrder/" + $(this).attr("orderNo"),
            type: 'DELETE',
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    // 从新刷新主界面
                    location.reload();
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

});

})
;

