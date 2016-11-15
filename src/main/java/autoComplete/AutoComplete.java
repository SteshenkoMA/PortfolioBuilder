package autoComplete;

import java.net.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* 
   Данный класс испольуется для вызова списка тикеров, которые содержат в себе
   символы введенные пользователем в JTextField

   This class is used to display a list of tickers that contain
   the characters entered by the user in JTextField
*/

public class AutoComplete {
    
    public static JSONArray tickersList = new JSONArray();
    
    public static void showVariants(String symbol){
     
   
        
    lab1: try {
                
        URL url = new URL("http://autoc.finance.yahoo.com/autoc?query="+symbol+"&region=US&lang=en-US&callback=YAHOO.Finance.SymbolSuggest.ssCallback");
                
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String error ="/**/YAHOO.Finance.SymbolSuggest.ssCallback({\"ResultSet\":{\"result\":null,\"error\":{\"code\":\"internal-error\",\"description\":\"Timeout after";
       
        inputLine = in.readLine();
                
        /* 
           labl - метка, она используется по следующей причине:
           иногда сервер autoc.finance.yahoo.com выдает ошибку (выше error)
           ниже цикл проверяет: вернул ли сервер при вызове этого метода ошибку,
           если вернул, то метод showVariants() вызывается еще раз (внутри самого себя)
           и так может продолжается до тех пор, пока метод не вернет список тикеров без ошибки,
           а далее используется break lab1 - выход из блока кода помеченного меткой,
           чтобы метод не продолжал выполнятся, так как список тикеров уже был получен, 
           и нет необходимости выполнять оставшийся код
        
           labl - is a label, it is used for the following reason:
           sometimes the server autoc.finance.yahoo.com gives an error (above error)
           the loop below checks: if the server returned the error when you call this method,
           if returned, the method showVariants() is called again (inside of itself)
           and so it continues until the method returns a list of tickers without error,
           and then uses break lab1 - the output from the code block labeled with a label
           so method does not continue executing, as the list of tickers has already been received 
           and there is no need to execute remaining code
        */     
        
        if (inputLine.contains(error)){
            
                showVariants(symbol);
                in.close();
                break lab1;
            }
        
        /* 
           http://autoc.finance.yahoo.com/autoc?query - возвращает список тикеров в json формате
           org.json - библиотека, которая позволяет преобразовать этот формат в удобный вид
           код ниже:
           1) обрезает строчку inputLine, до нужного json формата, так как изначально url
              содержит в себе ненужные символы
           2) добавляет тикеры в массив ra вида JSONArray
              
           http://autoc.finance.yahoo.com/autoc?query - returns a list of tickers in json format
           org.json- is a library that allows to convert this format into useful information
           code below:
           1) truncates inputLine to the desired json format, because originally the url
               contains unnecessary characters
           2) add the tickers in an array ra type JSONArray
        */
        
        if (inputLine != null){
                        
            String jsonFormat = inputLine.substring(39, inputLine.length()-2 );

               JSONObject js = null;
	        try {
				js = new JSONObject(jsonFormat);
				JSONArray ra = js.getJSONObject("ResultSet").getJSONArray("Result");
                                tickersList = ra;
				for(int i=0;i<ra.length();++i)
				{
					JSONObject item = ra.getJSONObject(i);
					String s = "";
					s+=item.getString("symbol") + " ";
					s+=item.getString("name");
					s+="(" + item.getString("exch") + ")";
                                        
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}

            
            
        }
        
        in.close();

        
        } catch (IOException e) {
        }
}
    }
