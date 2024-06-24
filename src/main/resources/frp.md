### 1. 服务端(centos)安装

```
wget https://github.com/fatedier/frp/releases/download/v0.58.1/frp_0.58.1_linux_amd64.tar.gz
tar -zxvf frp_0.58.1_linux_amd64.tar.gz
cd frp_0.58.1_linux_amd64/
vim frps.ini
mkdir -p /etc/frp
cp frps.ini /etc/frp
cp frps /usr/bin
yum install systemd
vim /etc/systemd/system/frps.service
# 启动frps
systemctl start frps
# 停止frps
systemctl stop frps
# 重启frps
systemctl restart frps
# 查看frps状态
systemctl status frps
# 开机启动
systemctl enable frps
# 开放端口
iptables -I INPUT -p tcp --dport 7000 -j ACCEPT
# 端口文件保存
iptables-save
```

### 2. 客户端(windows )安装

```
解压文件夹下 新建 frpc.ini 文件 , cmd 运行
frpc.exe -c frpc.ini
```

### 3.  文件

### frps.ini

```
[common]
# 客户端运行报错添加
vhost_http_port = 7001
# frp监听的端口，默认是7000，可以改成其他的
bind_port = 7000
# 授权码,token之后在客户端会用到
token = adfe338$3f3(34rfsef3
subdomain_host = www.pycur.com
# frp管理后台端口，请按自己需求更改
dashboard_port = 7900
# frp管理后台用户名和密码，请改成自己的
dashboard_user = admin
dashboard_pwd = admin_524
enable_prometheus = true

# frp日志配置
log_file = /var/log/frps.log
log_level = info
log_max_days = 3
```

### frps.service

```
[Unit]
# 服务名称，可自定义
Description = frp server
After = network.target syslog.target
Wants = network.target

[Service]
Type = simple
# 启动frps的命令，需修改为您的frps的安装路径
ExecStart = /usr/bin/frps -c /etc/frp/frps.ini

[Install]
WantedBy = multi-user.target
```

### frpc.ini

```
[common]
server_addr = 139.84.194.82
# 请换成设置的服务器端口
server_port = 7000
token = adfe338$3f3(34rfsef3

# 配置ssh服务
[ssh]
type = tcp
local_ip = 127.0.0.1
local_port = 22
remote_port = 21328

# 配置http服务，可用于小程序开发、远程调试等
[web]
type = http
local_ip = 127.0.0.1
local_port = 19093
remote_port = 7001
custom_domains = 139.84.194.82
```
