package advisor.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Trial {
	
	public static final String COMPLETED = "Completed";
	private static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static SimpleDateFormat dtf = new SimpleDateFormat(pattern); 
	
	private int id;
	private int studyId;
	private String name;
	private JSONObject parameterValues;
	private Double objectiveValue;
	private String status;
	private Date createdTime;
	private Date updatedTime;
	
	public Trial(int id, int studyId, String name, JSONObject parameterValues,
			Double objectiveValue, String status, Date createdTime,
			Date updatedTime) {
		this.id = id;
		this.studyId = studyId;
		this.name = name;
		this.parameterValues = parameterValues;
		this.objectiveValue = objectiveValue;
		this.status = status;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}
	
	public Trial(JSONObject jsonObject) throws JSONException, ParseException {
		
		
		this.id=jsonObject.getInt("id");
		this.studyId=jsonObject.getInt("study_id");
		this.name=jsonObject.getString("name");
		Object objParams=jsonObject.get("parameter_values");
		this.parameterValues=objParams.equals(JSONObject.NULL)?null:new JSONObject( jsonObject.getString("parameter_values"));
		Object objValue=jsonObject.get("objective_value");
		this.objectiveValue=objValue.equals(JSONObject.NULL)?null:Double.parseDouble(objValue.toString());
		this.status=jsonObject.getString("status");
		this.createdTime= Trial.dtf.parse(jsonObject.getString("created_time"));
		this.updatedTime= Trial.dtf.parse(jsonObject.getString("updated_time"));
		/*
		 * {"data": {"id": 2, "name": "Study1", "study_configuration": "{\"goal\": \"MAXIMIZE\", \"randomInitTrials\": 3, \"maxTrials\": 5, \"maxParallelTrials\": 1, \"params\": [{\"parameterName\": \"learning_rate\", \"type\": \"DOUBLE\", \"minValue\": 0.001, \"maxValue\": 1, \"scallingType\": \"LOG\"}]}", "algorithm": "RandomSearchAlgorithm", "status": "Pending", "created_time": "2018-08-30T22:46:28.838Z", "updated_time": "2018-08-30T22:46:28.838Z"}}
		 */
	}

	public int getId() {
		return id;
	}

	public int getStudyId() {
		return studyId;
	}

	public String getName() {
		return name;
	}

	public JSONObject getParameterValues() {
		return parameterValues;
	}

	public Double getObjectiveValue() {
		return objectiveValue;
	}

	public String getStatus() {
		return status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}
	
	

	
	
}
