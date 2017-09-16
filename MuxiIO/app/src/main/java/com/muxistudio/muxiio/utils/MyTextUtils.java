package com.muxistudio.muxiio.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by kolibreath on 17-8-2.
 */

public class MyTextUtils {

    public static final String LEFT_S_BRACKET = "\\[";
    public static final String RIGHT_S_BRACKET = "\\]";
    public static final String LEFT_PARENTHESIS = "\\(";
    public static final String RIGHT_PARENTHEIS = "\\)";



    //use to parse date
    //parsed date will be like 03 Aug 2017
    public static String parseDate(String date){
        String datePieces[] = date.substring(5,16).split(" ");
        String dateStr = datePieces[0] + " " + getMonth(datePieces[1]) + " " + datePieces[2];
        return dateStr;
    }

    //return month in English abbreviation eg: August -> Aug
    public static int getMonth(String month){
       int monthN = 0;
        if(month.equals("Jan")){
            monthN = 1;
        }
        if(month.equals("Feb")){
            monthN = 2;
        }
        if(month.equals("Mar")){
            monthN = 3;
        }
        if(month.equals("Apr")){
            monthN = 4;
        }
        if(month.equals("May")){
            monthN = 5;
        }
        if(month.equals("Jun")){
            monthN = 6;
        }
        if(month.equals("Jul")){
            monthN = 7;
        }
        if(month.equals("Aug")){
            monthN = 8;
        }
        if(month.equals("Sep")){
            monthN = 9;
        }
        return monthN;
    }

    //get share and split it to link and title
    public static String[] getDataFromOtherApp(String rawData){
        //if the length of title > 10, the title will be its substring and appends "..."
        //wrapped in cutTitle()
        String title ;
        String link;
        String rawTitle ;

        String dataPieces[] = rawData.split("http");

        title = dataPieces[0];
        rawTitle = dataPieces[0];
        link = "http" + dataPieces[1];

        String zhihuString = "（分享自知乎网）";


        //if the share comes from zhihu
        //the othersite is normal
        if(link.contains(zhihuString)){
            String titlePieces[] = title.split("】");
            title = titlePieces[0].substring(1,titlePieces[0].length());
            rawTitle = titlePieces[0].substring(1,titlePieces[0].length());

            String linkPieces[] = link.split(zhihuString);
            link= linkPieces[0];
        }

        if(length(title)>16){
            title = cutTitle(title);
        }
        return new String[]{title, link,rawTitle};
    }

    public static boolean isMarkDown(String data){
        if(data.contains("[")&&data.contains("]")&&data.contains("(")&&data.contains(")")){
            return true;
        }else{
            return false;
        }
    }

    public static String getLink(String rawData){

        String dataPieces[] = rawData.split("http");

        return new String("http" + dataPieces[1]);
    }

    public static List<String>[] markDownParser(String data){

        List<String>links = new ArrayList<>();
        List<String>titles = new ArrayList<>();
        int [] leftSquareBracket = new int[data.length()/4];
        int [] rightSquareBracket = new int[data.length()/4];
        int [] leftParenthesis = new int[data.length()/4];
        int [] rightParenthesis = new int[data.length()/4];

            getMarkDownIndex(data,leftSquareBracket,'[');
            getMarkDownIndex(data,rightSquareBracket,']');
            getMarkDownIndex(data,leftParenthesis,'(');
            getMarkDownIndex(data,rightParenthesis,')');


        for(int i=0;i<leftSquareBracket.length;i++){
            if(leftParenthesis[i]==0){
                break;
            }
            int leftSquareIndex = leftSquareBracket[i];
            int rightSquareIndex = rightSquareBracket[i];
            int rightParenthesisIndex =  rightParenthesis[i];
            int leftParentthesisIndex = leftParenthesis[i];

            String title = data.substring(leftSquareIndex+1,rightSquareIndex);
            String link = data.substring(leftParentthesisIndex+1,rightParenthesisIndex);
            links.add(link);
            titles.add(title);
        }

        List[] content = new List[]{titles, links};
        return  content;
    }

    public static void getMarkDownIndex(String data,int []array,char c){
        String regex = "";
        int index = 0;
        String copy = data;
        switch (c){
            case '[':
                regex = LEFT_S_BRACKET;
                break;
            case ']':
                regex = RIGHT_S_BRACKET;
                break;
            case '(':
                regex = LEFT_PARENTHESIS;
                break;
            case ')':
                regex = RIGHT_PARENTHEIS;
                break;
        }
        while(copy.contains(c+"")) {
            int temp = copy.indexOf(c);
            if (temp >= 0) {
                array[index++] = temp;
                copy = copy.replaceFirst(regex, "#");
            }
        }

    }

    //make title limit to 10 characters or less
    public static String cutTitle(String title){
        return title.substring(0,9) + "...";
    }

    public static String cutTitle(String title, int end){
        return title.substring(0,end) + "...";
    }

    //get length of a mixed string which consists of Chinese and English
    public static int length(String string){
        String chinese = "[\u0391-\uFFE5]";
        int valueLength = 0;
        int length = string.length();
        for(int i=0;i<length;i++){
            String temp = string.substring(i,i+1);
            if(temp.matches(chinese)){
                valueLength += 2;
            }else{
                valueLength ++;
            }
        }
        return valueLength;
    }

    //format date in share to things like :今天 昨天 or some specific date
    public static String formatDate(String parseDate){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM DD");
        String dateString  =  format.format(date);

        String formatDate ="" ;

        String datePieces[] = dateString.split(" ");
        String thisDay = datePieces[0];
        String thisMonth = datePieces[1];

        datePieces = parseDate.split(" ");
        //day, month those are the day the share released
        String day = datePieces[0];
        String month = datePieces[1];

        if(month.equals(thisMonth)&&day.equals(thisDay)){
            formatDate = "今天";
        }


        int interval = Integer.parseInt(thisDay) - parseInt(day);
        if(month.equals(thisMonth)&& interval==1){
            formatDate = "昨天";
        }
        if(month.equals(thisMonth)&& interval==2){
            formatDate = "前天";
        }
        return formatDate;
    }

    public static String urlFormat(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    public static boolean errorLink( String link){
        boolean errorFlag = false;
        if(link == null || link.equals("")) {
            ToastUtils.showShort("你的链接有问题");
            errorFlag = true;
        }
        return errorFlag;
    }

    public static String parseTime(String date){
        String timePieces[] = date.substring(18,25).split(":");
        return new String((Integer.parseInt(timePieces[0])+8)
                +""+":"+timePieces[1]+":"+timePieces[2]);
    }

    //format date to 2017年5月1日
    public static String formatDataInShare(String date){
        String datePieces[] = date.split(" ");
        return datePieces[2]+"年"+datePieces[1]+"月"+datePieces[0]+"日";
    }

    //format date to 08/20
    //be sure to use parseDate() and parse the raw date to the parsedDate format 20 7 2017
    public static String formatDateUsingSlash(String date){
        String datePieces[] = date.split(" ");
        return String.format("%02d",Integer.parseInt(datePieces[1])) +"/"+ datePieces[0];
    }
}
