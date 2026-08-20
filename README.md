# 靓码行

靓码行是一款基于 Jetpack Compose 开发的 Android 数字组合排列与筛选应用，包含今日运势、靓码计算和历史记录功能。
PS: 这是一个给阿姨阿叔打奖票的辅助工具，用于根据他们设定的规则排出符合规则的靓码，纯属娱乐，请勿用于赌博。
## 主要功能

- 今日运势：展示每日固定的运势等级、宜忌和幸运数字。
- 靓码计算：输入数字组合，生成四位数字排列结果，并支持多种筛选条件。
- 历史记录：按日期保存最近的计算结果，每天最多保留三条记录。
- 自适应界面：针对不同屏幕宽度调整输入框、按钮和结果展示区域。

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- Kotlin Coroutines、StateFlow 和 ViewModel
- Preferences DataStore
- Room Persistence Library
- KSP

## 项目结构

```text
app/src/main/
  java/com/example/nicecode/
    components/  可复用 Compose 组件
    pages/       页面内容
    func/        运势、排列、筛选和历史记录逻辑
    ui/theme/    主题、颜色和字体
  res/           图标及其他 Android 资源
gradle/          Gradle 版本和依赖配置
```

## 版本信息

- 应用名称：靓码行
- 最低 Android 版本：API 31
- 目标 Android 版本：API 35
- JDK：11

本项目仅供娱乐使用，不应作为博彩或其他金融决策依据。
