# 靓码行

靓码行是一款基于 Jetpack Compose 开发的 Android 数字组合排列与筛选应用，提供今日运势、靓码计算和历史记录三个主要模块。
PS: 这是个华南f3阿姨阿叔打奖票的辅助工具，用于根据他们设定的规则排出符合规则的靓码，纯属娱乐，请勿用于赌博。

## 功能简介

- **主页**：展示当天的运势等级、宜、忌、幸运数字和对应文案。
- **靓码计算**：支持输入多组数字组合，设置主要组合和参与位数，生成四位数字排列结果。
- **结果筛选**：支持数位值、不定位数、数位和值、性质、重复值和确认定数等筛选条件。
- **历史记录**：按日期保存计算结果，每天最多保留最新三条记录，并自动清理五天以前的数据。
- **结果操作**：支持复制结果、返回筛选条件继续修改，以及完成计算并返回主页。
- **自适应布局**：针对不同屏幕宽度和高度调整内容区域、按钮、输入框及结果展示区域。

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- Compose Adaptive Navigation Suite
- Kotlin Coroutines
- `StateFlow` 与 `ViewModel`
- Preferences DataStore：保存当天运势数据
- Room Persistence Library：保存历史计算记录
- KSP：生成 Room 编译代码

## 环境要求

- Android Studio，建议使用支持当前项目 Gradle 和 Kotlin 版本的版本
- JDK 11
- Android SDK 35
- 最低 Android 版本：Android 12（API 31）
- 目标 Android 版本：API 35

项目的应用包名为 `com.example.nicecode`，应用显示名称为“靓码行”。

## 项目结构

```text
nicecode/
├─ app/
│  └─ src/main/
│     ├─ java/com/example/nicecode/
│     │  ├─ MainActivity.kt              # 应用入口、底部导航和页面切换
│     │  ├─ HomeScreen.kt                 # 主页入口
│     │  ├─ CountScreen.kt                # 计算模块入口
│     │  ├─ HistoryScreen.kt              # 历史记录模块入口
│     │  ├─ components/                   # 可复用 Compose 组件
│     │  ├─ pages/                        # 各页面的具体 UI
│     │  ├─ func/                         # 运势、排列、筛选、历史记录逻辑
│     │  └─ ui/theme/                     # 主题、颜色和字体配置
│     └─ res/                             # 图标、字符串、主题等资源
├─ gradle/
│  └─ libs.versions.toml                  # 依赖和插件版本管理
├─ build.gradle.kts                       # 根项目构建配置
├─ settings.gradle.kts                    # 模块和仓库配置
└─ gradlew / gradlew.bat                  # Gradle Wrapper
```

## 版本说明

当前版本：`1.0`
版本号：`1`
