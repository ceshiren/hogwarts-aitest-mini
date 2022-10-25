package com.hogwartstest.aitestmini.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件工具类
 *
 */
@Slf4j
public class FileUtil {

	public static String saveFile(MultipartFile file, String pathname) {
		try {
			File targetFile = new File(pathname);
			if (targetFile.exists()) {
				return pathname;
			}

			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			file.transferTo(targetFile);

			return pathname;
		} catch (Exception e) {
			log.error(""+e);;
		}

		return null;
	}

	public static boolean deleteFile(String pathname) {
		File file = new File(pathname);
		if (file.exists()) {
			boolean flag = file.delete();
			file.getParentFile().setWritable(true);

			/*if (flag) {
				File[] files = file.getParentFile().listFiles();
				if (files == null || files.length == 0) {
					file.getParentFile().delete();
				}
			}*/

			return flag;
		}

		return false;
	}

	public static String getPath() {
		return "/" + LocalDate.now().toString().replace("-", "/") + "/";
	}

	/**
	 * 将文本写入文件
	 *
	 * @param value
	 * @param path
	 */
	public static void saveTextFile(String value, String path) {
		FileWriter writer = null;
		try {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			writer = new FileWriter(file);
			writer.write(value);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getText(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}

		try {
			return getText(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getText(InputStream inputStream) {
		InputStreamReader isr = null;
		BufferedReader bufferedReader = null;
		try {
			isr = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(isr);
			StringBuilder builder = new StringBuilder();
			String string;
			while ((string = bufferedReader.readLine()) != null) {
				string = string + "\n";
				builder.append(string);
			}

			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}


	public static void main(String[] args) {

		String filePath = "G:\\ceba\\report\\106_fc7ca2cde13ea69412573dba3220ded3 (1)\\testdir_106_1225_8868";
		String pattern = "\\-\\d.html";

		List<String> resultList = getFileNamesByRegex(filePath, pattern);

		log.info("resultList== "+resultList.get(0));

		//File[] paths = file.listFiles(filter);

       /* if(Objects.isNull(fileList) || fileList.isEmpty()){
            return ResultDto.fail("根据展示文件的访问路径在压缩文件中未查到对应文件: " + "aitest_test");
        }*/

		/*log.info("size= "+paths.length);
		File tt = paths[0];

		log.info("getPath== "+tt.getPath());*/

		//listFile1(file);
		//listFile2(file);
		//listFile3(file);
		//listFile4(file);
		//listFile5(file);
	}

	/**
	 *  查询特定目录下匹配正则表达式的目录地址，返回一个列表
	 * @param filePath 特定目录完整路径
	 * @param pattern 正则表达式
	 * @return
	 */
	public static List<String> getFileNamesByRegex(String filePath, String pattern) {
		return getFileNamesByRegex(filePath, pattern, true, true);
	}

	/**
	 *  查询特定目录下匹配正则表达式的目录地址，返回一个列表
	 * @param filePath 特定目录完整路径
	 * @param pattern 正则表达式
	 * @param recursionFlag 是否递归子目录 true 是 false 否
	 * @param emptyRootFlag 是否将根目录置为空，即仅保留子目录 true 是 false 否
	 * @return
	 */
	public static List<String> getFileNamesByRegex(String filePath, String pattern, boolean recursionFlag, boolean emptyRootFlag) {
		File file = new File(filePath);

		Pattern r = Pattern.compile(pattern);

		List<String> fileNameList = new ArrayList<>();

		if(recursionFlag){
			listFileName1(file, fileNameList);
		}else {
			fileNameList = Arrays.asList(listFileName(file));
		}

		List<String> resultList = new ArrayList<>();
		for (String fileName : fileNameList) {
			Matcher m = r.matcher(fileName);
			log.info(fileName);
			if(m.find()){
				log.info("m.find()==  "+fileName);
				if(emptyRootFlag){
					String newFileName = fileName.replace(file.getAbsolutePath(), "");
					resultList.add(newFileName);
				}else {
					resultList.add(fileName);
				}

			}
		}
		return resultList;
	}

	/**
	 * 列出该目录下所有的目录名和文件名 不会列出整个目录名称，
	 * 也不会遍历子目录
	 * @param file
	 */
	public static String[] listFileName(File file){
		String[] filenames = file.list();
		return filenames;
	}

	/**
	 * 列出该目录下所有的目录名和文件名 不会列出整个目录，
	 * 也不会遍历子目录
	 * @param file
	 */
	public static File[] listFile(File file){
		//直接列出目录下的，产生的是File对象，也不会遍历子目录
		File[] files = file.listFiles();
		return files;
	}

	/**
	 * 能够遍历file和file子目录下的所有内容
	 * @param file
	 */
	public static void listFile1(File file, List<File> fileList){
		File[] files = file.listFiles();
		for (File file2 : files) {
			if(file2.isFile()){//如果是文件
				fileList.add(file2);
			}else if(file2.isDirectory()){//如果是目录 进行递归调用
				listFile1(file2, fileList);
			}
		}
	}

	/**
	 * 能够遍历file和file子目录下所有的文件名称
	 * @param file
	 */
	public static void listFileName1(File file, List<String> fileList){
		File[] files = file.listFiles();
		for (File file2 : files) {
			if(file2.isFile()){//如果是文件
				fileList.add(file2.getAbsolutePath());
			}else if(file2.isDirectory()){//如果是目录 进行递归调用
				listFileName1(file2, fileList);
			}
		}
	}


	public static void listFile2(File file){
		//String[] filenames = file.list(new MyFileNameFilter());
		String[] filenames = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".java");
			}
		});
		for (String string : filenames) {
			log.info(file.getAbsolutePath()+"/"+string);
		}
	}

	public static void listFile3(File file){
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// pathname 就是file下的每个文件or目录
				// log.info(pathname);
				return pathname.getAbsolutePath().endsWith(".java");
			}
		});
		log.info("=======================");
		for (File file2 : files) {
			log.info(file2.getAbsolutePath());
		}
	}

	//递归拿出.java文件，包括子目录下的
	public static void listFile4(File file){
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isFile())
					return pathname.getAbsolutePath().endsWith(".java");
				else if(pathname.isDirectory())
					listFile4(pathname);
				return true;

			}
		});
		for (File file2 : files) {
			if(file2.isFile())
				log.info(file2.getAbsolutePath());
		}

	}

	public static void listFile5(File file){
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				//log.info(dir.getAbsolutePath()+" ... "+name);
				File file1 = new File(dir,name);
				if(file1.isFile())
					return name.endsWith(".java");
				else if(file1.isDirectory())
					listFile5(file1);
				return true;
			}
		});
		for (File file2 : files) {
			if(file2.isFile())
				log.info(file2.getAbsolutePath());
		}
	}

	static class MyFileNameFilter implements FilenameFilter{
		//file列出的每个文件都要调用accept方法，返回true留下，false被过滤
		@Override
		public boolean accept(File dir, String name) {
			//File dir 代表父目录 name代表直接子的名字是文件名or目录名
			//log.info(dir.getAbsolutePath()+" ... "+name);
			return name.endsWith(".java");
		}

	}


}
