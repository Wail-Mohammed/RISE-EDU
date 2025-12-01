#!/bin/bash

echo "=========================================="
echo "编译源代码和测试代码..."
echo "=========================================="

# 编译源代码
echo "编译源代码..."
javac -d bin -sourcepath src $(find src/app -name "*.java" -type f 2>/dev/null)

if [ $? -ne 0 ]; then
    echo "❌ 源代码编译失败！"
    exit 1
fi

# 编译测试代码
echo "编译测试代码..."
javac -d bin -sourcepath src -cp "lib/junit-platform-console-standalone-1.9.3.jar:bin" $(find src/JunitTests -name "*.java" -type f 2>/dev/null)

if [ $? -ne 0 ]; then
    echo "❌ 测试代码编译失败！"
    exit 1
fi

echo "✅ 编译成功！"
echo ""
echo "=========================================="
echo "运行SystemManagerTester测试..."
echo "=========================================="

# 运行SystemManagerTester
java -jar lib/junit-platform-console-standalone-1.9.3.jar \
  --class-path bin \
  --select-class Server.SystemManagerTester \
  --details verbose

echo ""
echo "=========================================="
echo "测试完成！"
echo "=========================================="

