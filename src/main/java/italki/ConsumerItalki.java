package italki;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class ConsumerItalki {
 
		public static String callURL(String myURL) {
		//System.out.println("Requested URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
		
	public static void main(String[] args) throws InterruptedException {


//String Url_inicial="https://www.italki.com/api/teachers?_r=&country=&hl=es-ES&i_token=&is_instant=&page=1&price_usd=&speak=&teach=&teacher_type=1";
//https://www.italki.com/api/teachers?_r=&country=&hl=es-ES&i_token=&is_instant=&page="+q+"&price_usd=&speak=&teach=&teacher_type=1
		
		//https://www.italki.com/api/teachers?_r=1491864948226&country=&hl=es-ES&i_token=&is_instant=&page=3&price_usd=min-1000&speak=&teach=english&teacher_type=0
		
		int q= 1;
		
		String continuar= "true";
		String c ="US";
		
		 //COUNTRY : US
		 // COUNTRY : GB REINO UNIDO
		// COUNTRY :AU
		
		while(continuar == "true"){
			System.out.println("----------------------- PAGINA "+q+ "----------------------");
			String jsonString = callURL("https://www.italki.com/api/teachers?_r=&country="+c+"&hl=es-ES&i_token=&is_instant=&page="+q+"&price_usd=min-1000&speak=&teach=english&teacher_type=0");
			JSONObject objeto = new JSONObject(jsonString);
			JSONObject meta = objeto.getJSONObject("meta");
	
			continuar =   meta.get("has_next").toString();
			System.out.println("hay siguiente pagina = "+continuar);
								 	    	    
		    JSONArray data_profes = objeto.getJSONArray("data");
		 				
			for(int i=0 ;i < data_profes.length(); i++){
				   JSONObject objeto_profe = (JSONObject) data_profes.get(i);
				   
				   JSONObject profe_detalle = objeto_profe.getJSONObject("teacher_info_obj");
			  
				  float min_price =  Float.valueOf(profe_detalle.get("min_price").toString());
				  int cantidad_alumnos = Integer.parseInt(profe_detalle.get("student_count").toString());
				  String pais = objeto_profe.get("origin_country_id").toString();
				  if(min_price < 80 ){
					  
					   System.out.print("italki.com/teacher/"+objeto_profe.get("id")+ " - ");
					   System.out.print(objeto_profe.get("nickname")+ " ");
					   System.out.print(pais + "  -  ");
					   if( cantidad_alumnos <= 20){
						   System.out.print(cantidad_alumnos +" Alumnos"); 
					   }
					   
					   if( Integer.parseInt(objeto_profe.get("is_pro").toString())== 1){
						   System.out.print("      Es profesor-----");
					   }
					   					  
					   System.out.print("-  -- -- - --- - ---Precio $"+min_price/10);
					   System.out.println();
				   }
				  
				  
			 }	
			q++;
			Thread.sleep(5000);	
				
		}
			
					
	}

 
}