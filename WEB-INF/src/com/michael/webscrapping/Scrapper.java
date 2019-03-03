package com.michael.webscrapping;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import com.michael.database.ConnectionManager;

import java.io.*; // Only needed if scraping a local File.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Scrapper {

    // Drag data from wikipedia and insert it into our file.sql
    public void cityScrapper() throws Exception {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "oracle.jdbc.driver.OracleDriver", "za", "za");
            FileWriter fileWriter = new FileWriter("/home/michael/Dropbox/GeneralizedDb/city.sql");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String countryId = new String();
            int asciiCode = 65;
            char fullLetter = (char) asciiCode;
            StringBuilder url = new StringBuilder("https://en.wikipedia.org/wiki/List_of_towns_and_cities_with_100,000_or_more_inhabitants/cityname:_A");
            Document doc = null;
            Element tbody = null;
            Elements trElts = null;
            Elements aElts = null;
            PreparedStatement preparedStatement = null;
            String sql = "select * from country where country_name =  ?";
            ResultSet resultSet = null;
            String cityName = new String();

            for(int i = 1; i <= 26; i++) {
                fullLetter = (char) asciiCode;
                url.setCharAt(url.length()-1, fullLetter);

                doc = Jsoup.connect(url.toString()).header("Host", "www.notimportant.fr").header("Connection", "keep-alive").header("Cache-Control", "max-age=0").header("Origin", "https://www.whatever.fr/").header("Upgrade-Insecure-Requests", "1").header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.48 Safari/537.36").header("Content-Type", "application/x-www-form-urlencoded").header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").referrer("blabla.fr").header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "en-US,en;q=0.8").get();
                tbody = doc.getElementsByTag("tbody").first();
                trElts = tbody.getElementsByTag("tr");
            
                for(Element tr : trElts) {
                    aElts = tr.select("td > a");
                    if(aElts.last() != null) {
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, aElts.last().text());
                        resultSet = preparedStatement.executeQuery();
                        if(resultSet.next()) {
                            countryId = resultSet.getString("country_id");
                            cityName = aElts.first().text();
                            if(cityName.contains("'")) {
                                int pos = cityName.indexOf("'");
                                cityName = cityName.substring(0, pos) + "'" + cityName.substring(pos);
                            }
                            printWriter.printf("INSERT INTO city (city_name, country) VALUES('%s', '%s');\n", cityName, countryId);
                        }
                        resultSet.close();
                    }
                }

                asciiCode++;
            }

            printWriter.close();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        
    }

    public Scrapper() {}

    public Scrapper(String url) {
		Document doc = null;
		try {
            // doc = Jsoup.connect(url).header("Host", "www.leboncoin.fr").header("Connection", "keep-alive").header("Cache-Control", "max-age=0").header("Origin", "https://www.leboncoin.fr/").header("Upgrade-Insecure-Requests", "1").header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.48 Safari/537.36").header("Content-Type", "application/x-www-form-urlencoded").header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").referrer(url).header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "en-US,en;q=0.8").get();
            doc = Jsoup.connect(url).header("Host", "www.notimportant.fr").header("Connection", "keep-alive").header("Cache-Control", "max-age=0").header("Origin", "https://www.whatever.fr/").header("Upgrade-Insecure-Requests", "1").header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.48 Safari/537.36").header("Content-Type", "application/x-www-form-urlencoded").header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").referrer("blabla.fr").header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "en-US,en;q=0.8").get();
            // System.out.println(doc);
		} catch (IOException ioe) {
			ioe.printStackTrace();
        }
        Elements liElts = doc.getElementsByClass("s-item");
		// Element table = doc.getElementById("datatable");
		// Elements rows = table.getElementsByTag("TR");
		
		for (Element li : liElts) {
            Elements title = li.select(".s-item__title");
            Elements price = li.select(".s-item__price");
            System.out.println(title.first().text() + " \\ " + price.first().text());
			// for (int i = 0; i < tds.size(); i++) {
			// 	if (i == 1) System.out.println(tds.get(i).text());
			// }
		}
	
	}
	
}