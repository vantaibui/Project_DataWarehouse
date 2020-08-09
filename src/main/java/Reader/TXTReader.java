package Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class TXTReader {
	
	static final String NUMBER_REGEX = "^[0-9]+$";
	
	private String readLines(String value, String delim) {
		String values = "";
		StringTokenizer stoken = new StringTokenizer(value, delim);
		if (stoken.countTokens() > 0) {
			stoken.nextToken();
		}
		int countToken = stoken.countTokens();
		String lines = "(";
		for (int j = 0; j < countToken; j++) {
			String token = stoken.nextToken();
			if (Pattern.matches(NUMBER_REGEX, token)) {
				lines += (j == countToken - 1) ? token.trim() + ")," : token.trim() + ",";
			} else {
				lines += (j == countToken - 1) ? "'" + token.trim() + "')," : "'" + token.trim() + "',";
			}
			values += lines;
			lines = "";
		}
		return values;
	}

	public String readValuesTXT(File s_file, String delim) {
		String values = "";
		try {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(s_file)));
			String line;
//			Bỏ dòng đầu
			line = bReader.readLine();
			
			while ((line = bReader.readLine()) != null) {
				values += readLines(line, delim);
			}
			bReader.close();
			return values.substring(0, values.length() - 1);

		} catch (NoSuchElementException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
		TXTReader txtReader = new TXTReader();
		String url = "Data/Class/lophoc_sang_nhom11_2020.txt";
		File source = new File(url);
		String delim = "|";
		System.out.println(txtReader.readValuesTXT(source, delim));
	}
}
