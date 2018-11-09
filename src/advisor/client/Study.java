package advisor.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Study {
	public static final String COMPLETED = "Completed";
	public static final String MAXIMIZE = "MAXIMIZE";
	public static final String MINIMIZE = "MINIMIZE";
	public static final String BAYESIAN_OPTIMIZATION="BayesianOptimization";
	public static final String RANDOM_SEARCH="RandomSearchAlgorithm";
	public static final String GRID_SEARCH="GridSearchAlgorithm";
	public static final String POPULATION_SEARCH="PopulationBasedAlgorithm";
	
	private static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static SimpleDateFormat dtf = new SimpleDateFormat(pattern); 
	private int id;
	private String name;
	private JSONObject studyConfiguration;
	private String algorithm;
	private String status;
	private Date createdTime;
	private Date updatedTime;
	
	
	public Study(int id, String name, JSONObject studyConfiguration,
			String algorithm, String status, Date createdTime,
			Date updatedTime) {
		
		this.id = id;
		this.name = name;
		this.studyConfiguration = studyConfiguration;
		this.algorithm = algorithm;
		this.status = status;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}
	public Study(JSONObject jsonObject) throws JSONException, ParseException {
		
		
		this.id=jsonObject.getInt("id");
		this.name=jsonObject.getString("name");
		this.studyConfiguration=new JSONObject( jsonObject.getString("study_configuration"));
		this.algorithm=jsonObject.getString("algorithm");
		this.status=jsonObject.getString("status");
		this.createdTime= Study.dtf.parse(jsonObject.getString("created_time"));
		this.updatedTime= Study.dtf.parse(jsonObject.getString("updated_time"));
		
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public JSONObject getStudy_configuration() {
		return studyConfiguration;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public String getStatus() {
		return status;
	}
	public Date getCreated_time() {
		return createdTime;
	}
	public Date getUpdated_time() {
		return updatedTime;
	}
	
	
}
