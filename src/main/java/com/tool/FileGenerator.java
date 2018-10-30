//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tool;

import com.tool.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class FileGenerator {
    public FileGenerator() {
    }

    public static void generate(File templateFile, File configFile) throws Exception {
        Map<String, String[]> confMap = parseConfigFile(configFile);
        String fileType = ((String[])confMap.get("type"))[0];
        String[] filePath = (String[])confMap.get("path");
        String[] packageNames = null;
        String template = FileReader.read(templateFile);
        Map<String, List<String>> templateMap = parseTemplateFile(templateFile);
        List<String> parameters = (List)templateMap.get("parameters");
        List<String> contentList = (List)templateMap.get("contents");
        String content = (String)contentList.get(0);
        String[] contents = null;
        Iterator paramIter = parameters.iterator();

        while(true) {
            String parameter;
            String p;
            do {
                if (!paramIter.hasNext()) {
                    if ("java".equals(fileType)) {
                        packageNames = (String[])confMap.get("package");
                        String[] classNames = (String[])confMap.get("className");

                        for(int i = 0; i < classNames.length; ++i) {
                            if (packageNames.length == 1) {
                                generateJavaFile(classNames[i], packageNames[0], contents[i]);
                            } else {
                                generateJavaFile(classNames[i], packageNames[i], contents[i]);
                            }
                        }
                    } else {
                        for(int i = 0; i < filePath.length; ++i) {
                            generateFile(filePath[i], contents[i]);
                        }
                    }

                    return;
                }

                parameter = (String)paramIter.next();
                p = parameter.replace("\\", "");
            } while(confMap.get(p) == null);

            String[] values = (String[])confMap.get(p);
            contents = new String[values.length];
            String content_copy = content;

            for(int i = 0; i < values.length; ++i) {
                content = content.replaceAll(parameter, values[i]);
                contents[i] = content;
                content = content_copy;
            }
        }
    }

    public static void generateJavaFile(String className, String packageName, String content) throws IOException {
        String fileName = className + ".java";
        String filePath = packageName.replaceAll("\\.", "\\\\") + "\\" + fileName;
        filePath = FileUtil.getRootPath()[0] + "\\" + filePath;
        File file = new File(filePath);
        file.createNewFile();
        PrintWriter writer = new PrintWriter(file);
        content = "package " + packageName + ";\n" + content;
        writer.write(content);
        writer.close();
    }

    public static void generateFile(String filePath, String content) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            throw new Exception("file " + filePath + " is already exist!");
        } else {
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.write(content);
            writer.close();
        }
    }

    public static Map<String, String[]> parseConfigFile(File configFile) throws IOException {
        String name = configFile.getName();
        if (!name.endsWith(".properties")) {
            return null;
        } else {
            Map<String, String[]> map = new HashMap();
            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile));
            Set<Object> configNames = properties.keySet();
            Iterator configNameIter = configNames.iterator();

            while(true) {
                while(configNameIter.hasNext()) {
                    Object obj = configNameIter.next();
                    String key = (String)obj;
                    String value = properties.getProperty(key);
                    if (value.startsWith("{") && value.endsWith("}")) {
                        value = value.substring(1, value.length() - 1);
                        map.put(key, value.split(","));
                    } else {
                        String[] s = new String[]{value};
                        map.put(key, s);
                    }
                }

                return map;
            }
        }
    }

    public static Map<String, List<String>> parseTemplateFile(File templateFile) throws Exception {
        if (!templateFile.getName().endsWith(".tmp")) {
            return null;
        } else {
            Map<String, List<String>> map = new HashMap();
            List<String> parameters = new ArrayList();
            String templateContent = FileReader.read(templateFile);
            char[] chars = templateContent.toCharArray();
            StringBuilder builder = new StringBuilder();
            int flag = 0;

            for(int i = 0; i < chars.length; ++i) {
                if ((!" ".equals(String.valueOf(chars[i])) || flag == -1) && (!"\n".equals(String.valueOf(chars[i])) || flag == -1)) {
                    builder.append(chars[i]);
                    if (builder.toString().equals("set") && flag == 0) {
                        builder.delete(0, builder.length());
                        flag = 1;
                    }

                    if (builder.toString().equals("parameter") && flag == 1) {
                        builder.delete(0, builder.length());
                        flag = 2;
                    }

                    if (";".equals(String.valueOf(chars[i])) && flag == 2) {
                        if (!builder.toString().startsWith("$")) {
                            throw new Exception("parameter must start with $");
                        }

                        builder.insert(0, "\\");
                        String parameter = builder.deleteCharAt(builder.length() - 1).toString();
                        parameters.add(parameter);
                        builder.delete(0, builder.length());
                        flag = 0;
                    }

                    if (flag == 0 && "$begin".equals(builder.toString())) {
                        builder.delete(0, builder.length());
                        flag = -1;
                    }

                    if (flag == -1 && builder.toString().endsWith("$end")) {
                        builder.delete(builder.length() - 3, builder.length());
                        String content = builder.toString();
                        List<String> contents = new LinkedList();
                        contents.add(content);
                        map.put("contents", contents);
                    }
                }
            }

            map.put("parameters", parameters);
            return map;
        }
    }
}
