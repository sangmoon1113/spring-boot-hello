package com.springboot.hello.controller;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/opensearch")
public class OpensearchController {

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    @GetMapping(value = "/{variable}")
    public SearchResponse getName(@PathVariable("variable") String var) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        //System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        //System.setProperty("javax.net.ssl.trustStore", "C:\\Temp\\keystore");

        credentialsProvider
                .setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));

        SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true);
        final SSLContext sslContext = sslBuilder.build();


        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                        .setSSLContext(sslContext)
                        //.setSSLHostnameVerifier((s, sslSession) -> true)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .setDefaultCredentialsProvider(credentialsProvider));
        SearchResponse searchResponse;
        try (RestHighLevelClient client = new RestHighLevelClient(builder)) {

            SearchRequest searchRequest = new SearchRequest("sm_books", "python-test-index");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery("title", "*" + var + "*");

            sourceBuilder.query(matchQueryBuilder);
            searchRequest.source(sourceBuilder);

            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        }

        return searchResponse;
    }
}
