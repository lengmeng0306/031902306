package com.company;
package com.ccytsoft.wkc.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 敏感词汇service
 *
 * @author  kuangxiang(kuangxiang666@yeah.net)
 * @date 15:06 2017/12/12
 */
@Data
@Service
public class SensitiveWordService {

    private StringBuilder replaceAll;

    /**
     * 编码
     * <P>
     * 在读敏感词汇文件时需要用到
     */
    private String encoding = "UTF-8";

    /**
     * 替换字符窜
     * <P>
     * 用于替换敏感词汇的字符窜
     */
    private String replceStr = "*";

    /**
     *单次替换的敏感词汇的长度
     */
    private int replceSize = 500;

    /**
     * 敏感词汇文件
     * <P>
     * 此文件放在资源文件的根目录下
     */
    private String fileName = "censorwords.txt";

    private List<String> arrayList;

    /**
     * 包含的敏感词列表，过滤掉重复项
     */
    public Set<String> sensitiveWordSet;

    /**
     * 包含的敏感词列表，包括重复项，统计次数
     */
    public List<String> sensitiveWordList;

    /**
     * 移除敏感词汇
     *
     * @param str 需要过滤的字符窜
     *
     * @return 过滤之后的字符窜
     */
    public String removeSensitiveWord(String str){
        SensitiveWordService sw = new SensitiveWordService("censorwords.txt");
        sw.InitializationWork();
        return sw.filterInfo(str);
    }

    /**
     * 拦截信息
     * <P>
     * 过滤掉敏感词汇的方法
     *
     * @param str 将要被过滤信息
     *
     * @return 过滤后的信息
     */
    public String filterInfo(String str) {
        sensitiveWordSet = new HashSet<String>();
        sensitiveWordList= new ArrayList<>();
        StringBuilder buffer = new StringBuilder(str);
        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>(arrayList.size());
        String temp;
        for(int x = 0; x < arrayList.size();x++) {
            temp = arrayList.get(x);
            int findIndexSize = 0;
            for(int start = -1;(start=buffer.indexOf(temp,findIndexSize)) > -1;){
                //System.out.println("###replace="+temp);
                findIndexSize = start+temp.length();//从已找到的后面开始找
                Integer mapStart = hash.get(start);//起始位置
                //满足1个，即可更新map
                if(mapStart == null || (mapStart != null && findIndexSize > mapStart)){
                    hash.put(start, findIndexSize);
                    //System.out.println("###敏感词："+buffer.substring(start, findIndexSize));
                }
            }
        }
        Collection<Integer> values = hash.keySet();
        for(Integer startIndex : values){
            Integer endIndex = hash.get(startIndex);
            //获取敏感词，并加入列表，用来统计数量
            String sensitive = buffer.substring(startIndex, endIndex);
            //System.out.println("###敏感词："+sensitive);
            if (!sensitive.contains("*")) {//添加敏感词到集合
                sensitiveWordSet.add(sensitive);
                sensitiveWordList.add(sensitive);
            }
            buffer.replace(startIndex, endIndex, replaceAll.substring(0,endIndex-startIndex));
        }
        hash.clear();
        return buffer.toString();
    }

