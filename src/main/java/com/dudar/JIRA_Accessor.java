package com.dudar;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import webwork.util.TextUtil;

import javax.security.sasl.AuthenticationException;

public class JIRA_Accessor {

    public Map<Long, Record> getWorklogIssues(ConnectionData connectionData, String names){

//        List<String> names1 = new ArrayList<String>(Arrays.asList(names.split(",")));
//        List<String> resultNames = new ArrayList<>();
//        for(String a : names1){
//            resultNames.add(a.trim().replace(" ", "."));
//        }

        final URI jiraServerUri;
        Map<Long, Record> issuesList = new HashMap<>();
        ArrayList<Long> ids = new ArrayList<>();
        try {
            jiraServerUri = new URI(connectionData.getUrl());

            final JiraRestClient restClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraServerUri, connectionData.getUser(), connectionData.getPass());
            Set<String> asd = new HashSet<String>();
            Promise<SearchResult> searchJqlPromise = restClient.getSearchClient().searchJql("worklogAuthor in (" + names + ")", 1000, 0, asd);

            //TODO handle exception RestClientException for getIssues()
            for (Issue issue : searchJqlPromise.claim().getIssues()) {
                issuesList.put(issue.getId(), new Record(issue.getId(), issue.getKey(), issue.getSummary(), "", 0));
                ids.add(issue.getId());
            }
            return issuesList;
        } catch (URISyntaxException e) {
            System.out.println("URI Syntax Exception");
        }
        catch (RestClientException e){
            System.out.println("Rest Client Exception");
        }
        return null;
    }

    public void testResponse(ConnectionData connectionData, String users) throws AuthenticationException {

        List<String> names1 = new ArrayList<String>(Arrays.asList(users.split(",")));
        List<String> resultNames = new ArrayList<>();
        for(String a : names1){
            resultNames.add(a.trim().replace(" ", "."));
        }

        String auth = new String(Base64.encode(connectionData.getUser()+":"+connectionData.getPass()));
        Client client = Client.create();
        //TODO add '' for all items inside users list
//        String users1 = users;
//        users1.replaceAll(" ", "%20");
        String buff = "/rest/api/2/search?jql=worklogAuthor%20in%20("+String.join(",", resultNames)+")&maxResults=1000";
        String url_request = connectionData.getUrl()+buff;

        WebResource webResource = client.resource(String.valueOf(url_request));

        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json").accept("application/json").get(ClientResponse.class);

        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
        Map<Long, Record> issues = getWorklogIssues(connectionData, String.join(",", resultNames));
        for(Long id : issues.keySet()){
            url_request = connectionData.getUrl()+"/rest/api/2/issue/"+id+"/worklog";
            webResource = client.resource(url_request);
            response = webResource.header("Authorization", "Basic " + auth).type("application/json").accept("application/json").get(ClientResponse.class);

            statusCode = response.getStatus();
            if (statusCode == 401) {
                throw new AuthenticationException("Invalid Username or Password");
            }
            String response1 = response.getEntity(String.class);

            try {
                JSONObject jsonVal = new JSONObject(response1);

                JSONArray arr = jsonVal.getJSONArray("worklogs");
                for (int i = 0; i < arr.length(); i++)
                {
                    issues.get(id).updateAuthorAndTime(arr.getJSONObject(i).getJSONObject("author").getString("name"), arr.getJSONObject(i).getLong("timeSpentSeconds"));
                }


            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }

        for(Long id : issues.keySet()){
            issues.get(id).print(String.join(",", resultNames));
        }

    }

}
