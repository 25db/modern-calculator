# Modern Calculator (Android / Kotlin + Jetpack Compose)

现代深色风格计算器 App，目标 Android 14–16（compileSdk 35 / targetSdk 35，minSdk 24）。

## 特性
- 纯 Kotlin + Jetpack Compose（Material3），深色现代 UI
- 大圆角按钮 + ripple 触感反馈，等号/运算符高亮配色
- 表达式历史行（如 `12 + 3 =`），长按 ⟲ 一键清零
- 支持：`+ − × ÷`、`±`、`⌫`、`C`、小数点；除零/溢出显示 `Error`
- 结果显示 15 位内自适应字号

## 目录
```
calculator-app/
├── app/
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── res/values/themes.xml
│   │   └── java/com/example/moderncalculator/
│   │       ├── MainActivity.kt
│   │       └── ui/
│   │           ├── CalculatorScreen.kt   # UI + 状态机
│   │           └── theme/Theme.kt
│   └── src/test/java/.../CalcLogicTest.kt  # 纯逻辑单测（可选）
├── settings.gradle.kts
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

## 构建
```bash
cd calculator-app
gradle assembleDebug
# 产物: app/build/outputs/apk/debug/app-debug.apk
```
> 需要 Android SDK + JDK 17。用 Android Studio 打开可直接运行。
> 逻辑单测（不依赖 Android）：`gradle :app:testDebugUnitTest`

## 适配说明
- 无敏感权限、无后台服务，不需要额外的 14/15 运行时适配。
- 16 的返回手势：单 Activity 单屏，默认行为即可；如需拦截自定义返回可加 `OnBackInvokedCallback`。
- 打包 16KB 内存页（Android 15+ 强制）：当前无本地 .so，默认 64KB 对齐即可，若日后引入 native 库需设 `android:extractNativeLibs` 和 so 对齐。

## 想加什么（下一步可扩展）
- 键盘输入（`KeyEvent` / 遥控器 / 车载）
- 百分比 `%`、括号、科学模式
- 主题切换（浅/深/跟随系统）
- 历史记录列表持久化（DataStore/Room）