    /**
     *   初始化敏感词库
     */
    private void InitializationWork() {
        replaceAll = new StringBuilder(replceSize);
        for(int x=0;x < replceSize;x++)
        {
            replaceAll.append(replceStr);
        }
        //加载词库
        arrayList = new ArrayList<String>();
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try {
            read = new InputStreamReader(SensitiveWordService.class.getClassLoader().getResourceAsStream(fileName),encoding);
            bufferedReader = new BufferedReader(read);
            for(String txt = null;(txt = bufferedReader.readLine()) != null;){
                if(!arrayList.contains(txt))
                    arrayList.add(txt);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(null != bufferedReader)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(null != read)
                    read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试方法
     *
     * @param args 参数
     */
    public static void main(String[] args){
        long startNumer = System.currentTimeMillis();
        SensitiveWordService sw = new SensitiveWordService("censorwords.txt");
        sw.InitializationWork();
        //System.out.println("敏感词的数量：" + arrayList.size());
        String str = "你好呀，我这里有敏感词汇，来过滤我呀";
        System.out.println("被检测字符串长度:"+str.length());
        str = sw.filterInfo(str);
        long endNumber = System.currentTimeMillis();
        //System.out.println("语句中包含敏感词的个数为：" + sensitiveWordSet.size() + "。包含：" + sensitiveWordSet);
        //System.out.println("语句中包含敏感词的个数为：" + sensitiveWordList.size() + "。包含：" + sensitiveWordList);
        System.out.println("总共耗时:"+(endNumber-startNumer)+"ms");
        System.out.println("替换后的字符串为:\n"+str);
        System.out.println("替换后的字符串长度为:\n"+str.length());
    }

    /**
     * 有参构造
     * <P>
     * 文件要求路径在src或resource下，默认文件名为censorwords.txt
     * @param fileName 词库文件名(含后缀)
     */
    public SensitiveWordService(String fileName) {

        this.fileName = fileName;
    }

    /**
     * 有参构造
     *
     * @param replceStr 敏感词被转换的字符
     * @param replceSize 初始转义容量
     */
    public SensitiveWordService(String replceStr, int replceSize){
        this.replceStr = fileName;
        this.replceSize = replceSize;
    }

    /**
     * 无参构造
     */
    public SensitiveWordService(){
    }
}
3、或者使用如下service代码:

        package com.ccytsoft.wkc.filters;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Enumeration;
        import java.util.Properties;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

/**
 * 【匹配度可以，速度较慢】
 * Java关键字过滤：http://blog.csdn.net/linfssay/article/details/7599262
 * @author ShengDecheng
 *
 */
public class KeyWordFilter {

    private static Pattern pattern = null;
    private static int keywordsCount = 0;

    // 从words.properties初始化正则表达式字符串
    private static void initPattern() {
        StringBuffer patternBuffer = new StringBuffer();
        try {
            //words.properties
            InputStream in = KeyWordFilter.class.getClassLoader().getResourceAsStream("keywords.properties");
            Properties property = new Properties();
            property.load(in);
            Enumeration<?> enu = property.propertyNames();
            patternBuffer.append("(");
            while (enu.hasMoreElements()) {
                String scontent = (String) enu.nextElement();
                patternBuffer.append(scontent + "|");
                //System.out.println(scontent);
                keywordsCount ++;
            }
            patternBuffer.deleteCharAt(patternBuffer.length() - 1);
            patternBuffer.append(")");
            //System.out.println(patternBuffer);
            // unix换成UTF-8
            // pattern = Pattern.compile(new
            // String(patternBuf.toString().getBytes("ISO-8859-1"), "UTF-8"));
            // win下换成gb2312
            // pattern = Pattern.compile(new String(patternBuf.toString()
            // .getBytes("ISO-8859-1"), "gb2312"));
            // 装换编码
            pattern = Pattern.compile(patternBuffer.toString());
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private static String doFilter(String str) {
        Matcher m = pattern.matcher(str);
//        while (m.find()) {// 查找符合pattern的字符串
//            System.out.println("The result is here :" + m.group());
//        }
        // 选择替换方式，这里以* 号代替
        str = m.replaceAll("*");
        return str;
    }

    public static void main(String[] args) {
        long startNumer = System.currentTimeMillis();
        initPattern();
        //String str = "我日，艹，fuck，你妹的 干啥呢";
        System.out.println("敏感词的数量：" + keywordsCount);
        String str = "你好呀，我这里有敏感词汇，来过滤我呀";
        System.out.println("被检测字符串长度:"+str.length());
        str = doFilter(str);
        //高效Java敏感词、关键词过滤工具包_过滤非法词句：http://blog.csdn.net/ranjio_z/article/details/6299834
        //FilteredResult result = WordFilterUtil.filterText(str, '*');
        long endNumber = System.currentTimeMillis();
        System.out.println("总共耗时:"+(endNumber-startNumer)+"ms");
        System.out.println("替换后的字符串为:\n"+str);
        //System.out.println("替换后的字符串为:\n"+result.getFilteredContent());
        //System.out.println("替换后的字符串为1:\n"+result.getOriginalContent());
        //System.out.println("替换后的字符串为2:\n"+result.getBadWords());
    }
}

