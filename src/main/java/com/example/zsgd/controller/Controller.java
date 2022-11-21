package com.example.zsgd.controller;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class Controller {
    class StudentNews{
        public String url;
        public String title;
        public String content;
    }
    class NatureNews{
        public  String brief_introduction_url;
        public  String title;
        public  String content;
        public  String original_text_url;
    }
    class USSTNews{
        public  String url;
        public String title;
        public  String content;
    }
    class MusicNews{
        public  String name;
        public  String url;
        public  int flag;
    }
    @GetMapping("/getStudentNews")
    public ArrayList<StudentNews> getStudentNews() throws IOException {
        ArrayList<StudentNews> jsonObjectArrayList = new ArrayList<>();
        String url = "http://www.daxuejia.com/shsj/";
        String link_url = "http://www.daxuejia.com";
        Document document = Jsoup.connect(url).get();
        List<String> href_list = new ArrayList<>();
        List<String> title_list = new ArrayList<>();
        List<Element> html_list = document.getElementsByClass("lbra");
        for(Element element : html_list){
            title_list.add(element.text());
            href_list.add(link_url+element.select("a").attr("href"));
        }
//        int size = href_list.size();
        int size = 5;
        for(int i = 0; i < href_list.size(); ++i) {
            try {
                TimeUnit.SECONDS.sleep(1);
                Document doc = Jsoup.connect((String)href_list.get(i)).get();
                List<Element> html_list2 = doc.getElementsByClass("showb");
                for (Element element:html_list2){
                    StudentNews studentNews = new StudentNews();
                    studentNews.url = href_list.get(i);
                    studentNews.title = title_list.get(i);
                    studentNews.content = element.text();
                    if (studentNews.url!=null && studentNews.title!=null && studentNews.content!=null)
                        jsonObjectArrayList.add(studentNews);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return jsonObjectArrayList;
    }
    @GetMapping("/getNatureNews")
    public ArrayList<NatureNews> getNatureNews() throws IOException {
        ArrayList<NatureNews> jsonObjectArrayList = new ArrayList<>();
        String url = "http://www.naturechina.com/categories/sci";
        String link_url = "http://www.naturechina.com";
        Document document = Jsoup.connect(url).get();
        List<String> href_list = new ArrayList<>();
        List<String> title_list = new ArrayList<>();
        List<Element> html_list = document.getElementsByClass("itemlist");
        for(Element element : html_list){
            title_list.add(element.getElementsByClass("title").text());
            href_list.add(link_url+element.select("a").attr("href"));
        }
//        int size = href_list.size();
        int size = 5;
        for(int i = 0; i < size; ++i) {
            try {
                TimeUnit.SECONDS.sleep(1);
                Document doc = Jsoup.connect((String)href_list.get(i)).get();
                List<Element> html_list2 = doc.getElementsByClass("doinum");
                for (Element element:html_list2){
                    NatureNews natureNews = new NatureNews();
                    natureNews.brief_introduction_url = href_list.get(i);
                    natureNews.title = title_list.get(i);
                    natureNews.original_text_url = element.select("a").attr("href");
                    natureNews.content = doc.getElementsByClass("paragraph").text();
                    if (natureNews.brief_introduction_url!=null && natureNews.title!=null
                    && natureNews.original_text_url!=null && natureNews.content!=null)
                        jsonObjectArrayList.add(natureNews);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return jsonObjectArrayList;
    }
    @GetMapping("/getUSSTNews")
    public ArrayList<USSTNews> getUSSTNews() throws IOException {
        ArrayList<USSTNews> jsonObjectArrayList = new ArrayList<>();
        String url = "http://jwc.usst.edu.cn/10238/list.htm";
        String link_url = "http://jwc.usst.edu.cn";
        Document document = Jsoup.connect(url).get();
        List<Element> html_list;
        List<String>  href_list = new ArrayList<>();
        List<String> title_list = new ArrayList<>();
        html_list = document.getElementsByClass("news_title");
        for(Element element : html_list){
            if (element.select("a").attr("href").contains("http"))
            {
                continue;
            }
            else {
                title_list.add(element.getElementsByClass("news_title").text());
                href_list.add(link_url+element.select("a").attr("href"));
            }
        }
//        int size = href_list.size();
        int size = 5;
        for(int i=0;i<size;i++){
            try {
                TimeUnit.SECONDS.sleep(1);
                Document doc = Jsoup.connect(href_list.get(i)).get();
                USSTNews usstNews = new USSTNews();
                usstNews.url = href_list.get(i);
                usstNews.content = doc.getElementsByClass("wp_articlecontent").text();
                usstNews.title = title_list.get(i);
                if (usstNews.url!=null && usstNews.content!=null && usstNews.title!=null)
                    jsonObjectArrayList.add(usstNews);
            }
            catch (Exception e){
                continue;
            }
        }
        return jsonObjectArrayList;
    }
//    @GetMapping("/getMusicNews")
//    public ArrayList<MusicNews> getMusicNews(@RequestParam("song") @NotNull String song)throws IOException{
//        ArrayList<MusicNews> jsonObjectArrayList = new ArrayList<>();
//        Map<String,String> headers = new HashMap<>();
//        headers.put("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
//        String name = song;
//
//        String url = "https://music.taihe.com/search?word=" + name;
//        Connection c = Jsoup.connect(url);
//        Connection connection = c.headers(headers);
//        Document document = connection.get();
//        String regex = "<a href=\"/song/(.+?)\"";
//        Pattern pattern = Pattern.compile(regex);
//        List<String> tsid = new ArrayList<>();
//        Matcher matcher = pattern.matcher(String.valueOf(document));
//        String r = "";
//        MusicNews musicNews = new MusicNews();
//        musicNews.name = name;
//        musicNews.url = "null";
//        musicNews.flag = 0;
//        while (matcher.find())
//        {
//            tsid.add(matcher.group(1));
//        }
//        int flag = 0;
//        for (String str:tsid){
//            if (flag == 0)
//                r  = "sign=0dfd10fa6229ead6d2ff7768fde60213&appid=16073360&TSID="+str+"&timestamp=1668686436";
//            else
//                r = "sign=994aa548d2643489fad70631a8280ac3&appid=16073360&TSID="+str+"&timestamp=1668780054";
//            url = "https://music.91q.com/v1/song/tracklink?"+r;
////           System.out.println(url);
//            try {
//                Connection.Response doc =
//                        Jsoup.connect(url).timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/" +
//                                "537.36 (KHTML, like Gecko) Chrome/75.0." +
//                                "3770.100 Safari/537.36").ignoreContentType(true).execute();
//                JSONObject jsonObject = JSONObject.parseObject(doc.body());
//                JSONObject object = (JSONObject) jsonObject.get("data");
//                String path = (String) object.get("path");
//                String real_name = (String) object.get("title");
//                musicNews.name = real_name;
//                musicNews.url = path;
//                musicNews.flag = 1;
//                break;
//            }catch (Exception e){
//                flag = 1;
//                continue;
//            }
//        }
//        jsonObjectArrayList.add(musicNews);
//        return jsonObjectArrayList;
//    }
}
