package com.simbest.cloud;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Java文件列表生成工具类
 * 根据指定的package名称，递归遍历该package目录及其子目录下的所有Java文件，
 * 并在docs目录下输出一个PackageFiles.md文件，文件内容为表格，字段定义如下：
 * 序号，文件名称，文件类型（Java文件类型），文件路径，标记（默认为0）
 *
 * @date 2023-07-12
 */
@Slf4j
public class ListFilesByPackage {
    
    /**
     * Java文件信息内部类
     */
    private static class JavaFileInfo {
        int index;
        String className;
        String packagePath;
        int mark;

        public JavaFileInfo(int index, String className, String packagePath, int mark) {
            this.index = index;
            this.className = className;
            this.packagePath = packagePath;
            this.mark = mark;
        }
    }

    /**
     * 主方法入口
     */
    public static void main(String[] args) {
        // 源代码路径
        String srcPath = "src/main/java";

        // 输出文件路径
        String outputPath = "docs/PackageFiles.md";

        // 固定的包名
        String packageName = "com.simbest.cloud.cores.utils";

        // 存储Java文件信息的列表
        List<JavaFileInfo> javaFiles = new ArrayList<>();

        try {
            // 将包名转换为文件路径
            String packagePath = packageName.replace('.', '/');
            Path startPath = Paths.get(srcPath, packagePath);

            // 检查起始路径是否存在
            if (!Files.exists(startPath)) {
                log.warn("指定的包路径不存在: {}", startPath);
                return;
            }

            // 遍历收集Java文件信息
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                int fileIndex = 1;

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith(".java")) {
                        // 获取包路径
                        String fullPackagePath = file.toString()
                                .replace('\\', '/')
                                .replace(srcPath + "/", "")
                                .replace(".java", "")
                                .replace('/', '.');

                        javaFiles.add(new JavaFileInfo(
                                fileIndex++,
                                fileName.replace(".java", ""),
                                fullPackagePath,
                                0 // 默认标记为0
                        ));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    log.warn("访问文件失败: {}, 错误: {}", file, exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });

            // 确保docs目录存在
            Path docsPath = Paths.get("docs");
            if (!Files.exists(docsPath)) {
                Files.createDirectories(docsPath);
            }

            // 生成markdown文件
            try (FileWriter writer = new FileWriter(outputPath)) {
                // 写入表头
                writer.write("# " + packageName + " 包下的Java文件列表\n\n");
                writer.write("| 序号 | 类名 | 包路径 | 标记 |\n");
                writer.write("|------|------|--------|------|\n");

                // 写入文件信息
                for (JavaFileInfo file : javaFiles) {
                    writer.write(String.format("| %d | %s | %s | %d |\n",
                            file.index,
                            file.className,
                            file.packagePath,
                            file.mark));
                }

                log.info("文件已生成：" + outputPath);
                log.info("共处理 " + javaFiles.size() + " 个Java文件");
            }

        } catch (IOException e) {
            log.error("生成文件列表时发生错误: {}", e.getMessage());
        }
    }
}
