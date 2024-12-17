#FROM python:3.12-slim
FROM pybase:latest

# 设置工作目录
WORKDIR /frpcpy

# 复制项目文件到容器中
COPY akshare_stock_realtime.py /frpcpy
#COPY requirements.txt /frpcpy

# 设置时区为东八区（中国标准时间）
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

# 安装依赖
#RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt

# 暴露端口
EXPOSE 7010

# 设置容器启动时运行的命令
CMD ["python3", "akshare_stock_realtime.py"]
