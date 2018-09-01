package advisor.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class TrialMetric {

	private static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static SimpleDateFormat dtf = new SimpleDateFormat(pattern); 
	
	private int id;
	private int trialId;
	private JSONObject trainingStep;
	private Double objectiveValue;
	private Date createdTime;
	private Date updatedTime;
	
	public TrialMetric(int id, int trialId, JSONObject trainingStep,
			Double objectiveValue, Date createdTime, Date updatedTime) {
		super();
		this.id = id;
		this.trialId = trialId;
		this.trainingStep = trainingStep;
		this.objectiveValue = objectiveValue;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public TrialMetric(JSONObject jsonObject) throws JSONException, ParseException {
		
		
		this.id=jsonObject.getInt("id");
		this.trialId=jsonObject.getInt("trial_id");
		Object trStepObj=jsonObject.get("training_step");
		this.trainingStep=trStepObj.equals(JSONObject.NULL)?null:new JSONObject( jsonObject.getString("training_step"));
		Object objValue=jsonObject.get("objective_value");
		this.objectiveValue=objValue.equals(JSONObject.NULL)?null:(Double)objValue;
		this.createdTime= TrialMetric.dtf.parse(jsonObject.getString("created_time"));
		this.updatedTime= TrialMetric.dtf.parse(jsonObject.getString("updated_time"));
		/*
		 * {"data": {"id": 2, "name": "Study1", "study_configuration": "{\"goal\": \"MAXIMIZE\", \"randomInitTrials\": 3, \"maxTrials\": 5, \"maxParallelTrials\": 1, \"params\": [{\"parameterName\": \"learning_rate\", \"type\": \"DOUBLE\", \"minValue\": 0.001, \"maxValue\": 1, \"scallingType\": \"LOG\"}]}", "algorithm": "RandomSearchAlgorithm", "status": "Pending", "created_time": "2018-08-30T22:46:28.838Z", "updated_time": "2018-08-30T22:46:28.838Z"}}
		 */
	}

	public static String getPattern() {
		return pattern;
	}

	public static SimpleDateFormat getDtf() {
		return dtf;
	}

	public int getId() {
		return id;
	}

	public int getTrialId() {
		return trialId;
	}

	public JSONObject getTrainingStep() {
		return trainingStep;
	}

	public Double getObjectiveValue() {
		return objectiveValue;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}
	
	
	
}
