package com.simbest.cloud;

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

import lombok.extern.slf4j.Slf4j;

/**
 * Java文件列表生成工具类
 * 编写main函数，递归遍历src/main/java/com/simbest/boot/security目录的java文件，并在docs目录输出一个JavaFiles.md文件，文件内容为表格，字段定义如下：
 * 序号，文件名称，文件类型（Java文件类型），文件路径，标记（默认为0）
 * 
 * 
 * 上述文件生成后，转至cursor执行，模型选择auto，提示词如下：
 * 读取JavaFiles.md文件，实现文件中所有java文件添加或修正注释，并且删除文件版权信息和作者信息。要求如下：
 * 1、读取docs目录下的JavaFiles.md文件，循环遍历所有标记为0的所有文件
 * 2、根据文件路径定位到该文件，为此文件添加或修正注释，并且删除文件版权信息和作者信息，完成后将标记从0改为1，直到循环结束。
 * 
 * 上述任务处理完成后，转至cursor执行，模型选择auto，提示词如下：
 * 为我在docs目录下新增一个User_Organization_Specification.md文件，要求如下：
 * 1、读取docs目录下的JavaFiles.md文件，循环遍历所有标记为0的所有文件
 * 2、将文件的名称、类型、文件中所有字段和字段注释含义，追加写入到User_Organization_Specification.md文件中
 * 3、然后在JavaFiles.md文件中将该文件标记为1后，处理下一个文件
 * 4、所有文件处理完毕，均标记为1后，最后再向我反馈共计处理完成多少个文件
 * 
 * 
 * @date 2023-07-12
 */
@Slf4j
public class ListFilesByName {

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
        String srcPath = "src/main/java";
        String outputPath = "docs/JavaFiles.md";
        List<JavaFileInfo> controllerFiles = new ArrayList<>();

        try {
            // 遍历收集Controller文件信息
            Files.walkFileTree(Paths.get(srcPath), new SimpleFileVisitor<Path>() {
                int fileIndex = 1;

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith("Controller.java")) {
                        // 获取包路径
                        String packagePath = file.toString()
                                .replace('\\', '/')
                                .replace(srcPath + "/", "")
                                .replace(".java", "")
                                .replace('/', '.');

                        controllerFiles.add(new JavaFileInfo(
                                fileIndex++,
                                fileName.replace(".java", ""),
                                packagePath,
                                0 // 默认标记为0
                        ));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    log.error("访问文件失败: " + file, exc);
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
                writer.write("# Controller文件列表\n\n");
                writer.write("| 序号 | 类名 | 包路径 | 标记 |\n");
                writer.write("|------|------|--------|------|\n");

                // 写入文件信息
                for (JavaFileInfo file : controllerFiles) {
                    writer.write(String.format("| %d | %s | %s | %d |\n",
                            file.index,
                            file.className,
                            file.packagePath,
                            file.mark));
                }

                log.info("文件已生成：" + outputPath);
            }

        } catch (IOException e) {
            log.error("生成文件列表时发生错误", e);
        }
    }
}
