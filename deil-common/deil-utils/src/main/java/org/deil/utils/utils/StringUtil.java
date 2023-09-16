package org.deil.utils.utils;

import cn.hutool.core.util.ReUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {

	//region 构造
	/** yyyy-MM-dd日期格式 */
	//public static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	public static final String Empty = "";
	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String EMPTY = "";
	public static final String CRLF = "\r\n";
	public static final String NEWLINE = "\n";
	public static final String UNDERLINE = "_";
	public static final String COMMA = ",";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";
	//endregion

	//region 正常
	/**
	 * 是null或空，->.net{ String.IsNullOrEmpty() }
	 *
	 * @param str 货物
	 * @return boolean
	 * @time 2023/02/17
	 */
	public static boolean IsNullOrEmpty(String str) {
		return isEmpty(str);
	}

	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为不可见字符（如空格）<br>
	 * 3、不为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotBlank(String str) {
		return false == isBlank(str);
	}

	/**
	 * 字符串是否为空，空的定义如下 1、为null <br>
	 * 2、为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(String str) {
		return false == isEmpty(str);
	}


	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str
	 *            字符串
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, String prefix, String suffix) {
		return str.startsWith(prefix) && str.endsWith(suffix);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str
	 *            字符串
	 * @param wrapper
	 *            包装字符串
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str
	 *            字符串
	 * @param wrapper
	 *            包装字符
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str
	 *            字符串
	 * @param prefixChar
	 *            前缀
	 * @param suffixChar
	 *            后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, char prefixChar, char suffixChar) {
		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}

	/**
	 * 补充字符串以满足最小长度 StrUtil.padPre("1", 3, '0');//"001"
	 * 
	 * @param str
	 *            字符串
	 * @param minLength
	 *            最小长度
	 * @param fill
	 *            补充的字符
	 * @return 补充后的字符串
	 */
	@Deprecated
	public static String padLeft(String str, int minLength, char fill) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(fill);
		}
		sb.append(str);
		return sb.toString();
	}
	public static String padLeft2(String str, int minLength, char fill) {
		int diff = minLength - str.length();
		if (diff <= 0) {
			return str;
		}

		char[] charr = new char[minLength];
		System.arraycopy(str.toCharArray(), 0, charr, 0, str.length());
		for (int i = str.length(); i < minLength; i++) {
			charr[i] = fill;
		}
		return new String(charr);
	}

	/**
	 * 补充字符串以满足最小长度 StrUtil.padEnd("1", 3, '0');//"100"
	 * 
	 * @param str
	 *            字符串
	 * @param minLength
	 *            最小长度
	 * @param fill
	 *            补充的字符
	 * @return 补充后的字符串
	 */
	@Deprecated
	public static String padRight(String str, int minLength, char fill) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(str);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(fill);
		}
		return sb.toString();
	}
	public static String padRight2(String str, int minLength, char fill) {
		int diff = minLength - str.length();
		if (diff <= 0) {
			return str;
		}

		char[] charr = new char[minLength];
		System.arraycopy(str.toCharArray(), 0, charr, diff, str.length());
		for (int i = 0; i < diff; i++) {
			charr[i] = fill;
		}
		return new String(charr);
	}

	public static String stringToUnicode(String str) {
		StringBuffer sb = new StringBuffer();
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			sb.append("\\u" + Integer.toHexString(c[i]));
		}
		return sb.toString();
	}

	public static String unicodeToString(String unicode) {
		StringBuffer sb = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			int index = Integer.parseInt(hex[i], 16);
			sb.append((char) index);
		}
		return sb.toString();
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder() {
		return new StringBuilder();
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(int capacity) {
		return new StringBuilder(capacity);
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(String... strs) {
		final StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb;
	}

	/**
	 * 获得字符串对应字符集的byte数组
	 * 
	 * @param str
	 *            字符串
	 * @param charset
	 *            字符集编码
	 * @return byte数组
	 */
	public static byte[] bytes(String str, String charset) {
		if (null == str) {
			return null;
		}
		if (isBlank(charset)) {
			return null;
		}
		return str.getBytes(Charset.forName(charset));
	}

	/**
	 * 数字型String字符串转换成int型数组
	 * 
	 * @param str
	 *            string类型的数组
	 * @return
	 */
	public static Integer[] stringToIntegerArray(String[] str) {
		Integer array[] = new Integer[str.length];
		for (int i = 0; i < str.length; i++) {
			array[i] = Integer.parseInt(str[i]);
		}
		return array;
	}

	/**
	 * 数字型String字符串转换成Long型数组
	 * 
	 * @param str
	 *            string类型的数组
	 * @return
	 */
	public static Long[] stringTOLongArray(String[] str) {
		Long array[] = new Long[str.length];
		for (int i = 0; i < str.length; i++) {
			array[i] = Long.parseLong(str[i]);
		}
		return array;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param src
	 *            文件路径/名称 文件路径 C:\Users\Public\Pictures\Sample Pictures\test.jpg
	 * @return 如果文件后缀 jpg
	 */
	public static String getFileExt(String src) {

		String filename = src.substring(src.lastIndexOf(File.separator) + 1, src.length());// 获取到文件名

		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名称，不带文件后缀部分
	 * 
	 * @param src
	 *            文件路径 C:\Users\Public\Pictures\Sample Pictures\test.jpg
	 * @return 文件名称 不带文件后缀 test
	 */
	public static String getFileName(String src) {

		String filename = src.substring(src.lastIndexOf(File.separator) + 1, src.length());// 获取到文件名

		return filename.substring(0, filename.lastIndexOf("."));
	}

	/**
	 * 判断字符串是否为空（不能为空字符串）
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isNull(String src) {

		return src == null || src.length() == 0 || src.trim().length() == 0;
	}

	/**
	 * 检查数组中，是否含有当前元素
	 *
	 * @param arr
	 * @param checkValue
	 * @return
	 */
	public static Boolean checkArrayValue(String[] arr, String checkValue) {
		Boolean checkFlag = false;
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equals(checkValue)) {
					checkFlag = true;
					break;
				}
			}
		}
		return checkFlag;
	}

	/**
	 * 检查数组中元素，是否在checkValue中出现
	 *
	 * @param arr
	 * @param checkValue
	 * @return
	 */
	public static Boolean isContains(String[] arr, String checkValue) {
		Boolean checkFlag = false;
		if (arr != null && arr.length > 0) {
			for (String str : arr) {
				if (checkValue.indexOf(str)!=-1) {
					checkFlag = true;
					break;
				}
			}
		}
		return checkFlag;
	}

	/**
	 * @date 2022/8/19, 上午11:28 : 无数据自给定默认值
	 * @return java.lang.Object
	 */
	public static <T> T isBlankToDefault(T data, T toSet) {
		return (T) (isBlank(String.valueOf(data)) ? toSet : data);
	}

	/**
	 * @date 2022/7/4, 下午5:23 : 拼接文件路径
	 * @param path
	 * @param fileName
	 * @return java.lang.String
	 */
	public static String appendFilePath(String path, String fileName) {
		return new StringBuilder().append(path).append("/").append(fileName).toString();
	}

	/**
	 * @date 2022/8/12, 上午10:12 : 获取文件路径
	 * @param configName
	 * @return java.lang.Object
	 */
	public static Object getFilePathByClassLoader(Object clazz, String configName) {
		return clazz.getClass().getClassLoader().getResource(configName).getPath();//方案一：通过加载器
	}

	/**
	 * @date 2022/8/12, 上午10:12 : 获取文件路径
	 * @param configName
	 * @return java.lang.Object
	 */
	public Object getFilePathByResourceStream(String configName) {
		return StringUtil.class.getResourceAsStream("/" + configName);//方案二：通过流
	}

	/**
	 * @date 2022/8/12, 上午10:07 : 根据文件路径读取文件内容
	 * @param fileInPath
	 */
	/*public static String getFileContent(Object fileInPath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("")));
		if (fileInPath == null) {
			return null;
		}
		if (fileInPath instanceof String) {
			br = new BufferedReader(new FileReader(new File((String) fileInPath)));
		} else if (fileInPath instanceof InputStream) {
			br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
		}
		String line;
		StringBuilder resultBuilder= new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				resultBuilder.append(line + "\n");
			}
		} finally {
			br.close();
		}
		return resultBuilder.toString();
	}*/

	/***
	 * 是否小数
	 *
	 * @param input
	 * @return
	 */
	public static boolean isDigit(String input) {
		Matcher mer = Pattern.compile("^[+-]?[0-9]+.?[0-9]+$").matcher(input);
		return mer.find();
	}

	/***
	 * 是否小数
	 *
	 * @param input
	 * @return
	 */
	public static boolean IsDigit(char input) {
		return isDigit(String.valueOf(input));
	}

	/***
	 * 是否整数
	 *
	 * @param input
	 * @return
	 */
	public static boolean isDigitWithoutDot(String input) {
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}

	/***
	 * 是否整数
	 *
	 * @param input
	 * @return
	 */
	public static boolean IsDigitWithoutDot(char input) {
		return isDigitWithoutDot(String.valueOf(input));
	}

	/**
	 * 是否字母
	 *
	 * @param input
	 * @return boolean
	 * @time 2023/02/17
	 */
	public static boolean isLetter(String input) {
		Matcher mer = Pattern.compile("^[a-zA-Z]+$").matcher(input);
		return mer.find();
	}

	/**
	 * 是否字母
	 *
	 * @param input
	 * @return boolean
	 * @time 2023/02/17
	 */
	public static boolean IsLetter(char input) {
		return isLetter(String.valueOf(input));
	}

	/**
	 * 是否字母或数字
	 *
	 * @param input
	 * @return boolean
	 * @time 2023/02/17
	 */
	public static boolean IsLetterOrDigit(String input) {
		return input.matches("^[a-zA-Z0-9]+$");
	}

	/**
	 * 是否字母或数字
	 *
	 * @param input
	 * @return boolean
	 * @time 2023/02/17
	 */
	public static boolean IsLetterOrDigit(char input) {
		return IsLetterOrDigit(String.valueOf(input));
	}
	//endregion

	//region .net
	public static String[] split(String input, String split){
		return split(input, split, StringSplitOptions.None);
	}

	public static String[] split(String input, String split, StringSplitOptions stringSplitOptions){
		if (stringSplitOptions == null || StringSplitOptions.None.equals(stringSplitOptions)) {
			//.net默认-1，如StringSplitOptions.None，保留空元素
			return splitContentNoneArray(input, split);
		} else {
			//java默认0，如StringSplitOptions.RemoveEmptyEntries，不保留空元素
			return splitContentRemoveEmptyArray(input, split);
		}
	}

	/**
	 * 分割内容（适配函数）
	 * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.RemoveEmptyEntries) </p>
	 * <p>这样的函数效果是会去除分割后的数组中的长度为0的元素，但是有长度的空串或者非空串是不去除的</p>
	 *
	 * @param content   内容
	 * @param separator 分隔符
	 * @return 去除空元素后的所有报文
	 */
	public static String[] splitContentRemoveEmptyArray(String content, String separator) {
		// 分割
		String[] array = content.split(separator);
		// 去除空元素（长度不为为 0或 不为null）
		return Arrays.stream(array).filter(StringUtils::isNotEmpty).toArray(String[]::new);
	}

	/**
	 * 分割内容（适配函数）
	 * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.RemoveEmptyEntries) </p>
	 * <p>这样的函数效果是会去除分割后的数组中的长度为0的元素，但是有长度的空串或者非空串是不去除的</p>
	 *
	 * @param content   内容
	 * @param separator 分隔符
	 * @return 去除空元素后的所有报文
	 */
	public static List<String> splitContentRemoveEmpty(String content, String separator) {
		// 分割
		String[] array = content.split(separator);
		// 去除空元素（长度不为为 0或 不为null）
		return Arrays.stream(array).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
	}

	/**
	 * 分割内容（适配函数）
	 * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.None) </p>
	 * <p>这样的函数效果是不对内容做其他操作，但是如果分隔符后没有字符，也会附带一个空在数组中，</p>
	 * <p>也就是说，有多少个分隔符存在字符串，分割数组就要有分隔符数+1的元素存在</p>
	 *
	 * @param content   内容
	 * @param separator 分隔符
	 * @return 去除空元素后的所有报文
	 */
	public static String[] splitContentNoneArray(String content, String separator) {
		List<String> list = splitContentNone(content, separator);
		return list.toArray(new String[0]);
	}
	/**
	 * 分割内容（适配函数）
	 * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.None) </p>
	 * <p>这样的函数效果是不对内容做其他操作，但是如果分隔符后没有字符，也会附带一个空在数组中，</p>
	 * <p>也就是说，有多少个分隔符存在字符串，分割数组就要有分隔符数+1的元素存在</p>
	 *
	 * @param content   内容
	 * @param separator 分隔符
	 * @return 去除空元素后的所有报文
	 */
	public static List<String> splitContentNone(String content, String separator) {
		// 分割
		String[] array = content.split(separator);

		// 计算分隔符出现次数，加1，因为如果有2个分隔符，那就表示要有3份数据
		int count = ReUtil.count(separator, content);

		// 如果数量为0，表示没有，直接返回
		if (count > 0) {
			count++;
		}

		List<String> resultList = new ArrayList<>(Arrays.asList(array));

		// 如果长度不一致，则可能存在空没有分隔到
		if (array.length < count) {
			// 差值
			int deviation = count - array.length;
			for (int i = 0; i < deviation; i++) {
				resultList.add("");
			}
		}

		return resultList;
	}


	/**
	 * 适配.net中 TrimEnd()方法的效果
	 *
	 * @param text     文本
	 * @param trimChar 去除字符
	 * @return 结果
	 */
	public static String trimEnd(String text, char trimChar) {
		Assert.hasLength(text, "[TrimEnd] The text cannot be empty.");

		if (Objects.equals(trimChar, text.charAt(text.length() - 1))) {
			text = text.substring(0, text.length() - 2);
		}

		return text;
	}


	public enum StringSplitOptions {
		None,
		RemoveEmptyEntries,
	}
	//endregion
}
