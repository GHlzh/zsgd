package com.example.zsgd.controller;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        public  String song_name;
        public  String singer_name;
        public  String song_link;
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
        int size = href_list.size();
//        int size = 5;
        for(int i = 0; i < size; ++i) {
            try {
//                TimeUnit.SECONDS.sleep(1);
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
        int size = href_list.size();
//        int size = 5;
        for(int i = 0; i < size; ++i) {
            try {
//                TimeUnit.SECONDS.sleep(1);
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
        int size = href_list.size();
//        int size = 5;
        for(int i=0;i<size;i++){
            try {
//                TimeUnit.SECONDS.sleep(1);
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
    @GetMapping("/getMusicNews")
    public ArrayList<MusicNews> getMusicNews(@RequestParam("str") @NotNull String str)throws IOException{
        ArrayList<MusicNews> jsonObjectArrayList;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/songs.json");
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(streamReader);
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
//            System.out.println(streamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        jsonObjectArrayList = gson.fromJson(String.valueOf(stringBuilder),new TypeToken<ArrayList<MusicNews>>(){}.getType());
        ArrayList<MusicNews> songArrayList = new ArrayList<>();
        for(MusicNews music:jsonObjectArrayList){
            if (music.song_name.equals(str)){
                songArrayList.add(music);
                return songArrayList;
            }
            else if(music.singer_name.equals(str)){
                songArrayList.add(music);
            }
        }
        return songArrayList;
    }
}
