package MainServer;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {
	public static final Object JSON_MAPPER = new Object();

	public String readJson(String name, String psw) {

		JSONParser parser = new JSONParser();
		String student = "";

		try {

			JSONArray a = null;
			a = (JSONArray) parser
					.parse(new FileReader("/home/alumnos/sibanez/workspace/Server/src/MainServer/subjects.json"));

			for (Object o : a) {
				JSONObject jsonObject = (JSONObject) o;

				String nm = (String) jsonObject.get("name");
				String snm = (String) jsonObject.get("surname");
				String subj = (String) jsonObject.get("subject");
				String email = (String) jsonObject.get("email");
				String ps = (String) jsonObject.get("password");
				String day = (String) jsonObject.get("days");
				String time = (String) jsonObject.get("time");
				if (nm.equals(name) && ps.equals(psw)) {
					System.out.println("subject: " + subj + ", name: " + nm);
					student += nm + ";" + snm + ";" + subj + ";" + email + ";" + day+ ";" + time+ "|";
				}
			}
			
			if(student.equals("")){
				student = "fail";
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		
		return student;
	}

}
