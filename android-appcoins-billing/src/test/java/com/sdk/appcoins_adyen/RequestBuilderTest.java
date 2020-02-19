package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.utils.RequestBuilderUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class RequestBuilderTest {

  @Test public void urlBuilderTest() {
    Map<String, String> queries = new HashMap<>();
    List<String> paths = new ArrayList<>();
    queries.put("key1", "value1");
    queries.put("key2", "value2");
    paths.add("path1");
    paths.add("path2");
    String url = RequestBuilderUtils.buildUrl("base", "endpoint", paths, queries);
    Assert.assertEquals("baseendpoint/path1/path2?key1=value1&key2=value2", url);
  }

  @Test public void pathOnlyUrlBuilderTest() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    String url =
        RequestBuilderUtils.buildUrl("base", "endpoint", paths, new HashMap<String, String>());
    Assert.assertEquals("baseendpoint/path1/path2", url);
  }

  @Test public void queryOnlyUrlBuilderTest() {
    Map<String, String> queries = new HashMap<>();
    List<String> paths = new ArrayList<>();
    queries.put("key1", "value1");
    queries.put("key2", "value2");
    String url = RequestBuilderUtils.buildUrl("base", "endpoint", paths, queries);
    Assert.assertEquals("baseendpoint?key1=value1&key2=value2", url);
  }

  @Test public void urlOnlyBuilderTest() {
    Map<String, String> queries = new HashMap<>();
    List<String> paths = new ArrayList<>();
    String url = RequestBuilderUtils.buildUrl("base", "endpoint", paths, queries);
    Assert.assertEquals("baseendpoint", url);
  }

  @Test public void bodyBuilderTest() {
    Map<String, Object> queries = new HashMap<>();
    queries.put("key1", "value1");
    queries.put("key3", false);
    queries.put("key4", "{ \"payment\":\"valueb2\"}");
    String body = RequestBuilderUtils.buildBody(queries);
    Assert.assertEquals("{\"key1\":\"value1\",\"key3\":false,\"key4\":{ \"payment\":\"valueb2\"}}",
        body);
  }
}
