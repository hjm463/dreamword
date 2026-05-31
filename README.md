# 单词学习 - Android (Java版)

梦幻国风水墨风格的单词学习 App。

## 功能
- 添加单词（英文 + 中文）
- 背诵复习（显示中文，输入英文）
- 单词簿管理
- 朗读发音（有道词典API）
- 飘落花瓣动画

## 技术栈
- Java
- Android SDK 34
- Material Design Components
- Gson (数据持久化)

## 构建

### 方式1：Android Studio
1. 用 Android Studio 打开此目录
2. 等待 Gradle 同步
3. Build → Generate Signed Bundle/APK

### 方式2：命令行
```bash
./gradlew assembleDebug
```
APK 输出在 `app/build/outputs/apk/debug/`

## 项目结构
```
android_java/
├── app/
│   ├── src/main/java/com/dreamword/
│   │   ├── MainActivity.java      # 主界面
│   │   ├── AddActivity.java       # 添加单词
│   │   ├── ReviewActivity.java    # 背诵复习
│   │   ├── BookActivity.java      # 单词簿
│   │   ├── data/
│   │   │   ├── WordData.java      # 单词数据模型
│   │   │   └── WordStore.java     # 数据存储
│   │   └── view/
│   │       └── FallingPetalsView.java  # 飘落花瓣动画
│   └── src/main/res/              # 布局、样式、资源
├── build.gradle
└── settings.gradle
```

## 配色方案
- 主背景：深青渐变 #1A4A4A → #0D2B2B
- 主色调：青色 #2D8B8B
- 强调色：金色 #C9A962
- 文字：奶油白 #F5F0E8
