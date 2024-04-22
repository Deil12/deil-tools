// sa
var admin = {};

// 打开loading
admin.loading = function (msg) {
    layer.closeAll();// 开始前先把所有弹窗关了
    return layer.msg(msg, {icon: 16, shade: 0.3, time: 1000 * 20, skin: 'ajax-layer-load'});
};

// 隐藏loading
admin.hideLoading = function () {
    layer.closeAll();
};

// 登录事件

$('.loginBtn').click(function () {
    admin.loading("正在登录...");
    // 开始登录
    $("input[type='submit']").attr("disabled", true);// 设置disabed属性
    $.ajax({
        url: "doLogin",
        type: "post",
        data: {
            name: $('[name=name]').val(),
            pwd: $('[name=pwd]').val()
        },
        dataType: 'json',
        success: function (res) {
            console.log('返回数据：', res);
            admin.hideLoading();
            if (res.code == 200) {
                layer.msg('登录成功', {anim: 0, icon: 6});
                setTimeout(function () {
                    // location.reload();
                    location.replace('/');
                }, 800)
            } else {
                layer.msg(res.msg, {anim: 6, icon: 2});
                $("[name=pwd]").focus();
            }
        },
        error: function (xhr, type, errorThrown) {
            admin.hideLoading();
            if (xhr.status == 0) {
                return layer.alert('无法连接到服务器，请检查网络');
            }
            return layer.alert("异常：" + JSON.stringify(xhr));
        }
    });
    $("input[type='submit']").attr("disabled", false);// 移除disabed属性
    return false;
});

// 输入框获取焦点
$("[name=pwd]").focus();

// 打印信息
var str = "This page is provided by Deil";
console.log(str);
