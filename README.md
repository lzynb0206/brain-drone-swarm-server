# brain-drone-swarm-server

脑控无人机蜂群作战系统的本地认证后端。提供注册、登录和 JWT 身份接口，账号保存在 PostgreSQL 的 `app_users` 表中。

## 本机运行

数据库默认配置：

- 地址：`127.0.0.1:5432`
- 数据库：`brain_drone_swarm`
- 用户：`postgres`
- 默认密码：`postgres`

如果你的 PostgreSQL 密码不是 `postgres`，先设置环境变量：

```bash
export DB_PASSWORD="你的 PostgreSQL 密码"
export JWT_SECRET="至少32字节的本机随机密钥"
./mvnw -s .mvn/project-settings.xml spring-boot:run
```

第一次启动时 Flyway 会自动创建 `app_users` 表。注册密码通过 BCrypt（强度 12）生成带随机盐的哈希；数据库不会保存明文密码。JWT 默认 60 分钟过期。

如果提示 `Connection refused` / `尝试连线已失败`，说明 PostgreSQL 服务尚未启动。pgAdmin 只是数据库管理客户端，打开 pgAdmin 本身不等于 PostgreSQL 服务已经运行。

本机通过 PostgreSQL 18 安装器安装时，可以在终端启动服务（命令会要求输入 macOS 管理员密码）：

```bash
sudo launchctl load -w /Library/LaunchDaemons/postgresql-18.plist
```

## 接口

- `POST /api/auth/register`：注册并返回 JWT
- `POST /api/auth/login`：登录并返回 JWT
- `GET /api/auth/me`：携带 `Authorization: Bearer <token>` 查询当前用户
- `GET /api/health`：后端存活检查

当前为了本地课题演示允许注册时选择角色。正式部署时设置：

```bash
export ALLOW_REGISTRATION_ROLE_SELECTION=false
```

届时新注册账号一律为 `soldier`，高级身份应由管理员授权。
