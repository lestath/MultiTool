package MyGraph;

/**
 * Klasa zawierająca konfiguracje dla modułu Graph
 * @author lestath
 *
 */
public class Config {
	public static String[] ALLOWED_EXTENSIONS = {".csv",".CSV"}; //możliwe rozszerzenia plików do załadowania grafu
	public static String [] BLACK_LIST= {"e^x","e^(x"}; //czarna lista wykresów dla współrzędnych biegunowych (takie konstrukcje nie będą rysowane na wykresie biegunowym)
	public static String CSV_SEPARATOR=";" ;//znak separatora w pliku csv 
}
