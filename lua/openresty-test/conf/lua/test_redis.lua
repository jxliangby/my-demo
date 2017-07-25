local function close_redis(red)
    if not red then
        return
    end
    local ok, err = red:close()
    if not ok then
        ngx.say("close redis error : ", err)
    end
end

local redis = require("resty.redis")

--创建实例
local red = redis:new()
--设置超时（毫秒）
red:set_timeout(1000)
--建立连接
local ip = "10.0.53.65"
local port = 7000
local ok, err = red:connect(ip, port)
if not ok then
    ngx.say("connect to redis error : ", err)
    return close_redis(red)
end
--调用API进行处理
local test_key = "lua_test"
ok, err = red:set(test_key, "hello world")
if not ok then
    ngx.say("set msg error : ", err)
    return close_redis(red)
end

--调用API获取数据
local resp, err = red:get(test_key)
if not resp then
    ngx.say("get msg error : ", err)
    return close_redis(red)
end
--得到的数据为空处理
if resp == ngx.null then
    resp = ''  --比如默认值
end
ngx.say("msg : ", resp)


local hget_resp,err = red:hget("com.crt.openapi.modules.common.domain.entity.ApiApp","test:app_id:T000001702QY")
if not hget_resp then
    ngx.say("get msg error : ", err)
    return close_redis(red)
end
--得到的数据为空处理
if hget_resp == ngx.null then
    hget_resp = ''  --比如默认值
end
ngx.say("hget_resp : ", hget_resp)


close_redis(red)
