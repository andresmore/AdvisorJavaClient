package advisor.client;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.annotations.Beta;


/**
 * An advisor client for Java
 * Based on the python implementation from https://github.com/tobegit3hub/advisor/tree/master/advisor_client
 * @author AndresM
 *
 */
public class AdvisorClient {
	
	private String endpoint="http://0.0.0.0:8000";

	public AdvisorClient(){
		
	}
	
	public AdvisorClient(String endpoint){
		this.endpoint=endpoint;
	}
	
	public Study createStudy(String name, JSONObject studyConfiguration, String algorithm) throws ClientProtocolException, IOException, JSONException, ParseException{
		String url= endpoint+"/suggestion/v1/studies";
		
		JSONObject requestData= new JSONObject();
		requestData.put("name", name);
		requestData.put("study_configuration", studyConfiguration);
		requestData.put("algorithm", algorithm);
		
		StringEntity entity = new StringEntity(requestData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpPost httpPost= new HttpPost(url);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPost);
		if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			return 	new Study(responseJSON.getJSONObject("data"));		
		}
		return null;
	}
	
	public List<Study> listStudies() throws ClientProtocolException, IOException, JSONException, ParseException{
		
		LinkedList<Study> list= new LinkedList<Study>();
		String url= endpoint+"/suggestion/v1/studies";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpGet httpGet= new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpGet);
		if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			JSONArray responseJSONData=responseJSON.getJSONArray("data");
			Iterator<Object> studies=responseJSONData.iterator();
			while(studies.hasNext()){
				list.add(new Study((JSONObject) studies.next()));
			}
			
		}
		
		return list;
		
	}
	
	public Study getStudyById(int studyId) throws ClientProtocolException, IOException, JSONException, ParseException{
		String url= endpoint+"/suggestion/v1/studies/"+studyId;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpGet httpGet= new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpGet);
		if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			
			return 	new Study(responseJSON.getJSONObject("data"));		
		}
		return null;
		
	}
	
	public List<Trial> getSuggestions(int studyId, int trialsNumber) throws ClientProtocolException, IOException, JSONException, ParseException{
		LinkedList<Trial> list= new LinkedList<Trial>();
		if(trialsNumber<=0)
			trialsNumber=1;
		String url= endpoint+"/suggestion/v1/studies/"+studyId+"/suggestions";
		JSONObject requestData= new JSONObject();
		
		requestData.put("trials_number", trialsNumber);
		StringEntity entity = new StringEntity(requestData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpPost httpPost= new HttpPost(url);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPost);
	    if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			JSONArray responseJSONData=responseJSON.getJSONArray("data");
			Iterator<Object> trials=responseJSONData.iterator();
			while(trials.hasNext()){
				list.add(new Trial((JSONObject) trials.next()));
			}
			
		}
		
		return list;
	}
	
	
	public boolean isStudyDone(int studyId) throws ClientProtocolException, JSONException, IOException, ParseException{
		Study study=getStudyById(studyId);
		
		if(study==null)
			return false;
		
		
		if(Study.COMPLETED.equals(study.getStatus()))
			return true;
		
		List<Trial> trials=listTrials(studyId);
		for (Trial trial : trials){ 
			if(!Trial.COMPLETED.equals(trial.getStatus()))
				return false;
		}	
		String url= endpoint+"/suggestion/v1/studies/"+studyId;
		
		JSONObject putData= new JSONObject();
		putData.put("status", "Completed");
		StringEntity entity = new StringEntity(putData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPut httpPut= new HttpPut(url);
		httpPut.setEntity(entity);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPut);
	    if(responseHttp.getStatusLine().getStatusCode()==200)
	    	return true;
		
	    //Warning: No ok from server
	    return true;
	}

	public List<Trial> listTrials(int studyId) throws ClientProtocolException, IOException, JSONException, ParseException{
		LinkedList<Trial> list= new LinkedList<Trial>();
		String url= endpoint+"/suggestion/v1/studies/"+studyId+"/trials";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpGet httpGet= new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpGet);
	    if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			JSONArray responseJSONData=responseJSON.getJSONArray("data");
			Iterator<Object> trials=responseJSONData.iterator();
			while(trials.hasNext()){
				list.add(new Trial((JSONObject) trials.next()));
			}
			
		}
		return list;
	}
	
	@Beta
	public List<TrialMetric> listTrialMetrics(int studyId, int trialId){
		return null;
	}
	
	public Trial getBestTrial(int studyId) throws ClientProtocolException, JSONException, IOException, ParseException{
		if(!this.isStudyDone(studyId))
			return null;
		
		Study st= this.getStudyById(studyId);
		JSONObject configuration=st.getStudy_configuration();
		String studyGoal=configuration.getString("goal");
		List<Trial> trials=this.listTrials(studyId);
		
		Trial bestTrial=null;
		for (Trial trial : trials) {
			if (trial.getObjectiveValue() != null) {
				if(bestTrial==null)
					bestTrial=trial;
				else if (studyGoal.equals(Study.MAXIMIZE)) {
					if(trial.getObjectiveValue()>bestTrial.getObjectiveValue()){
						bestTrial=trial;
					}
				}
				else if (studyGoal.equals(Study.MINIMIZE)) {
					if(trial.getObjectiveValue()<bestTrial.getObjectiveValue()){
						bestTrial=trial;
					}
				}
			}
			
		}
		
		
		return bestTrial;
	}
	
	public Trial getTrial(int studyId, int trialId) throws ClientProtocolException, IOException, JSONException, ParseException{
		String url= endpoint+"/suggestion/v1/studies/"+studyId+"/trials/"+trialId;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpGet httpGet= new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpGet);
	    if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			
			return 	new Trial(responseJSON.getJSONObject("data"));		
		}
	    return null;
	}
	
	public TrialMetric createTrialMetric(int studyId, int trialId, JSONObject trainingStep, Double objectiveValue) throws ClientProtocolException, IOException, JSONException, ParseException{
		String url= endpoint+"/suggestion/v1/studies/"+studyId+"/trials/"+trialId+"/metrics";
		
		JSONObject requestData= new JSONObject();
		requestData.put("training_step", trainingStep==null?JSONObject.NULL:trainingStep);
		requestData.put("objective_value", objectiveValue);
		
		StringEntity entity = new StringEntity(requestData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		 
		HttpPost httpPost= new HttpPost(url);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPost);
		if(responseHttp.getStatusLine().getStatusCode()==200){
			String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			return 	new TrialMetric(responseJSON.getJSONObject("data"));		
		}
		return null;
		
	}
	
	public Trial completeTrialWithTensorboardMetrics(Trial trial, LinkedList<HashMap<String,Double>> list) throws ClientProtocolException, JSONException, IOException, ParseException{
		double objectiveValue=0;
		for (HashMap<String,Double> scalarSummary : list) {
			objectiveValue=scalarSummary.get("value");
			this.createTrialMetric(trial.getStudyId(), trial.getId(), new JSONObject(scalarSummary.get("step")), objectiveValue);
			
		}
		String url= endpoint+"/suggestion/v1/studies/"+trial.getStudyId()+"/trials/"+trial.getId();
		JSONObject putData= new JSONObject();
		putData.put("status", "Completed");
		putData.put("objective_value", objectiveValue);
		StringEntity entity = new StringEntity(putData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPut httpPut= new HttpPut(url);
		httpPut.setEntity(entity);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPut);
	    if(responseHttp.getStatusLine().getStatusCode()==200){
	    	String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			
			return 	new Trial(responseJSON.getJSONObject("data"));
	    }
	    return null;
	}
	
	public Trial completeTrialWithOneMetric(Trial trial, Double metric) throws ClientProtocolException, JSONException, IOException, ParseException{
		this.createTrialMetric(trial.getStudyId(), trial.getId(), null, metric);
		String url= endpoint+"/suggestion/v1/studies/"+trial.getStudyId()+"/trials/"+trial.getId();
		JSONObject putData= new JSONObject();
		putData.put("status", "Completed");
		putData.put("objective_value", metric);
		StringEntity entity = new StringEntity(putData.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPut httpPut= new HttpPut(url);
		httpPut.setEntity(entity);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse responseHttp = httpclient.execute(httpPut);
	    if(responseHttp.getStatusLine().getStatusCode()==200){
	    	String response=EntityUtils.toString(responseHttp.getEntity()) ;
			JSONObject responseJSON= new JSONObject(response);
			
			return 	new Trial(responseJSON.getJSONObject("data"));
	    }
	    return null;
	}

	public static void main(String[] args) {
		
		AdvisorClient cl= new AdvisorClient();
		try {
			JSONObject studyConfiguration= new JSONObject("{\"goal\": \"MAXIMIZE\",	\"randomInitTrials\": 3,	\"maxTrials\": 5,	\"maxParallelTrials\": 1,	\"params\": [		{			\"parameterName\": \"hidden1\",			\"type\": \"INTEGER\",			\"minValue\": 1,			\"maxValue\": 10,			\"scallingType\": \"LINEAR\"}, {\"parameterName\": \"learning_rate\",\"type\": \"DOUBLE\",		\"minValue\": 0.01,	\"maxValue\": 0.5,		\"scallingType\": \"LINEAR\"	}	]}");
			Study st=cl.createStudy("StudyPruebaTest", studyConfiguration, Study.BAYESIAN_OPTIMIZATION);
			System.out.println(st.getAlgorithm());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
